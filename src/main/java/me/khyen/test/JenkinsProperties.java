package me.khyen.jenkins;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kevin Yen
 */
public class JenkinsProperties {

	public static List<String> getPullRequestJobURLs() {
		List<String> pullRequestJobURLs = new ArrayList<String>();

		for (int i = start; i <= end; i++) {
			pullRequestJobURLs.addAll(getPullRequestJobURLs(i));
		}

		return pullRequestJobURLs;
	}

	public static List<String> getPullRequestJobURLs(int master) {
		List<String> pullRequestJobURLs = new ArrayList<String>();

		String jenkinsLocalURL = getJenkinsLocalURL(master);

		for (String pullRequestJobName : getPullRequestJobNames()) {
			StringBuilder sb = new StringBuilder();

			sb.append(jenkinsLocalURL);
			sb.append("job/");
			sb.append(pullRequestJobName);

			pullRequestJobURLs.add(sb.toString());
		}

		return pullRequestJobURLs;
	}

	public static List<String> getPullRequestJobNames() {
		List<String> pullRequestJobNames = new ArrayList<String>();

		for (String pullRequestJobType : getPullRequestJobTypes()) {
			for (String branch : getBranches()) {
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

	public static List<String> getPullRequestJobTypes() {
		List<String> pullRequestJobTypes = new ArrayList<String>();

		pullRequestJobTypes.add("test-portal-acceptance-pullrequest");

		return pullRequestJobTypes;
	}

	public static List<String> getJenkinsJobURLs(String jobName) {
		return getJenkinsJobURLs(jobName, false);
	}

	public static List<String> getJenkinsJobURLs(String jobName, boolean remote) {
		List<String> jenkinsJobURLs = new ArrayList<String>();		

		for (int i = start; i <= end; i++) {
			jenkinsJobURLs.addAll(getJenkinsJobURLs(jobName, i, remote));
		}

		return jenkinsJobURLs;
	}

	public static List<String> getJenkinsJobURLs(String jobName, int master) {
		return getJenkinsJobURLs(jobName, master, false);
	}

	public static List<String> getJenkinsJobURLs(String jobName, int master, boolean remote) {
		List<String> jenkinsJobURLs = new ArrayList<String>();

		String jenkinsURL = getJenkinsURL(master, remote);

		for (String jenkinsJobName : getJenkinsJobNames(jobName)) {
			StringBuilder sb = new StringBuilder();

			sb.append(jenkinsURL);
			sb.append("job/");
			sb.append(jenkinsJobName);
			
			jenkinsJobURLs.add(sb.toString());
		}

		return jenkinsJobURLs;
	}

	public static List<String> getJenkinsJobNames(String jobName) {
		List<String> jenkinsJobNames = new ArrayList<String>();

		for (String branch : getBranches()) {
			StringBuilder sb = new StringBuilder();

			sb.append(jobName);
			sb.append("(");
			sb.append(branch);
			sb.append(")");

			jenkinsJobNames.add(sb.toString());
		}

		return jenkinsJobNames;
	}

	public static List<String> getJenkinsURLs() {
		return getJenkinsLocalURLs();
	}

	public static List<String> getJenkinsLocalURLs() {
		return getJenkinsURLs(false);
	}

	public static List<String> getJenkinsRemoteURLs() {
		return getJenkinsURLs(true);
	}

	public static List<String> getJenkinsURLs(boolean remote) {
		List<String> jenkinsURLs = new ArrayList<String>();

		for (int i = start; i <= end; i++) {
			jenkinsURLs.add(getJenkinsURL(i, remote));
		}

		return jenkinsURLs;
	}

	public static String getJenkinsURL(int master) {
		return getJenkinsURL(master, false);
	}

	public static String getJenkinsURL(int master, boolean remote) {
		if (remote == true) {
			return getJenkinsRemoteURL(master);
		}
		else {
			return getJenkinsLocalURL(master);
		}
	}

	public static String getJenkinsLocalURL(int master) {
		return new String("http://test-1-" + master + "/");
	}

	public static String getJenkinsRemoteURL(int master) {
		return new String("https://test-1-" + master + ".liferay.com/");
	}

	public static List<String> getBranches() {
		List<String> branches = new ArrayList<String>();

		branches.add("master");
		branches.add("ee-6.2.x");
		branches.add("ee-6.1.x");

		return branches;
	}

	private final static int start = 1;
	private final static int end = 20;

}