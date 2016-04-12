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

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Kevin Yen
 */
public class JenkinsBuild {

	public static final String QUERY_PARAMETER =
		"tree=builds[building,duration,number,result,timestamp,url," +
			"actions[parameters[name,value]]]";

	private boolean building;
	private long duration;
	private int number;
	private JenkinsJob jenkinsJob;
	private long timestamp;
	private Map<String, String> parameters;
	private String result;
	private String url;

	public JenkinsBuild(JSONObject buildJson, JenkinsJob jenkinsJob) {
		this.jenkinsJob = jenkinsJob;

		building = buildJson.getBoolean("building");
		duration = buildJson.optLong("duration");
		number = buildJson.getInt("number");
		result = buildJson.optString("result");
		timestamp = buildJson.getLong("timestamp");
		url = buildJson.getString("url");

		parameters = new HashMap<>();

		JSONArray actionsJson = buildJson.getJSONArray("actions");

		if ((actionsJson.length() > 0) &&
			actionsJson.getJSONObject(0).has("parameters")) {

			JSONArray parametersJson =
				actionsJson.getJSONObject(0).getJSONArray("parameters");

			for (int i = 0; i < parametersJson.length(); i++) {
				JSONObject parameterJson = parametersJson.getJSONObject(i);

				String parameterName = parameterJson.getString("name");

				if (jenkinsJob.getParameterDefinitions().contains(
					parameterName)) {

					String parameterValue = parameterJson.getString("value");

					parameters.put(parameterName, parameterValue);
				}
			}
		}
	}

	public JenkinsJob getJenkinsJob() {
		return jenkinsJob;
	}

	public long getDuration() {
		return duration;
	}

	public int getNumber() {
		return number;
	}

	public String getResult() {
		return result;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public boolean isBuilding() {
		return building;
	}

	public String getURL() {
		return url;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(jenkinsJob.getName());
		sb.append("/");
		sb.append(number);
		sb.append("[");
		sb.append(url);
		sb.append("]");

		return sb.toString();
	}

}