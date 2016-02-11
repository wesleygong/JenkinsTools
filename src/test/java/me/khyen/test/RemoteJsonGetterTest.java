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
public class RemoteJsonGetterTest {

	@Test
	public void testConvertURL() {
		JsonGetter remoteJsonGetter = new RemoteJsonGetter("test", "test");

		String expectedURL = "https://test.liferay.com/14/";

		String localURL = "http://test-14/14/";

		String remoteURL = remoteJsonGetter.convertURL(localURL);

		assertEquals(remoteURL, expectedURL);

		expectedURL = "https://test-1-4.liferay.com/";

		localURL = "http://test-1-4/";

		remoteURL = remoteJsonGetter.convertURL(localURL);

		assertEquals(remoteURL, expectedURL);
	}

}