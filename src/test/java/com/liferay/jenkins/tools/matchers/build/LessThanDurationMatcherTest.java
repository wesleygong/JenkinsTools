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
public class LessThanDurationMatcherTest extends BuildMatcherTest {

	@Test
	public void testMatchesLessThanNumericString() throws Exception {
		BuildMatcher buildMatcher = new LessThanDurationMatcher("500000");

		assertFalse(buildMatcher.matches(build));
	}

	@Test
	public void testMatchesLessThanTextString() throws Exception {
		BuildMatcher buildMatcher = new LessThanDurationMatcher("PT5M");

		assertFalse(buildMatcher.matches(build));
	}

	@Test
	public void testNotMatchesGreaterThanNumericString() throws Exception {
		BuildMatcher buildMatcher = new LessThanDurationMatcher("1500000");

		assertTrue(buildMatcher.matches(build));
	}

	@Test
	public void testNotMatchesGreaterThanTextString() throws Exception {
		BuildMatcher buildMatcher = new LessThanDurationMatcher("PT20M");

		assertTrue(buildMatcher.matches(build));
	}

}