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
public class TimestampMatcher implements BuildMatcher {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(
		TimestampMatcher.class);

	Date end = new Date(Long.MAX_VALUE);
	Date start = new Date(Long.MIN_VALUE);

	public TimestampMatcher(boolean before, long time) {
		this(before, new Date(time));
	}

	public TimestampMatcher(boolean before, String timestamp)
		throws IllegalArgumentException {

		this(before, parseTimestamp(timestamp));
	}

	public TimestampMatcher(boolean before, Date date) {
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
		throws IllegalArgumentException {

		try {
			return new Date(Long.parseLong(timestamp));
		}
		catch (NumberFormatException e) {
			logger.debug("{} is not unix time", timestamp);
		}

		int[] styles = {
			DateFormat.SHORT, DateFormat.MEDIUM, DateFormat.LONG,
			DateFormat.FULL
		};

		for (int dateStyle : styles) {
			for (int timeStyle : styles) {
				try {
					DateFormat dateFormat =
						DateFormat.getDateTimeInstance(dateStyle, timeStyle);

					Date date = dateFormat.parse(timestamp);

					return date;
				}
				catch (ParseException e) {
					logger.debug(
						"{} does not match format {}, {}", timestamp,
							dateStyle, timeStyle);
				}
			}
		}

		for (int dateStyle : styles) {
			try {
				DateFormat dateFormat = DateFormat.getDateInstance(dateStyle);

				Date date = dateFormat.parse(timestamp);

				return date;
			}
			catch (ParseException e) {
				logger.debug(
					"{} does not match date format {}", timestamp, dateStyle);
			}
		}

		for (int timeStyle : styles) {
			try {
				DateFormat dateFormat = DateFormat.getTimeInstance(timeStyle);

				Date time = dateFormat.parse(timestamp);

				return combineDateTime(new Date(), time);
			}
			catch (ParseException e) {
				logger.debug(
					"{} does not match time format {}", timestamp, timeStyle);
			}
		}

		throw new IllegalArgumentException("Unable to parse timestamp");
	}

	private static Date combineDateTime(Date date, Date time) {
		Calendar dateCalendar = Calendar.getInstance();
		Calendar timeCalendar = Calendar.getInstance();

		dateCalendar.setTime(date);
		timeCalendar.setTime(time);

		timeCalendar.set(dateCalendar.get(Calendar.YEAR),
			dateCalendar.get(Calendar.MONTH), dateCalendar.get(Calendar.DATE));

		return timeCalendar.getTime();
	}

	public boolean matches(Build jenkinsBuild) {
		Date date = new Date(jenkinsBuild.getTimestamp());

		if (date.after(start) && date.before(end)) {
			return true;
		}

		return false;
	}

}