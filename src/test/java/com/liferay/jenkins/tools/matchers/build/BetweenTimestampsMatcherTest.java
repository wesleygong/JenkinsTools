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

/**
 * @author Kevin Yen
 */
public class BetweenTimestampsMatcherTest {

	@Test
	public void ConstructorValidUUnixTimeArgument() {
		String[] timestamps = {"1460240504174", "1460260504174"};

		new BetweenTimestampsMatcher(timestamps);
	}

	@Test
	public void ConstructorValidDateTimeArgument() {
		String[] timestamps = {"04/08/2016 10:31 AM", "06/07/2018 9:10 PM"};

		new BetweenTimestampsMatcher(timestamps);
	}

	@Test
	public void ConstructorValidDateArgument() {
		String[] timestamps = {"12/09/2015", "05/14/2016"};

		new BetweenTimestampsMatcher(timestamps);
	}

	@Test
	public void ConstructorValidTimeArgument() {
		String[] timestamps = {"12:54 PM", "2:34 PM"};

		new BetweenTimestampsMatcher(timestamps);
	}

	@Test (expected=IllegalArgumentException.class)
	public void ConstructorThrowsIllegalArgumentException() {
		String[] timestamps = {"DOES_NOT_EXIST", "NEITHER_DO_THIS"};

		new BetweenTimestampsMatcher(timestamps);
	}

}