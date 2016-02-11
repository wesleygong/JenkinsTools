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
public class LocalJsonGetterTest {

	@Test
	public void testConvertURL() {
		JsonGetter localJsonGetter = new LocalJsonGetter();

		String expectedURL = "http://test-14/14/";

		String remoteURL = "https://test.liferay.com/14/";

		String localURL = localJsonGetter.convertURL(remoteURL);

		assertEquals(localURL, expectedURL);

		expectedURL = "http://test-1-4/";

		remoteURL = "https://test-1-4.liferay.com/";

		localURL = localJsonGetter.convertURL(remoteURL);

		assertEquals(localURL, expectedURL);
	}

}