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

import java.net.URL;

import java.util.HashMap;
import java.util.Map;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kevin Yen
 */
public class TestJsonGetter extends ResourceJsonGetter {

	private static final Logger logger = LoggerFactory.getLogger(TestJsonGetter.class);

	private Map<String, String> testJsons = new HashMap<>();

	public void linkJsonFile(String file, String url) throws Exception {
		URL fileURL = TestJsonGetter.class.getResource(file);

		if (fileURL == null) {
			throw new IOException(file + " not found");
		}

		url = formatURL(url);

		logger.info("Linking key {} with resource at {}", url, file);

		testJsons.put(url, file);
	}

	@Override
	public JSONObject getJson(String url) throws Exception {
		url = formatURL(url);

		String file = testJsons.get(url);

		if (file == null) {
			throw new IOException("Key " + url + " is unlinked");
		}

		return super.getJson(file);
	}

	private String formatURL(String url) {
		url = url.replaceFirst("api/json.*", "");
		url = url.replaceFirst("/+$", "");

		return url;
	}

}