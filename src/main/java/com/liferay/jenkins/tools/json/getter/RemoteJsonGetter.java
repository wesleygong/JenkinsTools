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
import org.apache.http.client.config.RequestConfig;
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

	private static final Logger logger = LoggerFactory.getLogger(
		RemoteJsonGetter.class);

	private String username;
	private String password;

	private int timeout;

	public RemoteJsonGetter(String username, String password) {
		this.username = username;
		this.password = password;
		timeout = 0;
	}

	public RemoteJsonGetter(String username, String password, int timeout) {
		this.username = username;
		this.password = password;
		this.timeout = timeout;
	}

	@Override
	public JSONObject getJson(String url) throws Exception {
		logger.debug("Fetching JSON from {}", url);

		CredentialsProvider provider = new BasicCredentialsProvider();

		Credentials credentials = new UsernamePasswordCredentials(
			username, password);

		provider.setCredentials(AuthScope.ANY, credentials);

		RequestConfig requestConfig =
			RequestConfig.custom().setConnectTimeout(timeout).build();

		HttpClient httpClient =
			HttpClientBuilder.create().setDefaultCredentialsProvider(
				provider).setDefaultRequestConfig(requestConfig).build();

		HttpResponse httpResponse = httpClient.execute(new HttpGet(url));

		int statusCode = httpResponse.getStatusLine().getStatusCode();

		String jsonString =
			IOUtils.toString(httpResponse.getEntity().getContent());

		logger.debug("Successfully fetched {}", url);

		return new JSONObject(jsonString);
	}

}