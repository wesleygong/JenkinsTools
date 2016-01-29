package me.khyen.jenkins;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kevin Yen
 */
public class JenkinsProperties {

	public static String getJenkinsLocalURL(int master) {
		return new String("http://test-1-" + master + "/");
	}

	public static List<String> getJenkinsLocalURLs() {
		List<String> jenkinsLocalURLs = new ArrayList<String>();

		for (int i = start; i <= end; i++) {
			String jenkinsLocalURL = getJenkinsLocalURL(i);

			jenkinsLocalURLs.add(jenkinsLocalURL);
		}

		return jenkinsLocalURLs;
	}

	public static String getJenkinsRemoteURL(int master) {
		return new String("https://test-1-" + master + ".liferay.com/");
	}

	public static List<String> getJenkinsRemoteURLs() {
		List<String> jenkinsRemoteURLs = new ArrayList<String>();

		for (int i = start; i <= end; i++) {
			String jenkinsRemoteURL = getJenkinsRemoteURL(i);

			jenkinsRemoteURLs.add(jenkinsRemoteURL);
		}

		return jenkinsRemoteURLs;
	}

	public static List<String> getBranches() {
		List<String> branches = new ArrayList<String>();

		branches.add("master");
		branches.add("ee-6.2.x");
		branches.add("ee-6.1.x");

		return branches;
	}

	public static List<String> getPullRequestJobURLs() {
		List<String> pullRequestJobURLs = new ArrayList<String>();

		for (int i = start; i <= end; i++) {
			pullRequestJobURLs.addAll(getPullRequestJobURLs(i));
		}

		return pullRequestJobURLs;
	}

	public static List<String> getPullRequestJobURLs(int master) {
		List<String> pullRequestJobURLs = new ArrayList<String>();

		List<String> pullRequestJobNames = getPullRequestJobNames();

		String jenkinsLocalURL = getJenkinsLocalURL(master);

		for (String pullRequestJobName : pullRequestJobNames) {
			StringBuilder sb = new StringBuilder();

			sb.append(jenkinsLocalURL);
			sb.append("job/");
			sb.append(pullRequestJobName);

			pullRequestJobURLs.add(sb.toString());
		}

		return pullRequestJobURLs;
	}

	public static List<String> getPullRequestJobTypes() {
		List<String> pullRequestJobTypes = new ArrayList<String>();

		pullRequestJobTypes.add("test-portal-acceptance-pullrequest");

		return pullRequestJobTypes;
	}

	public static List<String> getPullRequestJobNames() {
		List<String> pullRequestJobNames = new ArrayList<String>();
		List<String> pullRequestJobTypes = getPullRequestJobTypes();
		List<String> branches = getBranches();

		for (String pullRequestJobType : pullRequestJobTypes) {
			for (String branch : branches) {
				StringBuilder sb = new StringBuilder();

				sb.append(pullRequestJobType);
				sb.append("(");
				sb.append(branch);
				sb.append(")");

				pullRequestJobNames.add(sb.toString());
			}
		}

		return pullRequestJobNames;
	}

	private final static int start = 1;
	private final static int end = 20;

}