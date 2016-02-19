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

/**
 * @author Kevin Yen
 */
public class JenkinsProperties {

	public static List<String> getPullRequestJobURLs() {
		return getPullRequestJobURLs(false);
	}

	public static List<String> getPullRequestJobURLs(boolean remote) {
		List<String> pullRequestJobURLs = new ArrayList<>();

		for (String pullRequestJobType : getPullRequestJobTypes()) {
			pullRequestJobURLs.addAll(getJenkinsJobURLs(pullRequestJobType, remote));
		}

		return pullRequestJobURLs;
	}

	public static List<String> getPullRequestJobTypes() {
		List<String> pullRequestJobTypes = new ArrayList<>();

		pullRequestJobTypes.add("test-portal-acceptance-pullrequest");
		pullRequestJobTypes.add("test-plugins-acceptance-pullrequest");

		return pullRequestJobTypes;
	}

	public static List<String> getJenkinsJobURLs(String jobName) {
		return getJenkinsJobURLs(jobName, false);
	}

	public static List<String> getJenkinsJobURLs(String jobName, boolean remote) {
		List<String> jenkinsJobURLs = new ArrayList<>();

		for (int i = start; i <= end; i++) {
			jenkinsJobURLs.addAll(getJenkinsJobURLs(jobName, i, remote));
		}

		return jenkinsJobURLs;
	}

	public static List<String> getJenkinsJobURLs(String jobName, int master) {
		return getJenkinsJobURLs(jobName, master, false);
	}

	public static List<String> getJenkinsJobURLs(String jobName, int master, boolean remote) {
		List<String> jenkinsJobURLs = new ArrayList<>();

		String jenkinsURL = getJenkinsURL(master, remote);

		for (String jenkinsJobName : getJenkinsJobNames(jobName)) {
			StringBuilder sb = new StringBuilder();

			sb.append(jenkinsURL);
			sb.append("job/");
			sb.append(jenkinsJobName);

			jenkinsJobURLs.add(sb.toString());
		}

		return jenkinsJobURLs;
	}

	public static List<String> getJenkinsJobNames(String jobName) {
		List<String> jenkinsJobNames = new ArrayList<>();

		for (String branch : getBranches()) {
			StringBuilder sb = new StringBuilder();

			sb.append(jobName);
			sb.append("(");
			sb.append(branch);
			sb.append(")");

			jenkinsJobNames.add(sb.toString());
		}

		return jenkinsJobNames;
	}

	public static List<String> getJenkinsLocalURLs() {
		return getJenkinsURLs(false);
	}

	public static List<String> getJenkinsRemoteURLs() {
		return getJenkinsURLs(true);
	}

	public static List<String> getJenkinsURLs() {
		return getJenkinsLocalURLs();
	}

	public static List<String> getJenkinsURLs(boolean remote) {
		List<String> jenkinsURLs = new ArrayList<>();

		for (int i = start; i <= end; i++) {
			jenkinsURLs.add(getJenkinsURL(i, remote));
		}

		return jenkinsURLs;
	}

	public static String getJenkinsURL(int master) {
		return getJenkinsURL(master, false);
	}

	public static String getJenkinsURL(int master, boolean remote) {
		if (remote == true) {
			return getJenkinsRemoteURL(master);
		}
		else {
			return getJenkinsLocalURL(master);
		}
	}

	public static String getJenkinsLocalURL(int master) {
		return new String("http://test-1-" + master + "/");
	}

	public static String getJenkinsRemoteURL(int master) {
		return new String("https://test-1-" + master + ".liferay.com/");
	}

	public static List<String> getBranches() {
		List<String> branches = new ArrayList<>();

		branches.add("master");
		branches.add("ee-6.2.x");
		branches.add("ee-6.1.x");

		return branches;
	}

	private final static int start = 1;
	private final static int end = 20;

}