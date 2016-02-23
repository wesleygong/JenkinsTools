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

	public static Set<JenkinsJob> getJenkinsJobs(JsonGetter jsonGetter, String jenkinsURL)
		throws Exception {

		Set<JenkinsJob> jenkinsJobs = new HashSet<>();

		JSONObject rootJson = jsonGetter.getJson(jenkinsURL + "api/json?tree=jobs[name,url]");

		JSONArray jobsJson = rootJson.getJSONArray("jobs");

		for (int i = 0; i < jobsJson.length(); i++) {
			JSONObject jobJson = jobsJson.getJSONObject(i);

			String name = jobJson.getString("name");
			String url = jobJson.getString("url");

			jenkinsJobs.add(new JenkinsJob(name, url));
		}

		return jenkinsJobs;
	}

}