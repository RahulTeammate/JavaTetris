package game.tetris.lib;

import java.awt.Color;

import game.tetris.lib.pieces.*;

/**
 * This class is used to save a state of
 * a Tetris Game once a falling piece stops moving
 * 
 * Once the falling piece stops moving, the falling piece
 * and tetris board are saved into a Game State.
 * 
 * The tetris board is deep copied into the member board becauase
 * it will be modified as to unlight the tiles representing the
 * stopped falling piece.
 * 
 * The member fallingPiece is made as a new Tetrimino subclass instance
 * that happens to be the same class as the falling piece of the game.
 * This is done because it resets the falling piece to its original position.
 * 
 * The mutation of the deep-copied tetris board is needed.
 * When this Game State is popped from a stack of Game States
 * and the tetris Game is modified to the popped Game State's
 * properties, the Game will be at the state which the previous
 * piece is only starting to fall. 
 * 
 * Thus, this fulfills the role of an undo.
 * 
 * @author Rahul
 *
 */
public class GameState {
	private static final int BOARD_MIN_ROW = 0;
	private static final int BOARD_MAX_ROW = 20;
	private static final int BOARD_MIN_COL = 0;
	private static final int BOARD_MAX_COL = 10;
	
	private static final int NUM_BLOCK_COORDS = 8;
	
	private Tile[][] board;
	private Tetrimino fallingPiece;
	
	/**
	 * Game State Constructor
	 * The members
	 * @param boardToSave Tile[][] representing the board
	 * @param fallingPiece Tetrimino representing the falling piece
	 */
	public GameState(Tile[][] boardToSave, Tetrimino fallingPieceToSave) {
		board = new Tile[BOARD_MAX_ROW][BOARD_MAX_COL];
		
		//Deep copy of board
		for (int row = BOARD_MIN_ROW; row < BOARD_MAX_ROW; row++) {
			for (int col = BOARD_MIN_COL; col < BOARD_MAX_COL; col++) {
				Color colorOfTile = boardToSave[row][col].getColor();
				
				board[row][col] = new Tile();
				board[row][col].setColor(colorOfTile);
			}
		}
		
		fallingPiece = newInstanceOf(fallingPieceToSave);
		setFallingPieceToTopOfBoard(fallingPieceToSave);
	}
	/**
	 * This private helper function returns a new Tetrimino subclass
	 * instance that is the same class as the param.
	 * @param piece Tetrimino 
	 * @return Tetrimino subclass instance which 
	 *         is the same class as the param
	 */
	private Tetrimino newInstanceOf(Tetrimino piece) {
		if (piece instanceof I)
			return new I();
		if (piece instanceof J)
			return new J();
		if (piece instanceof L)
			return new L();
		if (piece instanceof O)
			return new O();
		if (piece instanceof S)
			return new S();
		if (piece instanceof T)
			return new T();
		else
			return new Z();
	}/**
	 * This private helper function mutates the board member
	 * (which was deep copied in the constructor).
	 * 
	 * It unlights the Tiles inhabited by stoppedFallingPiece.
	 * It then lights up the Tiles inhabited by member fallingPiece.
	 * 
	 * This gives the illusion that the falling piece has been restored
	 * to the top of the board.
	 * @param stoppedFallingPiece Tetrimino instance
	 * @return Nothing
	 */
	private void setFallingPieceToTopOfBoard(Tetrimino stoppedFallingPiece) {
		int[] posOfBlocksToUnlight = stoppedFallingPiece.getTetBlockCoords();
		int[] posOfBlocksToLight = fallingPiece.getTetBlockCoords();
		
		int rowOfBlock = 0;
		int colOfBlock = 0;
		
		for (int i = 0; i < NUM_BLOCK_COORDS; i += 2) {
			//Unlights Tiles inhabited by stoppedFallingPiece
			rowOfBlock = posOfBlocksToUnlight[i];
			colOfBlock = posOfBlocksToUnlight[i + 1];
			board[rowOfBlock][colOfBlock].setColor(Color.BLACK);
			
			//Lights Tiles inhabited by member fallingPiece
			rowOfBlock = posOfBlocksToLight[i];
			colOfBlock = posOfBlocksToLight[i + 1];
			Color pieceColor = fallingPiece.getTetColor();
			board[rowOfBlock][colOfBlock].setColor(pieceColor);
		}
	}
	/**
	 * Getter for the member board.
	 * Used in undo and load.
	 * @return Tile[][] Same data type as the board member in 
	 *                  a Game instance.
	 */
	public Tile[][] getSavedBoard() {
		return board;
	}
	/**
	 * Getter for the member fallingPiece.
	 * Used in undo and load.
	 * @return Tetrimino Same data type as the fallingPiece 
	 *                   member in a Game instance.
	 */
	public Tetrimino getSavedFallingPiece() {
		return fallingPiece;
	}
}
