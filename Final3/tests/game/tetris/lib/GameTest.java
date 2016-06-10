package game.tetris.lib;

import static org.junit.Assert.*;
import game.tetris.lib.pieces.*;

import org.junit.Test;

public class GameTest {

	@Test
	public void basic_construction_and_tick_tock_test() {
		Game game = new Game();
		Tetrimino fallingPiece = game.getFallingPiece();
		
		int rowFirstPieceStopsFalling;
		String pieceName = fallingPiece.toString();
		
		if (pieceName.equals("I"))
			rowFirstPieceStopsFalling = 18;
		else
			rowFirstPieceStopsFalling = 17;

		//Here, for the entire for loop, the piece should be the same.
		for (int i = 0; i <= rowFirstPieceStopsFalling; i++) {
			game.tickTock();
			fallingPiece = game.getFallingPiece();
			
			String newPieceName = fallingPiece.toString();
			assertTrue(pieceName.equals(newPieceName));
		}
		
		//Now, there should be a new piece.
		game.tickTock();
		//Let's assert that it is new by checking if (0,4)
		//is a block coord of the new piece.
		fallingPiece = game.getFallingPiece();
		assertTrue(fallingPiece.isCoordABlockCoord(0, 4));
	}

}
