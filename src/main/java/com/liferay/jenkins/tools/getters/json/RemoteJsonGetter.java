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

import java.io.File;
import java.io.IOException;

import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kevin Yen
 */
public class RemoteJsonGetter extends RemoteStringGetter implements JsonGetter {

	private static final Logger logger = LoggerFactory.getLogger(
		RemoteJsonGetter.class);

	private String username;
	private String password;

	public RemoteJsonGetter(
			String username, String password, int timeout)
		throws IOException {

		super(username, password, timeout);
	}

	public RemoteJsonGetter(
			String username, String password, int timeout, File aliasesFile)
		throws IOException {

		super(username, password, timeout, aliasesFile);
	}

	@Override
	public JSONObject getJson(String url) throws Exception {
		return new JSONObject(getString(url));
	}

}
