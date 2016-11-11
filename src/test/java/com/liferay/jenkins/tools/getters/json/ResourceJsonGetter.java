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

import java.io.IOException;

import java.net.URL;

import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kevin Yen
 */
public class ResourceJsonGetter implements JsonGetter {

	private static final Logger logger = LoggerFactory.getLogger(ResourceJsonGetter.class);

	@Override
	public JSONObject getJson(String file) throws Exception {
		URL fileURL = ResourceJsonGetter.class.getResource(file);

		if (fileURL == null) {
			throw new IOException(file + " not found");
		}

		logger.debug("Retrieving json resource from {}", file);

		return new JSONObject(IOUtils.toString(fileURL.openStream(), Charset.defaultCharset()));
	}

}