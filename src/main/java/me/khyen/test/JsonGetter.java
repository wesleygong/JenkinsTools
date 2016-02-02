package me.khyen.jenkins;

import org.json.JSONObject;

/**
 * @author Kevin Yen
 */
public interface JsonGetter {

	public String convertURL(String url);

	public JSONObject getJson(String url) throws Exception;

}