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

/**
 * @author Kevin Yen
 */
public class JenkinsJobsGetter implements Callable<Set<JenkinsJob>> {

	private JsonGetter jsonGetter;
	private String jenkinsURL;

	public JenkinsJobsGetter(JsonGetter jsonGetter, String jenkinsURL) {
		this.jsonGetter = jsonGetter;
		this.jenkinsURL = jenkinsURL;
	}

	@Override
	public Set<JenkinsJob> call() throws Exception {
		return getJenkinsJobs(jsonGetter, jenkinsURL);
	}

	public static Set<JenkinsJob> getJenkinsJobs(JsonGetter jsonGetter, ExecutorService executor, Set<String> jenkinsURLs) throws Exception {
		CompletionService<Set<JenkinsJob>> completionService = new ExecutorCompletionService<Set<JenkinsJob>>(executor);

		Set<Future<Set<JenkinsJob>>> activeFutures = new HashSet<>();

		Set<JenkinsJob> jenkinsJobs = new HashSet<>();

		try {
			for (String jenkinsURL : jenkinsURLs) {
				activeFutures.add(completionService.submit(new JenkinsJobsGetter(jsonGetter, jenkinsURL)));
			}

			while (activeFutures.size() > 0) {
				Future<Set<JenkinsJob>> completedFuture = completionService.take();

				activeFutures.remove(completedFuture);

				jenkinsJobs.addAll(completedFuture.get());
			}
		}
		catch (ExecutionException e) {
			for (Future<Set<JenkinsJob>> future : activeFutures) {
				future.cancel(true);
			}

			throw e;
		}

		return jenkinsJobs;
	}

	public static Set<JenkinsJob> getJenkinsJobs(JsonGetter jsonGetter, String jenkinsURL) throws Exception {
		StringBuilder sb = new StringBuilder();

		sb.append(jenkinsURL);
		sb.append("api/json?tree=jobs[name,url]");

		JSONObject rootJson = jsonGetter.getJson(sb.toString());

		return getJenkinsJobs(rootJson);
	}

	public static Set<JenkinsJob> getJenkinsJobs(JSONObject rootJson) throws Exception {
		Set<JenkinsJob> jenkinsJobs = new HashSet<>();

		for (JSONObject jobJson : getJobJsons(rootJson)) {
			jenkinsJobs.add(getJenkinsJob(jobJson));
		}

		return jenkinsJobs;
	}

	public static JenkinsJob getJenkinsJob(JSONObject jobJson) throws Exception {
			String name = jobJson.getString("name");
			String url = jobJson.getString("url");

			return new JenkinsJob(name, url);
	}

	public static Set<JSONObject> getJobJsons(JSONObject rootJson) throws Exception {
		Set<JSONObject> jobJsons = new HashSet<>();

		JSONArray jobsJson = rootJson.getJSONArray("jobs");

		for (int i = 0; i < jobsJson.length(); i++) {
			jobJsons.add(jobsJson.getJSONObject(i));
		}

		return jobJsons;
	}

}