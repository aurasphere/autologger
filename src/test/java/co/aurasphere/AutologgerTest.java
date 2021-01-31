package co.aurasphere;

import org.junit.Test;

/**
 * Test class to run the application with a CI server.
 * 
 * @author Donato Rimenti
 */
public class AutologgerTest {

	/**
	 * Launches the application.
	 * 
	 * @throws InterruptedException
	 *                                  if any error occurs
	 */
	@Test
	public void test() throws InterruptedException {
		Autologger.main(null);
	}

}