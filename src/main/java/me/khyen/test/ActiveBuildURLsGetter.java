package me.khyen.jenkins;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.concurrent.Callable;

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

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Kevin Yen
 */
public class ActiveBuildURLsGetter implements Callable<List<String>> {

	private JsonGetter jsonGetter;
	private String jobURL;

	public ActiveBuildURLsGetter(JsonGetter jsonGetter, String jobURL) {
		this.jsonGetter = jsonGetter;
		this.jobURL = jobURL;
	}

	@Override
	public List<String> call() throws Exception {
		return getActiveBuildURLs(jobURL);
	}

	public List<String> getActiveBuildURLs(String jobURL)
		throws Exception {

		List<String> activeBuildURLs = new ArrayList<String>();

		List<String> buildURLs = getBuildURLs(jobURL + "/api/json?pretty");

		for (String buildURL : buildURLs) {
			String buildJsonURL = buildURL + "api/json?pretty";

			JSONObject buildJson = jsonGetter.getJson(buildJsonURL);

			if (isBuildActive(buildJson)) {
				activeBuildURLs.add(buildURL);
			}
		}

		return activeBuildURLs;
	}

	public boolean isBuildActive(JSONObject buildJson) throws Exception {
		return buildJson.getBoolean("building");
	}

	public List<String> getBuildURLs(String url) throws Exception {
		JSONObject jsonObject = jsonGetter.getJson(url);

		return getBuildURLs(jsonObject);
	}

	public List<String> getBuildURLs(JSONObject jobJson) throws Exception {
		JSONArray buildsJson = jobJson.getJSONArray("builds");

		List<String> buildURLs = new ArrayList<String>();

		for (int i = 0; i < buildsJson.length(); i++) {
			String remoteBuildURL = buildsJson.getJSONObject(i).getString("url");

			buildURLs.add(jsonGetter.convertURL(remoteBuildURL));
		}

		return buildURLs;
	}

}