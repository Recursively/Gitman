package tests;

import model.GameWorld;

public class TestSuite {
	
	private static GameWorld gameWorld;

	public static void main(String[] args) {
		new TestSuite();
	}

	public TestSuite() {
		gameWorld = new GameWorld();
	}

	public static GameWorld getGameWorld() {
		return gameWorld;
	}
}
