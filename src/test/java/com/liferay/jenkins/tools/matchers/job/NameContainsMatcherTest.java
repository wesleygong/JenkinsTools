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

import org.junit.Test;

import org.json.JSONObject;

/**
 * @author Kevin Yen
 */
public class NameContainsMatcherTest extends JobMatcherTest {

	@Test
	public void testEqualsName() {
		JobMatcher jobMatcher = new NameContainsMatcher("test-jenkins-job");

		assertTrue(jobMatcher.matches(job));
	}

	@Test
	public void testContainsName() {
		JobMatcher jobMatcher = new NameContainsMatcher("jenkins");

		assertTrue(jobMatcher.matches(job));
	}

	@Test
	public void testNotContainsName() {
		JobMatcher jobMatcher = new NameContainsMatcher("does-not-match");

		assertFalse(jobMatcher.matches(job));
	}

}