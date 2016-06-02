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
public class GreaterThanDurationMatcherTest extends BuildMatcherTest {

	@Test
	public void testNotMatchesLessThanNumericString() throws Exception {
		BuildMatcher buildMatcher = new GreaterThanDurationMatcher("1500000");

		assertFalse(buildMatcher.matches(build));
	}

	@Test
	public void testNotMatchesLessThanTextString() throws Exception {
		BuildMatcher buildMatcher = new GreaterThanDurationMatcher("PT20M");

		assertFalse(buildMatcher.matches(build));
	}

	@Test
	public void testMatchesGreaterThanNumericString() throws Exception {
		BuildMatcher buildMatcher = new GreaterThanDurationMatcher("500000");

		assertTrue(buildMatcher.matches(build));
	}

	@Test
	public void testMatchesGreaterThanTextString() throws Exception {
		BuildMatcher buildMatcher = new GreaterThanDurationMatcher("PT5M");

		assertTrue(buildMatcher.matches(build));
	}

}