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

import java.io.Console;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * @author Kevin Yen
 */
public class JenkinsStatus {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(JenkinsStatus.class);

	private static final int THREAD_POOL_SIZE = 20;

	public static void main(String [] args) throws Exception {
		CommandLineParser parser = new DefaultParser();

		Options options = new Options();

		options.addOption("i", "info", false, "Set logging level to info.");
		options.addOption("d", "debug", false, "Set logging level to debug.");
		options.addOption("u", "user", true, "Specify the username used in authentication.");

		CommandLine line = parser.parse(options, args);

		if (line.hasOption("i")) {
			Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
			rootLogger.setLevel(Level.INFO);
		}
		if (line.hasOption("d")) {
			Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
			rootLogger.setLevel(Level.DEBUG);
		}

		JsonGetter jsonGetter = new LocalJsonGetter();

		if (line.hasOption("u")) {
			Console console = System.console();

			if (console == null) {
				logger.error("Unable to get Console instance.");

				System.exit(0);
			}

			String username = line.getOptionValue("u");
			String password = new String(console.readPassword("Enter password for " + username + " :"));

			jsonGetter = new RemoteJsonGetter(username, password);
		}

		Set<String> jenkinsURLs = new HashSet<>();

		for (int i = 1; i <= 20; i++) {
			String jenkinsURL = JenkinsJobURLs.getJenkinsURL(i, line.hasOption("u"));
			jenkinsURLs.add(jenkinsURL);

			logger.info("Adding {} to the list of servers to search.", jenkinsURL);
		}

		ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

		Set<JenkinsJob> jenkinsJobs = JenkinsJobsGetter.getJenkinsJobs(jsonGetter, executor, jenkinsURLs);

		Set<JenkinsBuild> jenkinsBuilds = JenkinsBuildsGetter.getJenkinsBuilds(jsonGetter, executor, jenkinsJobs);

		executor.shutdown();

		for (JenkinsBuild jenkinsBuild : jenkinsBuilds) {
			if (jenkinsBuild.isBuilding()) {
				System.out.println(jenkinsBuild.getURL());
			}
		}
	}

}