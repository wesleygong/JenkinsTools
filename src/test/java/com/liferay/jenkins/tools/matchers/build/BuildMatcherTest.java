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

import org.junit.Before;

import org.json.JSONObject;

/**
 * @author Kevin Yen
 */
public abstract class BuildMatcherTest {

	private JsonGetter resourceJsonGetter = new ResourceJsonGetter();

	protected Build build;

	@Before
	public void setup() throws Exception {
		JSONObject jobJson = resourceJsonGetter.getJson("/json-samples/job.json");
		JSONObject buildJson = resourceJsonGetter.getJson("/json-samples/build.json");

		Job job = new Job(jobJson);
		build = new Build(buildJson, job);
	}

}