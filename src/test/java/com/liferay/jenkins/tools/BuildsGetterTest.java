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

import java.io.IOException;

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
public class BuildsGetterTest {

	private static final String VALID_JENKINS_URL = "https://test-1-1.liferay.com";
	private static final String VALID_JOB_NAME = "test-portal-acceptance-pullrequest(master)";
	private static final String VALID_JOB_URL = "https://test-1-1.liferay.com/job/test-portal-acceptance-pullrequest(master)";

	private static final String INVALID_JENKINS_URL = "https://invalid.url.com";
	private static final String INVALID_JOB_NAME = "this-does-not-exist";
	private static final String INVALID_JOB_URL = "https://test-1-1.liferay.com/job/this-does-not-exist";

	private Set<String> validParameterDefinitions;
	private Set<String> emptyParameterDefinitions;

	TestJsonGetter testJsonGetter = new TestJsonGetter();

	@Before
	public void setup() throws Exception {
		validParameterDefinitions = new HashSet<>();

		validParameterDefinitions.add("GITHUB_PULL_REQUEST_NUMBER");
		validParameterDefinitions.add("GITHUB_PULL_REQUEST_SHA");
		validParameterDefinitions.add("GITHUB_RECEIVER_USERNAME");
		validParameterDefinitions.add("GITHUB_SENDER_BRANCH_NAME");
		validParameterDefinitions.add("GITHUB_SENDER_BRANCH_SHA");
		validParameterDefinitions.add("GITHUB_SENDER_USERNAME");
		validParameterDefinitions.add("GITHUB_UPSTREAM_BRANCH_NAME");
		validParameterDefinitions.add("GITHUB_UPSTREAM_BRANCH_SHA");
		validParameterDefinitions.add("GITHUB_USERNAME");
		validParameterDefinitions.add("JENKINS_GITHUB_BRANCH_NAME");
		validParameterDefinitions.add("JENKINS_GITHUB_BRANCH_USERNAME");
		validParameterDefinitions.add("JENKINS_GITHUB_PULL_REQUEST_NUMBER");
		validParameterDefinitions.add("JENKINS_GITHUB_PULL_REQUEST_USERNAME");
		validParameterDefinitions.add("JENKINS_JOB_VARIANT");
		validParameterDefinitions.add("JENKINS_TOP_LEVEL_SHARED_DIR");

		emptyParameterDefinitions = new HashSet<>();

		testJsonGetter.linkJsonFile("/json-samples/jobs.json", VALID_JENKINS_URL);
		testJsonGetter.linkJsonFile("/json-samples/builds.json", VALID_JOB_URL);
	}

	@Test
	public void testGetBuilds() throws Exception {
		Job jenkinsJob = new Job(VALID_JOB_NAME, VALID_JOB_URL, validParameterDefinitions);

		BuildsGetter.getBuilds(testJsonGetter, jenkinsJob);
	}

	@Test (expected=IOException.class)
	public void testGetJobsThrowsException() throws Exception {
		Job jenkinsJob = new Job(VALID_JOB_NAME, INVALID_JOB_URL, validParameterDefinitions);

		BuildsGetter.getBuilds(testJsonGetter, jenkinsJob);
	}

	@Test (expected=ExecutionException.class)
	public void testGetBuildWithThreadsThrowsExecutionException() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(1);

		Set<Job> jenkinsJob = new HashSet<>();

		jenkinsJob.add(new Job(VALID_JOB_NAME, INVALID_JOB_URL, validParameterDefinitions));

		BuildsGetter.getBuilds(testJsonGetter, executor, jenkinsJob);
	}

}