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

/**
 * @author Kevin Yen
 */
public class JenkinsJobURLs {

	public static Set<String> getJenkinsJobURLs(int start, int end, Set<String> jobTypes, Set<String> branches, boolean remote) {
		Set<Integer> masters = getIntegerList(start, end);

		return getJenkinsJobURLs(masters, jobTypes, branches, remote);
	}

	public static Set<String> getJenkinsJobURLs(Set<Integer> masters, Set<String> jobTypes, Set<String> branches, boolean remote) {
		Set<String> jobNames = getJobNames(jobTypes, branches);

		return getJenkinsJobURLs(masters, jobNames, remote);
	}

	public static Set<String> getJenkinsJobURLs(int start, int end, Set<String> jobNames, boolean remote) {
		Set<Integer> masters = getIntegerList(start, end);

		return getJenkinsJobURLs(masters, jobNames, remote);
	}

	public static Set<Integer> getIntegerList(int start, int end) {
		Set<Integer> list = new HashSet<>();

		for (int i = start; i <= end; i++) {
			list.add(i);
		}

		return list;
	}

	public static Set<String> getJenkinsJobURLs(Set<Integer> masters, Set<String> jobNames, boolean remote) {
		Set<String> jenkinsJobURLs = new HashSet<>();

		for (String jobName : jobNames) {
			jenkinsJobURLs.addAll(getJenkinsJobURLs(masters, jobName, remote));
		}

		return jenkinsJobURLs;
	}

	public static Set<String> getJenkinsJobURLs(int start, int end, String jobName, boolean remote) {
		Set<Integer> masters = getIntegerList(start, end);

		return getJenkinsJobURLs(masters, jobName, remote);
	}

	public static Set<String> getJenkinsJobURLs(Set<Integer> masters, String jobName, boolean remote) {
		Set<String> jenkinsJobURLs = new HashSet<>();

		for (Integer master : masters) {
			StringBuilder sb = new StringBuilder();

			sb.append(getJenkinsURL(master, remote));
			sb.append("job/");
			sb.append(jobName);

			jenkinsJobURLs.add(sb.toString());
		}

		return jenkinsJobURLs;
	}

	public static Set<String> getJobNames(Set<String> jobTypes, Set<String> branches) {
		Set<String> jobNames = new HashSet<>();

		for (String jobType : jobTypes) {
			for (String branch : branches) {
				jobNames.add(getJobName(jobType, branch));
			}
		}

		return jobNames;
	}

	public static String getJobName(String jobType, String branch) {
		StringBuilder sb = new StringBuilder();

		sb.append(jobType);
		sb.append("(");
		sb.append(branch);
		sb.append(")");

		return sb.toString();
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
		StringBuilder sb = new StringBuilder();

		sb.append("http://test-1-");
		sb.append(master);
		sb.append("/");

		return sb.toString();
	}

	public static String getJenkinsRemoteURL(int master) {
		StringBuilder sb = new StringBuilder();

		sb.append("https://test-1-");
		sb.append(master);
		sb.append(".liferay.com/");

		return sb.toString();
	}

}