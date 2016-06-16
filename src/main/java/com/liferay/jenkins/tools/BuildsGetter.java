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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kevin Yen
 */
public class BuildsGetter implements Callable<Collection<Build>> {

	private static final Logger logger = LoggerFactory.getLogger(
		BuildsGetter.class);

	private JsonGetter jsonGetter;
	private Job job;

	public BuildsGetter(JsonGetter jsonGetter, Job job) {
		this.jsonGetter = jsonGetter;
		this.job = job;
	}

	@Override
	public Collection<Build> call() throws Exception {
		return getBuilds(jsonGetter, job);
	}

	public static Collection<Build> getBuilds(
			JsonGetter jsonGetter, ExecutorService executor,
			Collection<Job> jobs)
		throws Exception {

		return getBuilds(jsonGetter, executor, jobs, 60);
	}

	public static Collection<Build> getBuilds(
			JsonGetter jsonGetter, ExecutorService executor,
			Collection<Job> jobs, int timeout)
		throws Exception {

		CompletionService<Collection<Build>> completionService =
			new ExecutorCompletionService<Collection<Build>>(executor);

		Set<Future<Collection<Build>>> activeFutures = new HashSet<>();

		Collection<Build> builds = new HashSet<>();

		try {
			logger.info("Getting builds for {} jobs", jobs.size());

			for (Job job : jobs) {
				activeFutures.add(
					completionService.submit(
						new BuildsGetter(jsonGetter, job)));
			}

			while (activeFutures.size() > 0) {
				Future<Collection<Build>> completedFuture = completionService.poll(
					timeout, TimeUnit.SECONDS);

				if (completedFuture == null) {
					throw new TimeoutException("BuildsGetter timed out");
				}

				activeFutures.remove(completedFuture);

				builds.addAll(completedFuture.get());

				logger.debug("{} threads still active", activeFutures.size());
			}
		}
		catch (ExecutionException e) {
			logger.error("Invoked thread threw an exception");
			logger.error("Cancelling remaining threads");

			for (Future<Collection<Build>> future : activeFutures) {
				future.cancel(true);
			}

			throw e;
		}

		return builds;
	}

	public static Collection<Build> getBuilds(JsonGetter jsonGetter, Job job)
		throws Exception {

		StringBuilder sb = new StringBuilder();

		sb.append(job.getURL());
		sb.append("api/json?tree=builds[");
		sb.append(Build.TREE_PARAMETER);
		sb.append("]");

		JSONObject jobJson = jsonGetter.getJson(sb.toString());

		Collection<Build> builds = new HashSet<>();

		for (JSONObject buildJson : getBuildJsons(jobJson)) {
			builds.add(new Build(buildJson, job));
		}

		return builds;
	}

	public static Collection<JSONObject> getBuildJsons(JSONObject jobJson)
		throws Exception {

		Collection<JSONObject> buildJsons = new HashSet<>();

		JSONArray buildsJson = jobJson.getJSONArray("builds");

		for (int i = 0; i < buildsJson.length(); i++) {
			buildJsons.add(buildsJson.getJSONObject(i));
		}

		return buildJsons;
	}

}