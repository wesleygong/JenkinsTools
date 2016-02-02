package me.khyen.jenkins;

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

/**
 * @author Kevin Yen
 */
public class RemoteJsonGetter implements JsonGetter {

	private String username;
	private String password;

	private RemoteJsonGetter() {};

	public RemoteJsonGetter(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public JSONObject getJson(String url) throws Exception {
		CredentialsProvider provider = new BasicCredentialsProvider();

		Credentials credentials = new UsernamePasswordCredentials(username, password);

		provider.setCredentials(AuthScope.ANY, credentials);

		HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

		HttpResponse httpResponse = httpClient.execute(new HttpGet(url));

		int statusCode = httpResponse.getStatusLine().getStatusCode();

		String jsonString = IOUtils.toString(httpResponse.getEntity().getContent());

		return new JSONObject(jsonString);
	}

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

	private static final Pattern localURLPattern1 = Pattern.compile(
		"http://test-[0-9]+/([0-9]+)/?");
	private static final Pattern localURLPattern2 = Pattern.compile(
		"http://(test-[0-9]+-[0-9]+)/?");

}