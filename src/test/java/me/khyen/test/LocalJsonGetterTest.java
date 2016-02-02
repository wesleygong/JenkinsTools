package me.khyen.jenkins;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * @author Kevin Yen
 */
public class LocalJsonGetterTest {

	@Test
	public void testConvertURL() {
		JsonGetter localJsonGetter = new LocalJsonGetter();

		String remoteURL = "https://test.liferay.com/14/";

		String localURL = localJsonGetter.convertURL(remoteURL);

		String expectedURL = "http://test-14/14/";

		System.out.println("Remote URL: " + remoteURL);
		System.out.println("Local URL: " + localURL);

		assertEquals(localURL, expectedURL);
	}

}