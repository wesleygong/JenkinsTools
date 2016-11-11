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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.time.Duration;
import java.time.format.DateTimeParseException;

import java.util.*;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;

/**
 * @author Kevin Yen
 */
public abstract class DurationMatcher implements BuildMatcher {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(
		DurationMatcher.class);

	protected int duration;

	private List<String> secondsLabels = Arrays.asList(
		"S", "SEC", "SECOND", "SECONDS");
	private List<String> minutesLabels = Arrays.asList(
		"M", "MIN", "MINUTE", "MINUTES");
	private List<String> hoursLabels = Arrays.asList(
		"H", "HR", "HOUR", "HOURS");
	private List<String> daysLabels = Arrays.asList(
		"D", "DAY", "DAYS");

	private List<String> possibleSections = Arrays.asList("D", "H", "M", "S");
	private List<String> timeSections = Arrays.asList("H", "M", "S");

	public DurationMatcher(String[] duration) {
		this.duration = parseDuration(duration);
	}

	@Override
	public abstract boolean matches(Build jenkinsBuild);

	protected int parseDuration(String[] optionValues) throws IllegalArgumentException {
		String joinedString = StringUtils.join(optionValues, "");
		String spacedString = StringUtils.join(optionValues, " ");

		try {
			return Integer.parseInt(joinedString);
		}
		catch (NumberFormatException e) {
			logger.debug(
				"'{}' is not a numeric duration string", spacedString);
		}

		try {
			Duration durationObject = Duration.parse(joinedString);

			return (int) durationObject.toMillis();
		}
		catch (DateTimeParseException e) {
			logger.debug(
				"'{}' is not an ISO-8601 duration string", spacedString);
		}

		try {
			String textDurationString = parseTextDuration(optionValues);

			Duration durationObject = Duration.parse(textDurationString);

			return (int) durationObject.toMillis();
		}
		catch (DateTimeParseException e) {
			logger.debug(
				"'{}' is not a text duration string", spacedString);
		}

		throw new IllegalArgumentException(
			"Unable to parse duration string '" + spacedString + "'");
	}

	protected String parseTextDuration(String[] optionValues) {
		List<String> parsedStrings = new ArrayList<>();

		for (String optionValue : optionValues) {
			optionValue = optionValue.toUpperCase();

			if (NumberUtils.isDigits(optionValue)) {
				parsedStrings.add(optionValue);
			}
			else if (secondsLabels.contains(optionValue)) {
				parsedStrings.add("S");
			}
			else if (minutesLabels.contains(optionValue)) {
				parsedStrings.add("M");
			}
			else if (hoursLabels.contains(optionValue)) {
				parsedStrings.add("H");
			}
			else if (daysLabels.contains(optionValue)) {
				parsedStrings.add("D");
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append("P");

		boolean timeSectionSet = false;

		for (String section : possibleSections) {
			if (parsedStrings.contains(section)) {
				if (!timeSectionSet) {
					sb.append("T");

					timeSectionSet = true;
				}

				int index = parsedStrings.indexOf(section);

				sb.append(parsedStrings.get(index - 1));
				sb.append(parsedStrings.get(index));
			}
		}

		return sb.toString();
	}

}
