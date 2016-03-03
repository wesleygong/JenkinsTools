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

import org.apache.commons.io.IOUtils;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;

import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kevin Yen
 */
public class RemoteJsonGetter implements JsonGetter {

	private static final Logger logger = LoggerFactory.getLogger(RemoteJsonGetter.class);

	private String username;
	private String password;

	private RemoteJsonGetter() {};

	public RemoteJsonGetter(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public String convertURL(String url) {
		Matcher matcher = localURLPattern1.matcher(url);

		if (matcher.find()) {
			StringBuilder sb = new StringBuilder();

			sb.append("https://test.liferay.com/");
			sb.append(matcher.group(1));
			sb.append("/");

			return url.replaceAll(matcher.group(0), sb.toString());
		}

		matcher = localURLPattern2.matcher(url);

		if (matcher.find()) {
			StringBuilder sb = new StringBuilder();

			sb.append("https://");
			sb.append(matcher.group(1));
			sb.append(".liferay.com/");

			return url.replaceAll(matcher.group(0), sb.toString());
		}

		return url;
	}

	@Override
	public JSONObject getJson(String url) throws Exception {
		logger.debug("Fetching JSON from {} ...", url);

		CredentialsProvider provider = new BasicCredentialsProvider();

		Credentials credentials = new UsernamePasswordCredentials(username, password);

		provider.setCredentials(AuthScope.ANY, credentials);

		HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

		HttpResponse httpResponse = httpClient.execute(new HttpGet(url));

		int statusCode = httpResponse.getStatusLine().getStatusCode();

		String jsonString = IOUtils.toString(httpResponse.getEntity().getContent());

		logger.debug("Successfully fetched {}.", url);

		return new JSONObject(jsonString);
	}

	private static final Pattern localURLPattern1 = Pattern.compile(
		"http://test-[0-9]+/([0-9]+)/?");
	private static final Pattern localURLPattern2 = Pattern.compile(
		"http://(test-[0-9]+-[0-9]+)/?");

}