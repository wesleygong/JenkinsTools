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
public class NameRegexMatcherTest extends JobMatcherTest {

	@Test
	public void testRegexEqualsWithAnchorsName() {
		JobMatcher jobMatcher = new NameRegexMatcher("^test-jenkins-job$");

		assertTrue(jobMatcher.matches(job));
	}

	@Test
	public void testRegexEqualsName() {
		JobMatcher jobMatcher = new NameRegexMatcher("test-jenkins-job");

		assertTrue(jobMatcher.matches(job));
	}

	@Test
	public void testRegexMatchName() {
		JobMatcher jobMatcher = new NameRegexMatcher("test-.*-job");

		assertTrue(jobMatcher.matches(job));
	}

	@Test
	public void testRegexNotMatchName() {
		JobMatcher jobMatcher = new NameRegexMatcher("does-not-match");

		assertFalse(jobMatcher.matches(job));
	}

}