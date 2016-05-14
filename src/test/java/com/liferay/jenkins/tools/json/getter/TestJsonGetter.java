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
public class TestJsonGetter implements JsonGetter {

	private static final Logger logger = LoggerFactory.getLogger(TestJsonGetter.class);

	private Map<String, JSONObject> testJsons = new HashMap<>();

	public void linkJsonFile(String file, String url) throws Exception {
		URL fileURL = TestJsonGetter.class.getResource(file);

		if (fileURL == null) {
			throw new IOException(file + " not found");
		}

		url = formatURL(url);

		logger.info("Linking resource at {} with url key {}", fileURL, url);

		JSONObject jsonObject = new JSONObject(IOUtils.toString(fileURL.openStream()));

		testJsons.put(url, jsonObject);
	}

	@Override
	public String convertURL(String url) {
		return url;
	}

	@Override
	public JSONObject getJson(String url) throws Exception {
		logger.info("Retrieving with url key {}", formatURL(url));

		JSONObject json = testJsons.get(formatURL(url));

		if (json == null) {
			logger.error("Resource with url key {} does not exist", formatURL(url));

			throw new IOException("Resource with url key " + formatURL(url) + " does not exist");
		}

		return testJsons.get(formatURL(url));
	}

	private String formatURL(String url) {
		url = url.replaceFirst("api/json.*", "");
		url = url.replaceFirst("/+$", "");

		return url;
	}

}