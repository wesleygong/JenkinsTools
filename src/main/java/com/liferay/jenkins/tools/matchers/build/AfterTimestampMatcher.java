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

import java.util.Calendar;
import java.util.Date;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;

/**
 * @author Kevin Yen
 */
public class AfterTimestampMatcher extends TimestampMatcher {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(
		AfterTimestampMatcher.class);

	Date start = new Date(Long.MAX_VALUE);

	private void setTimestamp(Date date) {
		start = date;

		logger.debug("Matching builds after {}", start);
	}

	public AfterTimestampMatcher(String timestamp)
		throws IllegalArgumentException {

		setTimestamp(parseTimestamp(timestamp));
	}

	@Override
	public boolean matches(Build jenkinsBuild) {
		Date date = new Date(jenkinsBuild.getTimestamp());

		if (date.after(start)) {
			return true;
		}

		return false;
	}

}