package me.khyen.jenkins;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kevin Yen
 */
public class JenkinsJobUtil {

	public static String toLocalURL(String remoteURL) {
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