package me.khyen.jenkins;

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

}