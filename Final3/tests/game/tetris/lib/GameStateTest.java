package game.tetris.lib;

import static org.junit.Assert.*;

import org.junit.Test;

public class GameStateTest {

	@Test
	public void basic_construction_and_deep_copy_checks_test() {
		Game tetrisGame = new Game();
		GameState saveState = new GameState(tetrisGame.getBoard(), 
											tetrisGame.getFallingPiece());
		assertNotSame(saveState.getSavedBoard(), tetrisGame.getBoard());
		assertNotSame(saveState.getSavedFallingPiece(), tetrisGame.getFallingPiece());
	}

}
