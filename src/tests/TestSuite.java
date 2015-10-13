package tests;

import model.GameWorld;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import controller.AudioController;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	DataTests.class,
	GameWorldTests.class
})

/**
 * Test suite manager that creates a mock game world and then calls all the respective test suites.
 *
 * @author Marcel
 */
public class TestSuite {

	private static GameWorld gameWorld;

	/**
	 * Creates a new test suite, makes the game world and then runs the tests
	 */
	public TestSuite() {
		gameWorld = new GameWorld();
		new AudioController();
	}

	/**
	 * Gets game world.
	 *
	 * @return the game world
	 */
	public static GameWorld getGameWorld() {
		return gameWorld;
	}

	/**
	 * Runs the test suite
	 */
	public static void main(String[] args) {
		JUnitCore.runClasses(TestSuite.class);
	}
}
