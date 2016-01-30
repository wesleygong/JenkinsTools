package me.khyen.jenkins;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;

import org.apache.commons.codec.binary.Base64;

/**
 * @author Kevin Yen
 */
public class JenkinsStatus {

	public static void main(String [] args) throws Exception {
		Set<Future<List<String>>> futures = new HashSet<Future<List<String>>>();

		ExecutorService executor = Executors.newFixedThreadPool(30);

		CompletionService<List<String>> completionService = new ExecutorCompletionService<List<String>>(executor);

		for (String pullRequestJobURL : JenkinsProperties.getPullRequestJobURLs()) {
			Callable<List<String>> callable = new ActiveBuildURLsGetter(pullRequestJobURL);

			futures.add(completionService.submit(callable));
		}

		List<String> activePullRequestURLs = new ArrayList<String>();

		while (futures.size() > 0) {
			Future<List<String>> completedFuture = completionService.take();

			futures.remove(completedFuture);

			List<String> activeBuildURLs = completedFuture.get();

			activePullRequestURLs.addAll(activeBuildURLs);
		}

		executor.shutdown();

		System.out.println("Listing currently running pull requests...");

		for (String activePullRequestURL : activePullRequestURLs) {
			System.out.println(activePullRequestURL);
		}

		System.out.println(activePullRequestURLs.size() + " pull requests are currently running");
	}

	public static class ActiveBuildURLsGetter implements Callable<List<String>> {

		private String jobURL;

		public ActiveBuildURLsGetter(String jobURL) {
			this.jobURL = jobURL;
		}

		@Override
		public List<String> call() throws Exception {
			return JenkinsJob.getActiveBuildURLs(jobURL);
		}

	}

	public static String encodeAuthorizationFields(
		String username, String password) {

		String authorizationString = username + ":" + password;

		return new String(Base64.encodeBase64(authorizationString.getBytes()));
	}

}