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

import java.util.Map;
import java.util.HashMap;

import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kevin Yen
 */
public abstract class NetworkJsonGetter implements JsonGetter {

	protected Map<String, String> aliases;
	protected int timeout;

	public NetworkJsonGetter(int timeout, Map<String, String> aliases) {
		this.timeout = timeout;
		this.aliases = aliases;
	}

	protected String convertURL(String url) {
		for (String match : aliases.keySet()) {
			if (url.contains(match)) {
				return url.replace(match, aliases.get(match));
			}
		}

		return url;
	}

	public abstract JSONObject getJson(String url) throws Exception;

}