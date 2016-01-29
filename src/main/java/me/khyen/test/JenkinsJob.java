package me.khyen.jenkins;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;

import org.apache.commons.io.IOUtils;

import org.apache.commons.codec.binary.Base64;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * @author Kevin Yen
 */
public class JenkinsJob {

	public static List<String> getActiveBuildURLs(String jobURL)
		throws Exception {

		List<String> activePullRequestURLs = new ArrayList<String>();

		List<String> buildURLs = getBuildURLs(jobURL + "/api/json?pretty");

		for (String buildURL : buildURLs) {
			String buildJsonURL = buildURL + "api/json?pretty";

			JSONObject jsonObject = getJSONObject(buildJsonURL);

			boolean building = jsonObject.getBoolean("building");

			if (building == true) {
				activePullRequestURLs.add(buildURL);
			}
		}

		return activePullRequestURLs;
	}

	public static List<String> getBuildURLs(String url) throws Exception {
		JSONObject jsonObject = getJSONObject(url);

		return getBuildURLs(jsonObject);
	}

	public static List<String> getBuildURLs(JSONObject jsonObject) throws Exception {
		JSONArray jsonBuilds = jsonObject.getJSONArray("builds");

		List<String> buildURLs = new ArrayList<String>();

		for (int i = 0; i < jsonBuilds.length(); i++) {
			String remoteBuildURL = jsonBuilds.getJSONObject(i).getString("url");
			buildURLs.add(getLocalURL(remoteBuildURL));
		}

		return buildURLs;
	}

	public static JSONObject getJSONObject(String url) throws Exception {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = httpClient.execute(new HttpGet(url));
		int statusCode = httpResponse.getStatusLine().getStatusCode();

		String jsonString = IOUtils.toString(httpResponse.getEntity().getContent());
		return new JSONObject(jsonString);
	}

	public static String getLocalURL(String remoteURL) {
		Matcher matcher = remoteURLPattern1.matcher(remoteURL);

		if (matcher.find()) {
			StringBuilder sb = new StringBuilder();

			sb.append("http://test-");
			sb.append(matcher.group(1));
			sb.append("/");
			sb.append(matcher.group(1));
			sb.append("/");

			return remoteURL.replaceAll(matcher.group(0), sb.toString());
		}

		matcher = remoteURLPattern2.matcher(remoteURL);

		if (matcher.find()) {
			StringBuilder sb = new StringBuilder();

			sb.append("http://");
			sb.append(matcher.group(1));
			sb.append("/");

			return remoteURL.replaceAll(matcher.group(0), sb.toString());
		}

		return remoteURL;
	}

	private static final Pattern remoteURLPattern1 = Pattern.compile(
		"https://test.liferay.com/([0-9]+)/");
	private static final Pattern remoteURLPattern2 = Pattern.compile(
		"https://(test-[0-9]+-[0-9]+).liferay.com/");

}