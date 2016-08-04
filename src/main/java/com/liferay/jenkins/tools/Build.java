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
public class Build {

	public static final String TREE_PARAMETER =
		"building,duration,number,result,timestamp,url," +
			"actions[parameters[name,value]]";

	private Map<String, String> attributes;

	private Boolean building;
	private Long duration;
	private Integer number;
	private Job job;
	private Long timestamp;
	private Map<String, Object> parameters;
	private String result;
	private String url;

	public Build(JSONObject buildJson, Job job) {
		this.job = job;

		attributes = new HashMap<String, String>();

		building = buildJson.getBoolean("building");
		attributes.put("Building", building.toString());

		duration = buildJson.optLong("duration");
		attributes.put("Duration", duration.toString());

		number = buildJson.getInt("number");
		attributes.put("Number", number.toString());

		result = buildJson.optString("result");
		attributes.put("Result", result);

		timestamp = buildJson.getLong("timestamp");
		attributes.put("Timestamp", timestamp.toString());

		url = buildJson.getString("url");
		attributes.put("URL", url);

		parameters = new HashMap<>();

		JSONArray actionsJson = buildJson.getJSONArray("actions");

		if ((actionsJson.length() > 0) &&
			actionsJson.getJSONObject(0).has("parameters")) {

			JSONArray parametersJson =
				actionsJson.getJSONObject(0).getJSONArray("parameters");

			for (int i = 0; i < parametersJson.length(); i++) {
				JSONObject parameterJson = parametersJson.getJSONObject(i);

				String parameterName = parameterJson.getString("name");

				if (parameterJson.has("value")) {
					Object parameterValue = parameterJson.get("value");

					parameters.put(parameterName, parameterValue);
				}
			}
		}
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public Job getJob() {
		return job;
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

	public Map<String, Object> getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(job.getName());
		sb.append("/");
		sb.append(number);
		sb.append("[");
		sb.append(url);
		sb.append("]");

		return sb.toString();
	}

}