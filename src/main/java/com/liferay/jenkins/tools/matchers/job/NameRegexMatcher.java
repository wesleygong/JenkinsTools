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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;

/**
 * @author Kevin Yen
 */
public class NameRegexMatcher implements JobMatcher {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(
		NameRegexMatcher.class);

	private Pattern pattern;

	public NameRegexMatcher(String regex) throws PatternSyntaxException {
		pattern = Pattern.compile(regex);

		logger.debug("Matching job names matching regular expression {}",
			regex);
	}

	@Override
	public boolean matches(Job jenkinsJob) {
		Matcher matcher = pattern.matcher(jenkinsJob.getName());

		if (matcher.find()) {
			return true;
		}

		return false;
	}

}
