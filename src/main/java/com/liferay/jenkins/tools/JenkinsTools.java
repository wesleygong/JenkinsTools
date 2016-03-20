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

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * @author Kevin Yen
 */
public class JenkinsTools {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(JenkinsTools.class);

	private static final int THREAD_POOL_SIZE = 120;

	public static void main(String [] args) throws Exception {
		if (args.length < 1) {
			System.exit(0);
		}

		String command = args[0];

		if (command.equals("list")) {
			args = Arrays.copyOfRange(args, 1, args.length);
						
			JenkinsStatus.main(args);			
		}
		else {
			System.out.println("Available commands: list");
		}
	}

}