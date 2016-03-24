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

import java.util.Map;

/**
 * @author Kevin Yen
 */
public class JenkinsBuild {

	private boolean building;
	private int number;
	private JenkinsJob jenkinsJob;
	private long timestamp;
	private Map<String, String> parameters;
	private String result;
	private String url;

	public JenkinsBuild(
		JenkinsJob jenkinsJob, int number, boolean building, String url,
		Map<String, String> parameters, long timestamp, String result) {

		this.building = building;
		this.jenkinsJob = jenkinsJob;
		this.number = number;
		this.parameters = parameters;
		this.result = result;
		this.timestamp = timestamp;
		this.url = url;
	}

	public JenkinsJob getJenkinsJob() {
		return jenkinsJob;
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