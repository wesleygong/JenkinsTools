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

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.*;

/**
 * @author Kevin Yen
 */
public class JenkinsJobURLsTest {

	@Test
	public void testGetJobNames() {
		Set<String> expectedJenkinsJobNames = new HashSet<>();

		expectedJenkinsJobNames.add("test-portal-acceptance-pullrequest(master)");
		expectedJenkinsJobNames.add("test-portal-acceptance-pullrequest(ee-6.2.x)");
		expectedJenkinsJobNames.add("test-plugins-acceptance-pullrequest(master)");
		expectedJenkinsJobNames.add("test-plugins-acceptance-pullrequest(ee-6.2.x)");

		Set<String> jobTypes = new HashSet<>();

		jobTypes.add("test-portal-acceptance-pullrequest");
		jobTypes.add("test-plugins-acceptance-pullrequest");

		Set<String> branches = new HashSet<>();

		branches.add("master");
		branches.add("ee-6.2.x");

		Set<String> actualJenkinsJobNames = JenkinsJobURLs.getJobNames(jobTypes, branches);

		assertThat(actualJenkinsJobNames, is(expectedJenkinsJobNames));
	}

	@Test
	public void testGetJobName() {
		String expectedJenkinsURL = "test-portal-acceptance-pullrequest(master)";

		String actualJenkinsURL = JenkinsJobURLs.getJobName("test-portal-acceptance-pullrequest", "master");

		assertEquals(expectedJenkinsURL, actualJenkinsURL);

		expectedJenkinsURL = "test-plugins-acceptance-pullrequest(ee-6.2.x)";

		actualJenkinsURL = JenkinsJobURLs.getJobName("test-plugins-acceptance-pullrequest", "ee-6.2.x");

		assertEquals(expectedJenkinsURL, actualJenkinsURL);
	}

	@Test
	public void testGetJenkinsURL() {
		String expectedJenkinsURL = "http://test-1-1/";

		String actualJenkinsURL = JenkinsJobURLs.getJenkinsURL(1, false);

		assertEquals(expectedJenkinsURL, actualJenkinsURL);

		expectedJenkinsURL = "https://test-1-1.liferay.com/";

		actualJenkinsURL = JenkinsJobURLs.getJenkinsURL(1, true);

		assertEquals(expectedJenkinsURL, actualJenkinsURL);
	}

	@Test
	public void testGetJenkinsJobURLs() {
		Set<String> expectedJenkinsJobURLs = new HashSet<>();

		expectedJenkinsJobURLs.add("https://test-1-1.liferay.com/job/test-portal-acceptance-pullrequest(master)");
		expectedJenkinsJobURLs.add("https://test-1-1.liferay.com/job/test-portal-acceptance-pullrequest(ee-6.2.x)");
		expectedJenkinsJobURLs.add("https://test-1-1.liferay.com/job/test-plugins-acceptance-pullrequest(master)");
		expectedJenkinsJobURLs.add("https://test-1-1.liferay.com/job/test-plugins-acceptance-pullrequest(ee-6.2.x)");
		expectedJenkinsJobURLs.add("https://test-1-2.liferay.com/job/test-portal-acceptance-pullrequest(master)");
		expectedJenkinsJobURLs.add("https://test-1-2.liferay.com/job/test-portal-acceptance-pullrequest(ee-6.2.x)");
		expectedJenkinsJobURLs.add("https://test-1-2.liferay.com/job/test-plugins-acceptance-pullrequest(master)");
		expectedJenkinsJobURLs.add("https://test-1-2.liferay.com/job/test-plugins-acceptance-pullrequest(ee-6.2.x)");

		Set<String> jobTypes = new HashSet<>();

		jobTypes.add("test-portal-acceptance-pullrequest");
		jobTypes.add("test-plugins-acceptance-pullrequest");

		Set<String> branches = new HashSet<>();

		branches.add("master");
		branches.add("ee-6.2.x");

		Set<String> actualJenkinsJobURLs = JenkinsJobURLs.getJenkinsJobURLs(1, 2, jobTypes, branches, true);

		assertThat(actualJenkinsJobURLs, is(expectedJenkinsJobURLs));
	}

}