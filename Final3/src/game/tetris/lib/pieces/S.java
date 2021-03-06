package game.tetris.lib.pieces;

import java.awt.Color;

/**
 * S is a subclass instance of Tetrimino
 * and is a Tetrimino used in TetrisGUI.java.
 * 
 * The behavior of S is similar to the standard
 * S piece in Tetris.
 * 
 * @author Rahul
 *
 */
public class S extends Tetrimino {
	/**
	 * Orientations's first-level elements contain 
	 * the additives to give to the block rows and cols in a rotation
	 * that would bring them to an orientation = index of first-level element.
	 * 
	 * To see what orientation is, look at the class constructor.
	 */
	private static final int[][] ORIENTATIONS = {{1, 1, 0, 0, 1, -1, 0, -2},
		   										 {-1, -1, 0, 0, -1, 1, 0, 2}};
	private static final int[] INIT_TET_COORDS = {0, 5, 0, 4, 1, 4, 1, 3};
	private static final Color TET_COLOR = Color.GREEN;
	
	private int orientationNum;
	
	/**
	 * "Default" S Constructor
	 * This builds the Tetrimino's default position and color via the
	 * class constants.
	 * 
	 * Furthermore, it initializes orientationNum, an int
	 * that represents the current orientation:
	 * 
	 *    [][] => orientation = 0  []  => orientation = 1
	 *  [][]  					   [][]    
	 *    							 []
	 *  
	 */
	public S() {
		super(INIT_TET_COORDS, TET_COLOR);
		orientationNum = 0;
	}
	/**
	 * @see Tetrimino.java for more information
	 */
	@Override
	public int[] rotatingTetBlockCoords() {
		//If there is no rotation desired, then we 
		//return the current Tetrimino block coords.
		if (rotation == Rotate.NONE)
			return getTetBlockCoords();
		
		int[] currTetBlockCoords = getTetBlockCoords();
		int[] newTetBlockCoords = new int[8];
		
		orientationNum = (orientationNum == 0) ? 1 : 0; 
		
		//This will add the current tet. block coords with one 
		//element of rotative additives from ORIENTATIONS.
		for (int i = 0; i < 8; i++)
			newTetBlockCoords[i] = currTetBlockCoords[i] + ORIENTATIONS[orientationNum][i];
		
		//Stop the block from continuously rotating after this rotation
		rotation = Rotate.NONE;
		return newTetBlockCoords; 
	}
	/**
	 * See Tetrimino.java for more information
	 */
	@Override
	public String toString() {
		return "S";
	}
	/**
	 * @see Tetrimino.java for more information
	 */
	public int getOrientationNum() {
		return orientationNum;
	}
	/**
	 * @see Tetrimino.java for more information
	 */
	public void setAllMembersOfPiece(int [] newTetBlockCoords, Move trans, Rotate rot, int orientNum) {
		setAllTetBlockCoords(newTetBlockCoords);
		translate(trans);
		rotate(rot);
		orientationNum = orientNum;
	}
}
