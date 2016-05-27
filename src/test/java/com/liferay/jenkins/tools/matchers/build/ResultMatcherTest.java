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

import org.junit.*;

import org.json.JSONObject;

/**
 * @author Kevin Yen
 */
public class ResultMatcherTest {

	JsonGetter resourceJsonGetter = new ResourceJsonGetter();

	Build build;

	@Before
	public void setup() throws Exception {
		JSONObject jobJson = resourceJsonGetter.getJson("/json-samples/job.json");
		JSONObject buildJson = resourceJsonGetter.getJson("/json-samples/build.json");

		buildJson.put("result", "FAIULRE");

		Job job = new Job(jobJson);
		build = new Build(buildJson, job);
	}

	@Test
	public void testNotMatchesResultSuccess() {
		BuildMatcher buildMatcher = new ResultMatcher("success");

		assertFalse(buildMatcher.matches(build));
	}

	@Test
	public void testNotMatchesResultAborted() {
		BuildMatcher buildMatcher = new ResultMatcher("aborted");

		assertFalse(buildMatcher.matches(build));
	}

	@Test
	public void testMatchesResultFailure() {
		BuildMatcher buildMatcher = new ResultMatcher("failure");

		assertFalse(buildMatcher.matches(build));
	}

	@Test
	public void testConstructorSuccess() {
		new ResultMatcher("success");
	}

	@Test (expected=IllegalArgumentException.class)
	public void testConstructorThrowsIllegalArgumentException() {
		new ResultMatcher("DOES_NOT_EXIST");
	}

}