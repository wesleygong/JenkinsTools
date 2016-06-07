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
		String[] duration = {"500000"};

		BuildMatcher buildMatcher = new LessThanDurationMatcher(duration);

		assertFalse(buildMatcher.matches(build));
	}

	@Test
	public void testMatchesLessThanISO8601String() throws Exception {
		String[] duration = {"PT5M"};

		BuildMatcher buildMatcher = new LessThanDurationMatcher(duration);

		assertFalse(buildMatcher.matches(build));
	}

	@Test
	public void testMatchesLessThanTextString() throws Exception {
		String[] duration = {"3", "Minutes", "and", "13", "Seconds"};

		BuildMatcher buildMatcher = new LessThanDurationMatcher(duration);

		assertFalse(buildMatcher.matches(build));
	}

	@Test
	public void testNotMatchesGreaterThanNumericString() throws Exception {
		String[] duration = {"1500000"};

		BuildMatcher buildMatcher = new LessThanDurationMatcher(duration);

		assertTrue(buildMatcher.matches(build));
	}

	@Test
	public void testNotMatchesGreaterThanISO8601String() throws Exception {
		String[] duration = {"PT20M"};

		BuildMatcher buildMatcher = new LessThanDurationMatcher(duration);

		assertTrue(buildMatcher.matches(build));
	}

	@Test
	public void testNotMatchesGreaterThanTextString() throws Exception {
		String[] duration = {"25", "Minutes", "and", "53", "Seconds"};

		BuildMatcher buildMatcher = new LessThanDurationMatcher(duration);

		assertTrue(buildMatcher.matches(build));
	}

}