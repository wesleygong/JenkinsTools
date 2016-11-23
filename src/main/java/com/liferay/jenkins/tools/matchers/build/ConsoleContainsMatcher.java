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

import java.util.Arrays;
import java.util.List;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;

/**
 * @author Kevin Yen
 */
public class ConsoleContainsMatcher implements BuildMatcher {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(
		ConsoleContainsMatcher.class);

	private StringGetter stringGetter;
	private String match;

	public ConsoleContainsMatcher(StringGetter stringGetter, String match) {
		this.stringGetter = stringGetter;
		this.match = match;

		logger.debug("Matching console containing string {}", match);
	}

	@Override
	public boolean matches(Build jenkinsBuild) {
		try {
			String consoleText = stringGetter.getString(
				jenkinsBuild.getURL() + "/logText/progressiveText");

			if (consoleText.contains(match)) {
				return true;
			}

			return false;
		}
		catch (Exception e) {
			logger.warn(e.getMessage());

			return false;
		}
	}

}
