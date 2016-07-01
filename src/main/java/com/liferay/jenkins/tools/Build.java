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

import java.net.URL;
import java.net.MalformedURLException;

import java.util.Date;
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

	private boolean building;
	private long duration;
	private int number;
	private Job job;
	private Date timestamp;
	private Map<String, Object> parameters;
	private String result;
	private URL url;

	public Build(JSONObject buildJson, Job job) throws MalformedURLException {
		this.job = job;

		building = buildJson.getBoolean("building");
		duration = buildJson.optLong("duration");
		number = buildJson.getInt("number");
		result = buildJson.optString("result");
		timestamp = new Date(buildJson.getLong("timestamp"));
		url = new URL(buildJson.getString("url"));

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

	public Date getTimestamp() {
		return timestamp;
	}

	public boolean isBuilding() {
		return building;
	}

	public URL getURL() {
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
