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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kevin Yen
 */
public class JenkinsURLs {

	private static final Logger logger = LoggerFactory.getLogger(JenkinsURLs.class);

	public static List<URL> getJenkinsURLs(File file) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

		List<URL> jenkinsURLs = new ArrayList<>();

		String line = bufferedReader.readLine();

		while (line != null) {
			logger.debug("{}", line);

			jenkinsURLs.add(new URL(line));

			line = bufferedReader.readLine();
		}

		return jenkinsURLs;
	}

	public static List<URL> getJenkinsURLs(String parameter) throws IOException {
		List<URL> jenkinsURLs = new ArrayList<>();

		for (String jenkinsURL : parameter.split(",")) {
			jenkinsURLs.add(new URL(jenkinsURL));
		}

		return jenkinsURLs;
	}

}