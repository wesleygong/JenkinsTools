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
public class ParametersMatcherTest extends BuildMatcherTest {

	@Test
	public void testMatchesParametersStrings() {
		String[] parameters = {"hello=world", "foo=bar"};

		BuildMatcher buildMatcher = new ParametersMatcher(parameters);

		assertTrue(buildMatcher.matches(build));
	}

	@Test
	public void testMatchesParametersInteger() {
		String[] parameters = {"some=24"};

		BuildMatcher buildMatcher = new ParametersMatcher(parameters);

		assertTrue(buildMatcher.matches(build));
	}

	@Test
	public void testMatchesParametersBoolean() {
		String[] parameters = {"condition=true"};

		BuildMatcher buildMatcher = new ParametersMatcher(parameters);

		assertTrue(buildMatcher.matches(build));
	}

	@Test
	public void testNotMatchesParametersNameNotExist() {
		String[] parameters = {"this=doesnotexist"};

		BuildMatcher buildMatcher = new ParametersMatcher(parameters);

		assertFalse(buildMatcher.matches(build));
	}

	@Test
	public void testNotMatchesParametersValueNotSameString() {
		String[] parameters = {"hello=what"};

		BuildMatcher buildMatcher = new ParametersMatcher(parameters);

		assertFalse(buildMatcher.matches(build));
	}

	@Test
	public void testNotMatchesParametersValueNotSameBoolean() {
		String[] parameters = {"condition=false"};

		BuildMatcher buildMatcher = new ParametersMatcher(parameters);

		assertFalse(buildMatcher.matches(build));
	}

}