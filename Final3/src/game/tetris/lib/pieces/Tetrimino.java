package game.tetris.lib.pieces;
import game.tetris.lib.pieces.Move;
import game.tetris.lib.pieces.Rotate;

import java.awt.Color;

/**
 * Tetrimino is an abstract class
 * for tetrimino pieces.
 * 
 * In the game itself, a player will control a subclass instance
 * of Tetrimino. The player can set the subclass-Tetrimono to move
 * or rotate, and the Board will run the move or rotate
 * if it is legal.
 * 
 * It has the row and col of each block (4) of
 * a tetrimino. Plus, it has the rotation and translation
 * that the player wants the tetrimino to do on the Board.
 * 
 * @author Rahul
 */
public abstract class Tetrimino {
	
	protected int firstBlockRow;
	protected int firstBlockCol;
	protected int secondBlockRow;
	protected int secondBlockCol;
	protected int thirdBlockRow;
	protected int thirdBlockCol;
	protected int fourthBlockRow;
	protected int fourthBlockCol;
	
	protected Color tetColor;
	
	protected Move translation;
	protected Rotate rotation;
	/**
	 * Constructs a Tetrimino
	 * @param initCoords arrray of integers with ordering
	 * 				     Element 1 = first block row, element 2 = first block col,
	 * 					 Element 3 = second block row, etc.
	 * @param color The color the Tetrimino will be.
	 */
	public Tetrimino(int[] initCoords, Color color) {
		firstBlockRow = initCoords[0];
		firstBlockCol = initCoords[1];
		secondBlockRow = initCoords[2];
		secondBlockCol = initCoords[3];
		thirdBlockRow = initCoords[4];
		thirdBlockCol = initCoords[5];
		fourthBlockRow = initCoords[6];
		fourthBlockCol = initCoords[7];
		
		tetColor = color;
		//As the piece was just created, 
		//it has no initial translation or rotation.
		translation = Move.NONE;
		rotation = Rotate.NONE;
	}
	/**
	 * Gets the Tetrimino block dimension members
	 * @return array of ints containing 
	 * 	       Tetrimino block dimensions.
	 */
	public int[] getTetBlockCoords() {
		int[] newTetBlockCoords = new int[8];
		
		newTetBlockCoords[0] = firstBlockRow;
		newTetBlockCoords[1] = firstBlockCol;
		newTetBlockCoords[2] = secondBlockRow;
		newTetBlockCoords[3] = secondBlockCol;
		newTetBlockCoords[4] = thirdBlockRow;
		newTetBlockCoords[5] = thirdBlockCol;
		newTetBlockCoords[6] = fourthBlockRow;
		newTetBlockCoords[7] = fourthBlockCol;
		return newTetBlockCoords;
	}
	/**
	 * Gets the translation member
	 * @return translation member
	 */
	public Move getTranslation() {
		return translation;
	}
	/**
	 * Gets the rotation member
	 * @return rotation member
	 */
	public Rotate getRotation() {
		return rotation;
	}
	/**
	 * Gets the tetColor member
	 * @return tetColor member
	 */
	public Color getTetColor() {
		return tetColor;
	}
	/**
	 * Sets translation member based on what the Player wants
	 * @param translation Desired Move
	 * @return None
	 */
	public void translate(Move translation) {
		this.translation = translation;
	}
	/**
	 * Sets rotation member based on what the Player wants
	 * @param rotation Desired Rotation
	 * @return None
	 */
	public void rotate(Rotate rotation) {
		this.rotation = rotation;
	}
	/**
	 * Sets the row and cols of all Tetrimino
	 * blocks.
	 * @param array of integers of size 8 with this pattern:
	 *        Element 1 = first block row, element 2 = first block col,
	 * 		  Element 3 = second block row, etc.
	 * @return None
	 */
	public void setAllTetBlockCoords(int [] newTetBlockCoords) {
		if (newTetBlockCoords.length != 8)
			return;
		
		firstBlockRow = newTetBlockCoords[0];
		firstBlockCol = newTetBlockCoords[1];
		secondBlockRow = newTetBlockCoords[2];
		secondBlockCol = newTetBlockCoords[3];
		thirdBlockRow = newTetBlockCoords[4];
		thirdBlockCol = newTetBlockCoords[5];
		fourthBlockRow = newTetBlockCoords[6];
		fourthBlockCol = newTetBlockCoords[7];
	}
	/**
	 * Returns a array of integers that contain information on 
	 * the new rows and cols of the Tetrimino blocks after the
	 * translation specified in member translation is done.
	 * @return array of integers. Element 1 = first block row, element 2 = first block col,
	 * 							   Element 3 = second block row, etc.
	 */
	public int[] translatedTetBlockCoords() {
		int[] newTetBlockCoords = new int[8];
		int amtToMove = translation.toInt();
		
		newTetBlockCoords[0] = firstBlockRow;
		newTetBlockCoords[1] = firstBlockCol + amtToMove;
		newTetBlockCoords[2] = secondBlockRow;
		newTetBlockCoords[3] = secondBlockCol + amtToMove;
		newTetBlockCoords[4] = thirdBlockRow;
		newTetBlockCoords[5] = thirdBlockCol + amtToMove;
		newTetBlockCoords[6] = fourthBlockRow;
		newTetBlockCoords[7] = fourthBlockCol + amtToMove;
		
		//Stop the block from continuously moving after this move.
		translation = Move.NONE;
		return newTetBlockCoords;
	}
	/**
	 * Returns a array of integers that contain information on 
	 * the new rows and cols of the Tetrimino blocks after a
	 * piece falls by 1 row.
	 * @return array of integers. Element 1 = first block row, element 2 = first block col,
	 * 							   Element 3 = second block row, etc.
	 */
	public int[] fallingTetBlockCoords() {
		int[] newTetBlockCoords = new int[8];
		
		newTetBlockCoords[0] = firstBlockRow + 1;
		newTetBlockCoords[1] = firstBlockCol;
		newTetBlockCoords[2] = secondBlockRow + 1;
		newTetBlockCoords[3] = secondBlockCol;
		newTetBlockCoords[4] = thirdBlockRow + 1;
		newTetBlockCoords[5] = thirdBlockCol; 
		newTetBlockCoords[6] = fourthBlockRow + 1;
		newTetBlockCoords[7] = fourthBlockCol;
		return newTetBlockCoords;
	}
	/**
	 * Returns a array of integers that contain information on 
	 * the new rows and cols of the Tetrimino blocks after the 
	 * rotation specified in member rotation is done.
	 * This method is abstract since each Tetrimino subclass has a different rotation.
	 * @return array of integers. Element 1 = first block row, element 2 = first block col,
	 * 							   Element 3 = second block row, etc.
	 */
	public abstract int[] rotatingTetBlockCoords();
	/**
	 * Function that checks in params are actually coordinates
	 * of this Tetrimono's Blocks.
	 * @param row of the dimension
	 * @param col of the dimension
	 * @return true  if (row,col) is one of the coords 
	 * 	             of this Tetrimono's block
	 * 		   false otherwise
	 */
	public boolean isCoordABlockCoord(int row, int col) {
		return (row == firstBlockRow && col == firstBlockCol) ||
			   (row == secondBlockRow && col == secondBlockCol) ||
			   (row == thirdBlockRow && col == thirdBlockCol) ||
			   (row == fourthBlockRow && col == fourthBlockCol);
	}
	/**
	 * Returns the name of the class.
	 * @return String name of the class.
	 */
	public abstract String toString();
	/**
	 * Getter for orientation number.
	 * @return orientationNum
	 */
	public abstract int getOrientationNum();
	/**
	 * Setter that sets all members of this piece
	 * @param newTetBlockCoords list of 8 coords of Tetrimino blocks
	 * @param trans Move the Tetrimino must do.
	 * @param rot Rotate the Tetrimino must do.
	 * @param orientNum Int specifying Tetrimino's orientation number 
	 * @return Nothing
	 */
	public abstract void setAllMembersOfPiece(int [] newTetBlockCoords, Move trans, Rotate rot, int orientNum);
	/**
	 * Static Function that returns a heap-allocated
	 * Tetrmino subclass instance depending on the param.
	 * @param charRepOfPiece char that represents the falling piece
	 * @return Tetrmino subclass instance that represents the falling piece
	 */
	public static Tetrimino charToTetrimino(char charRepOfPiece) {
		if (charRepOfPiece == 'I')
			return new I();
		if (charRepOfPiece == 'J')
			return new J();
		if (charRepOfPiece == 'L')
			return new L();
		if (charRepOfPiece == 'O')
			return new O();
		if (charRepOfPiece == 'S')
			return new S();
		if (charRepOfPiece == 'T')
			return new T();
		else
			return new Z();
	}
}