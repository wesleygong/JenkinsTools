package me.khyen.jenkins;

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

}