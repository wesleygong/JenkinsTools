package me.khyen.jenkins;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * @author Kevin Yen
 */
public class RemoteJsonGetterTest {

	@Test
	public void testConvertURL() {
		JsonGetter remoteJsonGetter = new RemoteJsonGetter("test", "test");

		String localURL = "http://test-14/14/";

		String remoteURL = remoteJsonGetter.convertURL(localURL);

		String expectedURL = "https://test.liferay.com/14/";

		System.out.println("Local URL: " + localURL);
		System.out.println("Remote URL: " + remoteURL);

		assertEquals(remoteURL, expectedURL);
	}

}