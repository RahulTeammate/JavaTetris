package game.tetris.lib.pieces;

import java.awt.Color;

/**
 * O is a subclass instance of Tetrimino
 * and is a Tetrimino used in TetrisGUI.java.
 * 
 * The behavior of O is similar to the standard
 * O piece in Tetris.
 * 
 * @author Rahul
 *
 */
public class O extends Tetrimino {
	private static final int[] INIT_TET_COORDS = {0, 4, 0, 5, 1, 4, 1, 5};
	private static final Color TET_COLOR = Color.YELLOW;
	
	/**
	 * "Default" O Constructor
	 * This builds the Tetrimino's default position and color via the
	 * class constants.
	 * 
	 */
	public O() {
		super(INIT_TET_COORDS, TET_COLOR);
	}
	/**
	 * @see Tetrimino.java for more information
	 */
	@Override
	public int[] rotatingTetBlockCoords() {
		//A rotated O stays the same orientation, silly!
		rotation = Rotate.NONE; 
		return getTetBlockCoords();
	}
	/**
	 * See Tetrimino.java for more information
	 */
	@Override
	public String toString() {
		return "O";
	}
	/**
	 * @see Tetrimino.java for more information
	 */
	public int getOrientationNum() {
		return 0;
	}
	/**
	 * @see Tetrimino.java for more information
	 */
	public void setAllMembersOfPiece(int [] newTetBlockCoords, Move trans, Rotate rot, int orientNum) {
		setAllTetBlockCoords(newTetBlockCoords);
		translate(trans);
		rotate(rot);
		//ignore orientNum as it is not a member here.
	}
}
