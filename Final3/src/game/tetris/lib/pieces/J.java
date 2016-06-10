package game.tetris.lib.pieces;

import java.awt.Color;

/**
 * J is a subclass instance of Tetrimino
 * and is a Tetrimino used in TetrisGUI.java.
 * 
 * The behavior of J is similar to the standard
 * J piece in Tetris.
 * 
 * @author Rahul
 *
 */
public class J extends Tetrimino {
	/**
	 * Orientations's first-level elements contain 
	 * the additives to give to the block rows and cols in a rotation
	 * that would bring them to an orientation = index of first-level element.
	 * 
	 * To see what orientation is, look at the class constructor.
	 */
	private static final int[][] ORIENTATIONS = {{-1, -1, 0, 0, 1, 1, 2, 0},
		   										 {-1, 1, 0, 0, 1, -1, 0, -2},
												 {1, 1, 0, 0, -1, -1, -2, 0},
												 {1, -1, 0, 0, -1, 1, 0, 2}};
	private static final int[] INIT_TET_COORDS = {0, 3, 0, 4, 0, 5, 1, 5};
	private static final Color TET_COLOR = Color.BLUE;
	
	private int orientationNum;
	
	/**
	 * "Default" J Constructor
	 * This builds the Tetrimino's default position and color via the
	 * class constants.
	 * 
	 * Furthermore, it initializes orientationNum, an int
	 * that represents the current orientation:
	 * 
	 *    [][][] => orientation = 0   []  => orientation = 1
	 *        []					  []
	 *    							[][]
	 *        
	 *    []     => orientation = 2   [][]   => orientation = 3
	 *    [][][]					  []
	 *    							  []    						   
	 *  
	 */
	public J() {
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
		
		//modulo 4 because we are switching between 4 orientations
		//in the other 0,1,2,3,0,1...
		orientationNum = (orientationNum + 1) % 4; 
		
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
		return "J";
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
