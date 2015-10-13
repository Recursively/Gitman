package tests;

import model.GameWorld;

/**
 * Test suite manager that creates a mock game world and then calls all the respective test suites.
 *
 * @author Marcel
 */
public class TestSuite {
	
	private static GameWorld gameWorld;

	/**
	 * Run this to run the tests
	 *
	 * @param args Redundant
	 */
	public static void main(String[] args) {
		// Runs tests
		new TestSuite();
	}

	/**
	 * Creates a new test suite, makes the game world and then runs the tests
	 */
	public TestSuite() {
		gameWorld = new GameWorld();

		//TODO runs tests
	}

	public static GameWorld getGameWorld() {
		return gameWorld;
	}
}
