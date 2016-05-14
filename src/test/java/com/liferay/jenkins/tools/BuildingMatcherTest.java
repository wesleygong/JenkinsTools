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

/**
 * @author Kevin Yen
 */
public class BuildingMatcherTest {

	@Test
	public void ConstructorValidArgumentAny() {
		new BuildingMatcher("any");
	}

	@Test
	public void ConstructorValidArgumentFalse() {
		new BuildingMatcher("false");
	}

	@Test
	public void ConstructorValidArgumentTrue() {
		new BuildingMatcher("true");
	}

	@Test (expected=IllegalArgumentException.class)
	public void ConstructorThrowsIllegalArgumentException() {
		new BuildingMatcher("DOES_NOT_EXIST");
	}

}