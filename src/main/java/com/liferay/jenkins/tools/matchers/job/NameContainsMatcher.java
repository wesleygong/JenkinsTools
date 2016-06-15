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

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;

/**
 * @author Kevin Yen
 */
public class NameContainsMatcher implements JobMatcher {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(
		NameContainsMatcher.class);

	private String match;

	public NameContainsMatcher(String match) {
		this.match = match;

		logger.debug("Matching job names containing {}", match);
	}

	@Override
	public boolean matches(Job jenkinsJob) {
		String name = jenkinsJob.getName();

		if (name.contains(match)) {
			return true;
		}

		return false;
	}

}