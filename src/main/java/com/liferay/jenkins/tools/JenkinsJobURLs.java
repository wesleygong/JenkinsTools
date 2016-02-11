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
public class JenkinsJobURLs {

	public static List<String> getJenkinsJobURLs(int start, int end, List<String> jobTypes, List<String> branches, boolean remote) {
		List<Integer> masters = getIntegerList(start, end);

		return getJenkinsJobURLs(masters, jobTypes, branches, remote);
	}

	public static List<String> getJenkinsJobURLs(List<Integer> masters, List<String> jobTypes, List<String> branches, boolean remote) {
		List<String> jenkinsJobNames = getJenkinsJobNames(jobTypes, branches);

		return getJenkinsJobURLs(masters, jenkinsJobNames, remote);
	}

	public static List<String> getJenkinsJobURLs(int start, int end, List<String> jobNames, boolean remote) {
		List<Integer> masters = getIntegerList(start, end);

		return getJenkinsJobURLs(masters, jobNames, remote);
	}

	public static List<Integer> getIntegerList(int start, int end) {
		List<Integer> list = new ArrayList<>();

		for (int i = start; i <= end; i++) {
			list.add(i);
		}

		return list;
	}

	public static List<String> getJenkinsJobURLs(List<Integer> masters, List<String> jobNames, boolean remote) {
		List<String> jenkinsJobURLs = new ArrayList<>();

		for (String jobName : jobNames) {
			jenkinsJobURLs.addAll(getJenkinsJobURLs(masters, jobName, remote));
		}

		return jenkinsJobURLs;
	}

	public static List<String> getJenkinsJobURLs(int start, int end, String jobName, boolean remote) {
		List<Integer> masters = getIntegerList(start, end);

		return getJenkinsJobURLs(masters, jobName, remote);
	}

	public static List<String> getJenkinsJobURLs(List<Integer> masters, String jobName, boolean remote) {
		List<String> jenkinsJobURLs = new ArrayList<>();

		for (Integer master : masters) {
			StringBuilder sb = new StringBuilder();

			sb.append(getJenkinsURL(master, remote));
			sb.append("job/");
			sb.append(jobName);

			jenkinsJobURLs.add(sb.toString());
		}

		return jenkinsJobURLs;
	}

	public static List<String> getJenkinsJobNames(List<String> jobTypes, List<String> branches) {
		List<String> jobNames = new ArrayList<>();

		for (String jobType : jobTypes) {
			for (String branch : branches) {
				jobNames.add(getJenkinsJobName(jobType, branch));
			}
		}

		return jobNames;
	}

	public static String getJenkinsJobName(String jobType, String branch) {
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