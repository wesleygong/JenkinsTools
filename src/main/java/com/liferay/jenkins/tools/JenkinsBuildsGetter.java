/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.jenkins.tools;

import java.util.HashSet;
import java.util.Set;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kevin Yen
 */
public class JenkinsBuildsGetter implements Callable<Set<JenkinsBuild>> {

	private static final Logger logger = LoggerFactory.getLogger(JenkinsBuildsGetter.class);

	private JsonGetter jsonGetter;
	private JenkinsJob jenkinsJob;

	public JenkinsBuildsGetter(JsonGetter jsonGetter, JenkinsJob jenkinsJob) {
		this.jsonGetter = jsonGetter;
		this.jenkinsJob = jenkinsJob;
	}

	@Override
	public Set<JenkinsBuild> call() throws Exception {
		return getJenkinsBuilds(jsonGetter, jenkinsJob);
	}

	public static Set<JenkinsBuild> getJenkinsBuilds(JsonGetter jsonGetter, ExecutorService executor, Set<JenkinsJob> jenkinsJobs) throws Exception {
		CompletionService<Set<JenkinsBuild>> completionService = new ExecutorCompletionService<Set<JenkinsBuild>>(executor);

		Set<Future<Set<JenkinsBuild>>> activeFutures = new HashSet<>();

		Set<JenkinsBuild> jenkinsBuilds = new HashSet<>();

		try {
			logger.info("Getting builds for {} jobs ...", jenkinsJobs.size());

			for (JenkinsJob jenkinsJob : jenkinsJobs) {
				activeFutures.add(completionService.submit(new JenkinsBuildsGetter(jsonGetter, jenkinsJob)));
			}

			while (activeFutures.size() > 0) {
				Future<Set<JenkinsBuild>> completedFuture = completionService.take();

				activeFutures.remove(completedFuture);

				jenkinsBuilds.addAll(completedFuture.get());

				logger.debug("{} threads still active.", activeFutures.size());
			}
		}
		catch (ExecutionException e) {
			logger.error("Invoked thread threw an exception. Cancelling remaining threads.");

			for (Future<Set<JenkinsBuild>> future : activeFutures) {
				future.cancel(true);
			}

			throw e;
		}

		return jenkinsBuilds;
	}

	public static Set<JenkinsBuild> getJenkinsBuilds(JsonGetter jsonGetter, JenkinsJob jenkinsJob) throws Exception {
		StringBuilder sb = new StringBuilder();

		sb.append(jsonGetter.convertURL(jenkinsJob.getURL()));
		sb.append("api/json?tree=builds[building,number,url]");

		JSONObject jobJson = jsonGetter.getJson(sb.toString());

		Set<JenkinsBuild> jenkinsBuilds = new HashSet<>();

		for (JSONObject buildJson : getBuildJsons(jobJson)) {
			jenkinsBuilds.add(getJenkinsBuild(buildJson, jenkinsJob));
		}

		return jenkinsBuilds;
	}

	public static Set<JenkinsBuild> getJenkinsBuilds(JsonGetter jsonGetter, String jenkinsURL) throws Exception {
		StringBuilder sb = new StringBuilder();

		sb.append(jenkinsURL);
		sb.append("api/json?tree=jobs[name,url,builds[building,number,url]]");

		JSONObject rootJson = jsonGetter.getJson(sb.toString());

		return getJenkinsBuilds(rootJson);
	}

	public static Set<JenkinsBuild> getJenkinsBuilds(JSONObject rootJson) throws Exception {
		Set<JenkinsBuild> jenkinsBuilds = new HashSet<>();

		for (JSONObject jobJson : JenkinsJobsGetter.getJobJsons(rootJson)) {
			JenkinsJob jenkinsJob = JenkinsJobsGetter.getJenkinsJob(jobJson);

			for (JSONObject buildJson : getBuildJsons(jobJson)) {
				jenkinsBuilds.add(getJenkinsBuild(buildJson, jenkinsJob));
			}
		}

		return jenkinsBuilds;
	}

	public static JenkinsBuild getJenkinsBuild(JSONObject buildJson, JenkinsJob jenkinsJob) {
		int number = buildJson.getInt("number");
		boolean building = buildJson.getBoolean("building");
		String url = buildJson.getString("url");

		return new JenkinsBuild(jenkinsJob, number, building, url);
	}

	public static Set<JSONObject> getBuildJsons(JSONObject jobJson) throws Exception {
		Set<JSONObject> buildJsons = new HashSet<>();

		JSONArray buildsJson = jobJson.getJSONArray("builds");

		for (int i = 0; i < buildsJson.length(); i++) {
			buildJsons.add(buildsJson.getJSONObject(i));
		}

		return buildJsons;
	}

}