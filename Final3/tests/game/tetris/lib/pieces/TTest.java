package game.tetris.lib.pieces;

import static org.junit.Assert.*;

import org.junit.Test;

public class TTest {

	@Test
	public void construction_and_basic_get_set_tests() {
		T piece = new T();
		int[] correctPositions = {0, 3, 0, 4, 0, 5, 1, 4};
		assertArrayEquals(piece.getTetBlockCoords(), correctPositions);
		
		for (int i = 0; i < 8; i++)
			correctPositions[i] = 42;
		
		piece.setAllTetBlockCoords(correctPositions);
		assertArrayEquals(piece.getTetBlockCoords(), correctPositions);
	}

	@Test
	public void falling_piece_test() {
		T piece = new T();
		int[] correctPositions = {0, 3, 0, 4, 0, 5, 1, 4};
		
		//This for loop will test if a piece can fall down
		for (int fallAmt = 0; fallAmt < 10; fallAmt++) {
			//This inner for loop makes all the semantic rows in the 8-element
			//int array increment by 1 (or fall by 1 row)
			for (int i = 0; i < 8; i += 2)
				correctPositions[i] = correctPositions[i] + 1;
			
			assertArrayEquals(piece.fallingTetBlockCoords(), correctPositions);
			
			piece.setAllTetBlockCoords(correctPositions);
			assertArrayEquals(piece.getTetBlockCoords(), correctPositions);
		}
	}
	
	@Test
	public void translating_piece_test() {
		T piece = new T();
		int[] correctPositions = {0, 3, 0, 4, 0, 5, 1, 4};
		
		//This for loop will test if the pice can translate to the right.
		for (int transAmt = 0; transAmt < 10; transAmt++) {
			//This inner for loop makes all the semantic cols in the 8-element
			//int array increment by 1 (or translate 1 col to the right)
			for (int i = 1; i < 8; i += 2)
				correctPositions[i] = correctPositions[i] + 1;
			
			//Translate once to the right
			piece.translate(Move.RIGHT);
			assertEquals(piece.getTranslation(), Move.RIGHT);
			assertArrayEquals(piece.translatedTetBlockCoords(), correctPositions); 
			//Assert that translatedTetBlockCoords() resets the move.
			assertEquals(piece.getTranslation(), Move.NONE);
			
			piece.setAllTetBlockCoords(correctPositions);
			assertArrayEquals(piece.getTetBlockCoords(), correctPositions);
		}
		
		//This for loop will test if the pice can translate to the left.
		for (int transAmt = 0; transAmt < 10; transAmt++) {
			//This inner for loop makes all the semantic cols in the 8-element
			//int array decrement by 1 (or translate 1 col to the left)
			for (int i = 1; i < 8; i += 2)
				correctPositions[i] = correctPositions[i] - 1;
			
			//Translate once to the left
			piece.translate(Move.LEFT);
			assertEquals(piece.getTranslation(), Move.LEFT);
			assertArrayEquals(piece.translatedTetBlockCoords(), correctPositions);
			//Assert that translatedTetBlockCoords() resets the move.
			assertEquals(piece.getTranslation(), Move.NONE);
					
			piece.setAllTetBlockCoords(correctPositions);
			assertArrayEquals(piece.getTetBlockCoords(), correctPositions);
		}
	}
	
	@Test
	public void rotating_piece_test() {
		T piece = new T();
		int[][] orientations = {{0, 3, 0, 4, 0, 5, 1, 4},
								{-1, 4, 0, 4, 1, 4, 0, 3},
								{0, 5, 0, 4, 0, 3, -1, 4},
							    {1, 4, 0, 4, -1, 4, 0, 5} };
		
		//This for loop will rotate the piece four times.
		for (int rot = 1; rot < 5; rot++) {
			//Rotate once
			piece.rotate(Rotate.CW);
			assertEquals(piece.getRotation(), Rotate.CW);
			
			//The modulo logic in this assert will allow the four rotations will be 
			//correct if it rotates to orientation[1], then orientation[2], 
			//then orientation[3], then orientation[0]
			assertArrayEquals(piece.rotatingTetBlockCoords(), orientations[rot % 4]);
			
			//Assert that rotatingTetBlockCoords() resets the rotate.
			assertEquals(piece.getRotation(), Rotate.NONE);
			
			piece.setAllTetBlockCoords(orientations[rot % 4]);
			assertArrayEquals(piece.getTetBlockCoords(), orientations[rot % 4]);
		}
	}
}
