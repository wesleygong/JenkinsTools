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

import java.io.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * @author Kevin Yen
 */
public class JenkinsToolsServlet extends HttpServlet {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(
		JenkinsToolsServlet.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		JenkinsStatus jenkinsStatus = new JenkinsStatus();

		String[] args = {
			"--name-regex",
			"test-\\w+-acceptance-pullrequest\\(.*\\)",
			"--building",
			"true"
		};

		Collection<Build> builds = new ArrayList<Build>();

		try {
			jenkinsStatus.processArgs(args);

			builds = jenkinsStatus.listBuilds();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		response.setContentType("text/html;charset=UTF-8");

		PrintWriter out = response.getWriter();

		try {
			out.println("<!DOCTYPE html>");
			out.println("<html><head>");
			out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
			out.println("<script src=\"http://www.kryogenix.org/code/browser/sorttable/sorttable.js\"></script>");	
			out.println("<title>Hello World</title></head>");
			out.println("<body>");
			out.println("<p>Found ");
			out.println(builds.size());
			out.println(" active pull requests</p>");
			out.println("<table border=\"1\" class=\"sorttable\">");

			out.println("<tr>");
			out.println("<th>Name</th>");
			out.println("<th>Build Number</th>");
			out.println("<th>Timestamp</th>");
			out.println("<th>Pull Request Receiver</th>");
			out.println("<th>Pull Request Sender</th>");
			out.println("<th>Pull Request Number</th>");
			out.println("</tr>");

			for (Build build : builds) {
				out.println("<tr>");

				out.println("<td>");
				out.println(build.getJob().getName());
				out.println("</td>");

				out.println("<td>");
				out.println(build.getNumber());
				out.println("</td>");

				out.println("<td>");
				out.println(build.getTimestamp());
				out.println("</td>");

				out.println("<td>");
				out.println(build.getParameters().get("GITHUB_RECEIVER_USERNAME"));
				out.println("</td>");

				out.println("<td>");
				out.println(build.getParameters().get("GITHUB_SENDER_USERNAME"));
				out.println("</td>");

				out.println("<td>");
				out.println(build.getParameters().get("GITHUB_PULL_REQUEST_NUMBER"));
				out.println("</td>");

				out.println("</tr>");
			}

			out.println("</table>");
			out.println("</body></html>");
		}
		finally {
			out.close();
		}
	}

}