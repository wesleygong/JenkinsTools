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

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Kevin Yen
 */
public class Job {

	public static final String TREE_PARAMETER =
		"name,url,actions[parameterDefinitions[name,type]]";

	private String name;
	private URL url;
	private Set<String> parameterDefinitions;

	public Job(String name, String url, Set<String> parameterDefinitions)
		throws MalformedURLException {

		this.name = name;
		this.url = new URL(url);
		this.parameterDefinitions = parameterDefinitions;
	}

	public Job(JSONObject jobJson)
		throws Exception {

		name = jobJson.getString("name");
		url = new URL(jobJson.getString("url"));

		parameterDefinitions = new HashSet<>();

		JSONArray actionsJson = jobJson.getJSONArray("actions");

		if ((actionsJson.length() > 0) &&
			actionsJson.getJSONObject(0).has("parameterDefinitions")) {

			JSONObject actionJson = actionsJson.getJSONObject(0);

			JSONArray parameterDefinitionsJson = actionJson.getJSONArray(
				"parameterDefinitions");

			for (int i = 0; i < parameterDefinitionsJson.length(); i++) {
				JSONObject parameterDefinitionJson =
					parameterDefinitionsJson.getJSONObject(i);

				if (parameterDefinitionJson.getString("type").equals(
					"StringParameterDefinition")) {

					parameterDefinitions.add(
						parameterDefinitionJson.getString("name"));
				}
			}
		}
	}

	public String getName() {
		return name;
	}

	public URL getURL() {
		return url;
	}

	public Set<String> getParameterDefinitions() {
		return parameterDefinitions;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(name);
		sb.append("[");
		sb.append(url);
		sb.append("]");

		return sb.toString();
	}

}
