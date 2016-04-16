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
public class JobsGetter implements Callable<Set<Job>> {

	private static final Logger logger = LoggerFactory.getLogger(
		JobsGetter.class);

	private JsonGetter jsonGetter;
	private String jenkinsURL;

	public JobsGetter(JsonGetter jsonGetter, String jenkinsURL) {
		this.jsonGetter = jsonGetter;
		this.jenkinsURL = jenkinsURL;
	}

	@Override
	public Set<Job> call() throws Exception {
		return getJobs(jsonGetter, jenkinsURL);
	}

	public static Set<Job> getJobs(
			JsonGetter jsonGetter, ExecutorService executor,
			Set<String> jenkinsURLs)
		throws Exception {

		CompletionService<Set<Job>> completionService =
			new ExecutorCompletionService<Set<Job>>(executor);

		Set<Future<Set<Job>>> activeFutures = new HashSet<>();

		Set<Job> jobs = new HashSet<>();

		try {
			logger.info("Getting jobs from {} servers", jenkinsURLs.size());

			for (String jenkinsURL : jenkinsURLs) {
				activeFutures.add(
					completionService.submit(
						new JobsGetter(jsonGetter, jenkinsURL)));
			}

			while (activeFutures.size() > 0) {
				Future<Set<Job>> completedFuture = completionService.take();

				activeFutures.remove(completedFuture);

				jobs.addAll(completedFuture.get());

				logger.debug("{} threads still active", activeFutures.size());
			}
		}
		catch (ExecutionException e) {
			logger.error("Invoked thread threw an exception");
			logger.error("Cancelling remaining threads");

			for (Future<Set<Job>> future : activeFutures) {
				future.cancel(true);
			}

			throw e;
		}

		return jobs;
	}

	public static Set<Job> getJobs(JsonGetter jsonGetter, String jenkinsURL)
		throws Exception {

		StringBuilder sb = new StringBuilder();

		sb.append(jenkinsURL);
		sb.append("api/json?");
		sb.append(Job.QUERY_PARAMETER);

		JSONObject rootJson = jsonGetter.getJson(sb.toString());

		Set<Job> jobs = new HashSet<>();

		for (JSONObject jobJson : getJobJsons(rootJson)) {
			jobs.add(new Job(jobJson));
		}

		return jobs;
	}

	public static Set<JSONObject> getJobJsons(JSONObject rootJson)
		throws Exception {

		Set<JSONObject> jobJsons = new HashSet<>();

		JSONArray jobsJson = rootJson.getJSONArray("jobs");

		for (int i = 0; i < jobsJson.length(); i++) {
			jobJsons.add(jobsJson.getJSONObject(i));
		}

		return jobJsons;
	}

}