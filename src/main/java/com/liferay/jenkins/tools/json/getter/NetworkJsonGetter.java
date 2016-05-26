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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kevin Yen
 */
public abstract class NetworkJsonGetter implements JsonGetter {

	private static final Logger logger = LoggerFactory.getLogger(
		NetworkJsonGetter.class);

	protected Map<String, String> aliases;
	protected int timeout;

	protected Pattern whiteSpacePattern = Pattern.compile("\\s+");

	public NetworkJsonGetter(int timeout) {
		this.timeout = timeout;
		this.aliases = Collections.emptyMap();
	}

	public NetworkJsonGetter(int timeout, File aliasesFile) throws IOException {
		this.timeout = timeout;
		this.aliases = getAliases(aliasesFile);
	}

	public void loadAliases(File aliasFile) throws IOException {
		this.aliases = getAliases(aliasFile);
	}

	protected Map<String, String> getAliases(File aliasFile)
		throws IOException {

		BufferedReader bufferedReader = new BufferedReader(
			new FileReader(aliasFile));

		Map<String, String> aliases = new HashMap<>();

		String line = bufferedReader.readLine();

		while (line != null) {
			line = line.trim();

			if (!line.isEmpty() && (line.charAt(0) != '#')) {
				Scanner scanner = new Scanner(line);

				scanner.useDelimiter(whiteSpacePattern);

				try {
					String dest = scanner.next();
					String src = scanner.next();

					aliases.put(src, dest);

					logger.debug("Found alias " + src + " > " + dest);
				}
				catch (NoSuchElementException e) {
					logger.warn(
						"Encountered invalid line while reading " + aliasFile);
				}
				finally {
					scanner.close();
				}
			}

			line = bufferedReader.readLine();
		}

		return aliases;
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