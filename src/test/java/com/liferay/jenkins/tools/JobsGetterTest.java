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

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import org.junit.*;

import org.json.JSONException;

/**
 * @author Kevin Yen
 */
public class JobsGetterTest {

	private static final String VALID_JENKINS_URL = "https://test-1-1.liferay.com";
	private static final String VALID_JOB_URL = "https://test-1-1.liferay.com/job/test-portal-acceptance-pullrequest(master)";

	private static final String INVALID_JENKINS_URL = "https://invalid.url.com";

	TestJsonGetter testJsonGetter = new TestJsonGetter();

	@Before
	public void setup() throws Exception {
		testJsonGetter.linkJsonFile("/json-samples/jobs.json", VALID_JENKINS_URL);
		testJsonGetter.linkJsonFile("/json-samples/builds.json", VALID_JOB_URL);
	}

	@Test
	public void testGetJobs() throws Exception {
		JobsGetter.getJobs(testJsonGetter, VALID_JENKINS_URL);
	}

	@Test (expected=ExecutionException.class)
	public void testGetJobsWithThreadsThrowsExecutionException() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(1);

		Set<String> jenkinsURLs = new HashSet<>();

		jenkinsURLs.add(INVALID_JENKINS_URL);

		JobsGetter.getJobs(testJsonGetter, executor, jenkinsURLs);
	}

	@Test (expected=JSONException.class)
	public void testGetJobsThrowsJSONException() throws Exception {
		JobsGetter.getJobs(testJsonGetter, VALID_JOB_URL);
	}

}