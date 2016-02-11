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

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;

import org.json.JSONObject;

/**
 * @author Kevin Yen
 */
public class LocalJsonGetter implements JsonGetter {

	@Override
	public JSONObject getJson(String url) throws Exception {
		HttpClient httpClient = HttpClientBuilder.create().build();

		HttpResponse httpResponse = httpClient.execute(new HttpGet(url));

		int statusCode = httpResponse.getStatusLine().getStatusCode();

		String jsonString = IOUtils.toString(httpResponse.getEntity().getContent());

		return new JSONObject(jsonString);
	}

	public String convertURL(String url) {
		Matcher matcher = remoteURLPattern1.matcher(url);

		if (matcher.find()) {
			StringBuilder sb = new StringBuilder();

			sb.append("http://test-");
			sb.append(matcher.group(1));
			sb.append("/");
			sb.append(matcher.group(1));
			sb.append("/");

			return url.replaceAll(matcher.group(0), sb.toString());
		}

		matcher = remoteURLPattern2.matcher(url);

		if (matcher.find()) {
			StringBuilder sb = new StringBuilder();

			sb.append("http://");
			sb.append(matcher.group(1));
			sb.append("/");

			return url.replaceAll(matcher.group(0), sb.toString());
		}

		return url;
	}

	private static final Pattern remoteURLPattern1 = Pattern.compile(
		"https://test.liferay.com/([0-9]+)/?");
	private static final Pattern remoteURLPattern2 = Pattern.compile(
		"https://(test-[0-9]+-[0-9]+).liferay.com/?");

}