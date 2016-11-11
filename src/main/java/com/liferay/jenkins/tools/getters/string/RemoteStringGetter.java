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

import java.io.File;
import java.io.IOException;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kevin Yen
 */
public class RemoteStringGetter extends NetworkGetter implements StringGetter {

	private static final Logger logger = LoggerFactory.getLogger(
		RemoteStringGetter.class);

	private String username;
	private String password;

	public RemoteStringGetter(
			String username, String password, int timeout)
		throws IOException {

		super(timeout);

		this.username = username;
		this.password = password;
	}

	public RemoteStringGetter(
			String username, String password, int timeout, File aliasesFile)
		throws IOException {

		super(timeout, aliasesFile);

		this.username = username;
		this.password = password;
	}

	@Override
	public String getString(String url) throws Exception {
		url = convertURL(url);

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

		logger.debug("Successfully fetched {}", url);

		return IOUtils.toString(
			httpResponse.getEntity().getContent(), Charset.defaultCharset());
	}

}
