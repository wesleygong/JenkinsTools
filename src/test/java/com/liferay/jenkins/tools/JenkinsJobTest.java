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
import static org.hamcrest.CoreMatchers.*;

import org.junit.*;

/**
 * @author Kevin Yen
 */
public class JenkinsJobTest {

	@Test
	public void testGetToString() {
		String name = "test-portal-acceptance-pullrequest(master)";
		String url = "https://test-1-1.liferay.com/job/test-portal-acceptance-pullrequest(master)";

		String expectedString = "test-portal-acceptance-pullrequest(master)[https://test-1-1.liferay.com/job/test-portal-acceptance-pullrequest(master)]";

		JenkinsJob jenkinsJob = new JenkinsJob(name, url);

		assertEquals(jenkinsJob.toString(), expectedString);
	}

}