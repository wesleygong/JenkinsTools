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

		String expectedURL = "http://test-14/14/";

		String remoteURL = "https://test.liferay.com/14/";

		String localURL = localJsonGetter.convertURL(remoteURL);

		assertEquals(localURL, expectedURL);

		expectedURL = "http://test-1-4/";

		remoteURL = "https://test-1-4.liferay.com/";

		localURL = localJsonGetter.convertURL(remoteURL);

		assertEquals(localURL, expectedURL);
	}

}