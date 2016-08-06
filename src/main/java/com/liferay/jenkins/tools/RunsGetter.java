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
public class RunsGetter implements Callable<Collection<Run>> {

	private static final Logger logger = LoggerFactory.getLogger(
		RunsGetter.class);

	private JsonGetter jsonGetter;
	private Build build;

	public RunsGetter(JsonGetter jsonGetter, Build build) {
		this.jsonGetter = jsonGetter;
		this.build = build;
	}

	@Override
	public Collection<Run> call() throws Exception {
		return getRuns(jsonGetter, build);
	}

	public static Collection<Run> getRuns(
			JsonGetter jsonGetter, ExecutorService executor,
			Collection<Build> builds)
		throws Exception {

		return getRuns(jsonGetter, executor, builds, 60);
	}

	public static Collection<Run> getRuns(
			JsonGetter jsonGetter, ExecutorService executor,
			Collection<Build> builds, int timeout)
		throws Exception {

		CompletionService<Collection<Run>> completionService =
			new ExecutorCompletionService<Collection<Run>>(executor);

		Set<Future<Collection<Run>>> activeFutures = new HashSet<>();

		Collection<Run> runs = new HashSet<>();

		try {
			logger.info("Getting runs for {} builds", builds.size());

			for (Build build : builds) {
				activeFutures.add(
					completionService.submit(
						new RunsGetter(jsonGetter, build)));
			}

			while (activeFutures.size() > 0) {
				Future<Collection<Run>> completedFuture = completionService.poll(
					timeout, TimeUnit.SECONDS);

				if (completedFuture == null) {
					throw new TimeoutException("RunsGetter timed out");
				}

				activeFutures.remove(completedFuture);

				runs.addAll(completedFuture.get());

				logger.debug("{} threads still active", activeFutures.size());
			}
		}
		catch (ExecutionException e) {
			logger.error("Invoked thread threw an exception");
			logger.error("Cancelling remaining threads");

			for (Future<Collection<Run>> future : activeFutures) {
				future.cancel(true);
			}

			throw e;
		}

		return runs;
	}

	public static Collection<Run> getRuns(JsonGetter jsonGetter, Build build)
		throws Exception {

		StringBuilder sb = new StringBuilder();

		sb.append(build.getURL());
		sb.append("api/json?tree=runs[");
		sb.append(Run.TREE_PARAMETER);
		sb.append("]");

		JSONObject buildJson = jsonGetter.getJson(sb.toString());

		Collection<Run> runs = new HashSet<>();

		for (JSONObject runJson : getRunJsons(buildJson)) {
			runs.add(new Run(runJson, build));
		}

		return runs;
	}

	public static Collection<JSONObject> getRunJsons(JSONObject buildJson)
		throws Exception {

		Collection<JSONObject> runJsons = new HashSet<>();

		JSONArray runsJson = buildJson.getJSONArray("runs");

		for (int i = 0; i < runsJson.length(); i++) {
			runJsons.add(runsJson.getJSONObject(i));
		}

		return runJsons;
	}

}