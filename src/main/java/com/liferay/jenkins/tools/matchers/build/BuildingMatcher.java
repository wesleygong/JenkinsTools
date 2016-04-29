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
public class BuildingMatcher implements BuildMatcher {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(
		BuildingMatcher.class);

	private final List<String> validMatches = Arrays.asList(
		"TRUE", "FALSE", "ANY");

	private String building;

	public BuildingMatcher(String match) {
		if (!validMatches.contains(match.toUpperCase())) {
			throw new IllegalArgumentException(
				match + " is not a valid building state");
		}

		building = match.toUpperCase();

		logger.debug("Matching builds with a building state of {}", building);
	}

	public boolean matches(Build jenkinsBuild) {
		if (building.equals("ANY")) {
			return true;
		}
		else if (building.equals("TRUE") && jenkinsBuild.isBuilding()) {
			return true;
		}
		else if (building.equals("FALSE") && !jenkinsBuild.isBuilding()) {
			return true;
		}

		return false;
	}

}