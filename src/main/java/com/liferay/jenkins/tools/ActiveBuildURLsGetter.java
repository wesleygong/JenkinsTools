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

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.Callable;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Kevin Yen
 */
public class ActiveBuildURLsGetter implements Callable<List<String>> {

	private JsonGetter jsonGetter;
	private String jobURL;

	public ActiveBuildURLsGetter(JsonGetter jsonGetter, String jobURL) {
		this.jsonGetter = jsonGetter;
		this.jobURL = jobURL;
	}

	@Override
	public List<String> call() throws Exception {
		return getActiveBuildURLs(jobURL);
	}

	public List<String> getActiveBuildURLs(String jobURL)
		throws Exception {

		List<String> activeBuildURLs = new ArrayList<>();

		JSONObject rootJson = jsonGetter.getJson(jobURL + "/api/json?tree=builds[building,url]");

		JSONArray buildsJson = rootJson.getJSONArray("builds");

		for (int i = 0; i < buildsJson.length(); i++) {
			JSONObject buildJson = buildsJson.getJSONObject(i);

			boolean building = buildJson.getBoolean("building");
			String buildURL = buildJson.getString("url");

			if (building == true) {
				activeBuildURLs.add(buildURL);
			}
		}

		return activeBuildURLs;
	}

}