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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;

/**
 * @author Kevin Yen
 */
public class BuildTimestampMatcher implements BuildMatcher {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(
		BuildTimestampMatcher.class);

	Date end = new Date(Long.MAX_VALUE);
	Date start = new Date(Long.MIN_VALUE);

	public BuildTimestampMatcher(boolean before, long time) {
		this(before, new Date(time));
	}

	public BuildTimestampMatcher(boolean before, String timestamp)
		throws NumberFormatException {

		this(before, parseTimestamp(timestamp));
	}

	public BuildTimestampMatcher(boolean before, Date date) {
		if (before) {
			end = date;

			logger.debug("Matching builds before {}.", end);
		}
		else {
			start = date;

			logger.debug("Matching builds after {}.", start);
		}
	}

	private static Date parseTimestamp(String timestamp)
		throws NumberFormatException {

		return new Date(Long.parseLong(timestamp));
	}

	public boolean matches(JenkinsBuild jenkinsBuild) {
		Date date = new Date(jenkinsBuild.getTimestamp());

		if (date.after(start) && date.before(end)) {
			return true;
		}

		return false;
	}

}