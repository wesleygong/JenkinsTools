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
import java.io.File;

import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import java.util.regex.PatternSyntaxException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;

import org.apache.commons.lang.StringUtils;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * @author Kevin Yen
 */
public class JenkinsStatus {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(
		JenkinsStatus.class);

	private static final int THREAD_POOL_SIZE = 120;

	private static final int REQUEST_TIMEOUT = 30 * 1000; // miliseconds

	private static final int WAIT_TIMEOUT = 300; // seconds

	private File aliasesFile = new File("servers.aliases");

	private File serversListFile = new File("servers.list");

	private JsonGetter jsonGetter = new LocalJsonGetter(REQUEST_TIMEOUT);

	private boolean showBuildInfo = false;

	private boolean listJobs = false;

	private List<BuildMatcher> buildMatchers = new ArrayList<>();

	private List<JobMatcher> jobMatchers = new ArrayList<>();

	private void printMessage(String tag, String value, int maxTagLength) {
		StringBuilder sb = new StringBuilder();

		sb.append("  ");
		sb.append(tag);
		sb.append(':');

		int padding = maxTagLength - tag.length();

		for (int i = 0; i < padding; i++) {
			sb.append(' ');
		}

		sb.append(value);

		System.out.println(sb.toString());
	}

	private void printMessage(String tag, String[] values) {
		StringBuilder sb = new StringBuilder();

		sb.append("  ");
		sb.append(tag);
		sb.append(':');
		sb.append('\n');

		for (String value : values) {
			sb.append("    ");
			sb.append(value);
			sb.append('\n');
		}

		System.out.println(sb.toString());
	}

	private void printMessage(String tag, Map<String, Object> values) {
		StringBuilder sb = new StringBuilder();

		sb.append("  ");
		sb.append(tag);
		sb.append(':');
		sb.append('\n');

		for (String name : values.keySet()) {
			Object valueObject = values.get(name);

			String value = valueObject.toString();

			if ((value != null) && !value.isEmpty()) {
				sb.append("    ");
				sb.append(name);
				sb.append('=');
				sb.append(value);
				sb.append('\n');
			}
		}

		System.out.println(sb.toString());
	}

	private void processArgs(String[] args) throws Exception {
		CommandLineParser parser = new DefaultParser();

		Options options = new Options();

		options.addOption("c", "building", true, "Filter build by build state");
		options.addOption("d", "debug", false, "Set logging level to debug");
		options.addOption("f", "file", true, "Path to Jenkins servers list");
		options.addOption("h", "aliases", true, "Path to aliases file");
		options.addOption("i", "info", false, "Set logging level to info");
		options.addOption("n", "name", true, "Filter job by exact name");
		options.addOption("r", "result", true, "Filter build by result");
		options.addOption("u", "user", true, "Username used in authentication");

		options.addOption(
			Option.builder()
			.longOpt("list-jobs")
			.desc("List matching jobs")
			.build());
		options.addOption(
			Option.builder()
			.longOpt("name-contains")
			.hasArg()
			.desc("Filter job by name containing specified string")
			.build());
		options.addOption(
			Option.builder()
			.longOpt("name-regex")
			.hasArg()
			.desc("Filter job by name matching specified regular expression")
			.build());
		options.addOption(
			Option.builder("p")
			.longOpt("parameters")
			.hasArgs()
			.desc("Filter build by parameters")
			.valueSeparator(',')
			.build());
		options.addOption(
			Option.builder("b")
			.longOpt("before")
			.hasArgs()
			.desc("Filter build before specified time")
			.build());
		options.addOption(
			Option.builder("a")
			.longOpt("after")
			.hasArgs()
			.desc("Filter builds after specified time")
			.build());
		options.addOption(
			Option.builder("s")
			.longOpt("between")
			.hasArgs()
			.desc("Filter builds between two specified UNIX timestamps")
			.build());
		options.addOption(
			Option.builder("e")
			.longOpt("equals")
			.hasArgs()
			.desc("Filter builds with specific duration")
			.build());
		options.addOption(
			Option.builder("l")
			.longOpt("less")
			.hasArgs()
			.desc("Filter builds less than the duration")
			.build());
		options.addOption(
			Option.builder("g")
			.longOpt("greater")
			.hasArgs()
			.desc("Filter builds greater than the duration")
			.build());

		CommandLine line = parser.parse(options, args);

		if (line.hasOption("list-jobs")) {
			listJobs = true;
		}

		if (line.hasOption("info")) {
			Logger rootLogger = (Logger) LoggerFactory.getLogger(
				Logger.ROOT_LOGGER_NAME);
			rootLogger.setLevel(Level.INFO);

			showBuildInfo = true;
		}

		if (line.hasOption("debug")) {
			Logger rootLogger = (Logger) LoggerFactory.getLogger(
				Logger.ROOT_LOGGER_NAME);
			rootLogger.setLevel(Level.DEBUG);

			showBuildInfo = true;
		}

		if (line.hasOption("file")) {
			logger.debug("Loading file {}", line.getOptionValue("f"));

			serversListFile = new File(line.getOptionValue("f"));
		}

		if (line.hasOption("aliases")) {
			logger.debug("Loading file {}", line.getOptionValue("h"));

			aliasesFile = new File(line.getOptionValue("h"));
		}

		if (line.hasOption("user")) {
			Console console = System.console();

			if (console == null) {
				throw new IllegalStateException(
					"Unable to get Console instance");
			}

			String username = line.getOptionValue("u");
			String password = new String(
				console.readPassword("Enter password for " + username + " :"));

			if (aliasesFile.isFile()) {
				jsonGetter = new RemoteJsonGetter(
					username, password, REQUEST_TIMEOUT, aliasesFile);
			}
			else {
				jsonGetter = new RemoteJsonGetter(
					username, password, REQUEST_TIMEOUT);
			}
		}
		else if (aliasesFile.isFile()) {
				jsonGetter = new LocalJsonGetter(REQUEST_TIMEOUT, aliasesFile);
		}

		if (line.hasOption("name")) {
			jobMatchers.add(new NameEqualsMatcher(line.getOptionValue("name")));
		}

		if (line.hasOption("name-contains")) {
			jobMatchers.add(new NameContainsMatcher(line.getOptionValue("name-contains")));
		}

		if (line.hasOption("name-regex")) {
			jobMatchers.add(new NameRegexMatcher(line.getOptionValue("name-regex")));
		}

		if (line.hasOption("building")) {
			buildMatchers.add(new BuildingMatcher(line.getOptionValue("c")));
		}

		if (line.hasOption("parameters")) {
			buildMatchers.add(new ParametersMatcher(line.getOptionValues("p")));
		}

		if (line.hasOption("result")) {
			buildMatchers.add(new ResultMatcher(line.getOptionValue("r")));
		}

		if (line.hasOption("before")) {
			buildMatchers.add(new BeforeTimestampMatcher(line.getOptionValues(
				"b")));
		}

		if (line.hasOption("after")) {
			buildMatchers.add(new AfterTimestampMatcher(line.getOptionValues(
				"a")));
		}

		if (line.hasOption("between")) {
			buildMatchers.add(new BetweenTimestampsMatcher(line.getOptionValues(
				"s")));
		}

		if (line.hasOption("greater")) {
			buildMatchers.add(new GreaterThanDurationMatcher(
				line.getOptionValues("g")));
		}

		else if (line.hasOption("less")) {
			buildMatchers.add(new LessThanDurationMatcher(
				line.getOptionValues("l")));
		}
	}

	protected Collection<Job> getMatchingJobs(
			Collection<String> jenkinsURLs, ExecutorService executor)
		throws Exception {

		Collection<Job> matchingJobs = new HashSet<>();

		Collection<Job> jobs = JobsGetter.getJobs(
			jsonGetter, executor, jenkinsURLs, WAIT_TIMEOUT);

		for (Job job : jobs) {
			boolean match = true;

			for (JobMatcher jobMatcher : jobMatchers) {
				if (!jobMatcher.matches(job)) {
					match = false;
				}
			}

			if (match) {
				matchingJobs.add(job);
			}
		}

		return matchingJobs;
	}

	protected Collection<Build> getMatchingBuilds(
			Collection<Job> matchingJobs, ExecutorService executor)
		throws Exception {

		Collection<Build> builds = BuildsGetter.getBuilds(
			jsonGetter, executor, matchingJobs, WAIT_TIMEOUT);

		return getMatchingBuilds(builds, buildMatchers);
	}

	protected Collection<Build> getMatchingBuilds(
			Collection<Build> builds, Collection<BuildMatcher> buildMatchers)
		throws Exception {

		Collection<Build> startingBuilds = builds;
		Set<Build> matchingBuilds = new HashSet<>();

		for (BuildMatcher buildMatcher : buildMatchers) {
			matchingBuilds.clear();

			for (Build build : startingBuilds) {
				if (buildMatcher.matches(build)) {
					matchingBuilds.add(build);
				}
			}

			startingBuilds = matchingBuilds;
		}

		return matchingBuilds;
	}

	public void listBuilds() throws Exception {
		Collection<String> jenkinsURLs = new HashSet<>();

		logger.debug("Loading Jenkins URLs from {}", serversListFile);

		for (URL jenkinsURL : JenkinsURLs.getJenkinsURLs(serversListFile)) {
			jenkinsURLs.add(jenkinsURL.toString());

			logger.debug(
				"Adding {} to the list of servers to search",
					jenkinsURL.toString());
		}

		ExecutorService executor = Executors.newFixedThreadPool(
			THREAD_POOL_SIZE);

		Collection<Build> matchingBuilds = new HashSet<>();
		Collection<Job> matchingJobs = new HashSet<>();

		try {
			matchingJobs = getMatchingJobs(jenkinsURLs, executor);
			matchingBuilds = getMatchingBuilds(matchingJobs, executor);
		}
		catch (Exception e) {
			e.printStackTrace();

			throw e;
		}
		finally {
			if (listJobs) {
				System.out.println("Found " + matchingJobs.size() + " jobs");

				for (Job job : matchingJobs) {
					System.out.println(job.getURL());
				}
			}

			System.out.println("Found " + matchingBuilds.size() + " builds");

			for (Build build : matchingBuilds){
				if (showBuildInfo) {
					String date = new Date(build.getTimestamp()).toString();
					String number = Integer.toString(build.getNumber());
					String building = Boolean.toString(build.isBuilding());
					String duration = convertDurationToString(
						build.getDuration());

					System.out.print("\n");
					System.out.println(build.getURL());

					printMessage("Name", build.getJob().getName(), 12);
					printMessage("Number", number, 12);
					printMessage("Time", date, 12);
					printMessage("Building", building, 12);
					printMessage("Duration", duration, 12);
					printMessage("Result", build.getResult(), 12);
					printMessage("Parameters", build.getParameters());
				}
				else {
					System.out.println(build.getURL());
				}
			}

			executor.shutdown();
		}
	}

	private String convertDurationToString(long duration) {
		if (duration > 0) {
			StringBuilder sb = new StringBuilder();

			long hours = duration / 1000 / 60 / 60;

			if (hours > 0) {
				sb.append(hours);
				sb.append(" Hours ");
			}

			long minutes = duration / 1000 / 60 % 60;

			if (minutes > 0) {
				sb.append(minutes);
				sb.append(" Minutes ");
			}

			long seconds = duration / 1000 % 60;

			if (seconds > 0) {
				sb.append(seconds);
				sb.append(" Seconds ");
			}

			return sb.toString();
		}
		else {
			return "";
		}
	}

	public static void main(String [] args) throws Exception {
		JenkinsStatus jenkinsStatus = new JenkinsStatus();

		jenkinsStatus.processArgs(args);

		jenkinsStatus.listBuilds();
	}

}
