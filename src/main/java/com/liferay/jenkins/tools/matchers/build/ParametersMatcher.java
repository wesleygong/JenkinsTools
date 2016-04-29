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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;

/**
 * @author Kevin Yen
 */
public class ParametersMatcher implements BuildMatcher {

	private Map<String, String> parameters = new HashMap<>();

	public ParametersMatcher(String[] matches) {
		for (String match : matches) {
			String[] parameterSet = match.split("\\s*=\\s*");

			if (parameterSet.length != 2) {
				throw new IllegalArgumentException("Invalid parameter format");
			}

			parameters.put(parameterSet[0], parameterSet[1]);
		}
	}

	public boolean matches(Build jenkinsBuild) {
		for (String name : parameters.keySet()) {
			Map<String, String> buildParameters = jenkinsBuild.getParameters();

			if (!buildParameters.keySet().contains(name)) {
				return false;
			}
			else {
				String value = parameters.get(name);

				if (!buildParameters.get(name).equals(value)) {
					return false;
				}
			}
		}

		return true;
	}

}