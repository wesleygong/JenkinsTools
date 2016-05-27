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
public class BuildingMatcherTest extends BuildMatcherTest {

	@Test
	public void testNotMatchesBuildingTrue() {
		BuildMatcher buildMatcher = new BuildingMatcher("true");

		assertFalse(buildMatcher.matches(build));
	}

	@Test
	public void testMatchesBuildingFalse() {
		BuildMatcher buildMatcher = new BuildingMatcher("false");

		assertTrue(buildMatcher.matches(build));
	}

	@Test
	public void testMatchesBuildingAny() {
		BuildMatcher buildMatcher = new BuildingMatcher("false");

		assertTrue(buildMatcher.matches(build));
	}

	@Test
	public void testConstructorValidArgumentAny() {
		new BuildingMatcher("any");
	}

	@Test
	public void testConstructorValidArgumentFalse() {
		new BuildingMatcher("false");
	}

	@Test
	public void testConstructorValidArgumentTrue() {
		new BuildingMatcher("true");
	}

	@Test (expected=IllegalArgumentException.class)
	public void testConstructorThrowsIllegalArgumentException() {
		new BuildingMatcher("DOES_NOT_EXIST");
	}

}