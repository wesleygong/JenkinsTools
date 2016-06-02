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

import java.time.Duration;
import java.time.format.DateTimeParseException;

import java.util.Arrays;
import java.util.List;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;

/**
 * @author Kevin Yen
 */
public abstract class DurationMatcher implements BuildMatcher {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(
		DurationMatcher.class);

	protected int duration;

	public DurationMatcher(int duration) {
		this.duration = duration;
	}

	@Override
	public abstract boolean matches(Build jenkinsBuild);

	protected int parseDuration(String duration) throws IllegalArgumentException {
		try {
			return Integer.parseInt(duration);
		}
		catch (NumberFormatException e) {
			logger.debug("{} is not a numeric duration string");
		}

		try {
			Duration durationObject = Duration.parse(duration);

			return (int) durationObject.toMillis();
		}
		catch (DateTimeParseException e) {
			logger.debug("{} is not a text duration string");
		}

		throw new IllegalArgumentException("Unable to parse duration");
	}

}