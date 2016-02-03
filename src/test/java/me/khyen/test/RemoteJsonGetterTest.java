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

		String expectedURL = "https://test.liferay.com/14/";

		String localURL = "http://test-14/14/";

		String remoteURL = remoteJsonGetter.convertURL(localURL);

		assertEquals(remoteURL, expectedURL);

		expectedURL = "https://test-1-4.liferay.com/";

		localURL = "http://test-1-4/";

		remoteURL = remoteJsonGetter.convertURL(localURL);

		assertEquals(remoteURL, expectedURL);
	}

}