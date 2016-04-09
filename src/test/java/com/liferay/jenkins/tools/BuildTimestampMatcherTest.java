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
public class BuildTimestampMatcherTest {

	@Test
	public void ConstructorValidUUnixTimeArgument() {
		new BuildTimestampMatcher(true, "1460240504174");
	}

	@Test
	public void ConstructorValidDateTimeArgument() {
		new BuildTimestampMatcher(true, "04/08/2016 10:31 AM");
	}

	@Test
	public void ConstructorValidDateArgument() {
		new BuildTimestampMatcher(true, "12/09/2015");
	}

	@Test
	public void ConstructorValidTimeArgument() {
		new BuildTimestampMatcher(true, "12:54 PM");
	}

	@Test (expected=IllegalArgumentException.class)
	public void ConstructorThrowsIllegalArgumentException() {
		new BuildTimestampMatcher(true, "DOES_NOT_EXIST");
	}

}