package game.tetris.lib;

import game.tetris.lib.pieces.*;

import java.util.Vector;
import java.util.Random;
import java.awt.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * The main class of game.tetris.lib 
 * that can (and should) be used for running
 * a Tetris Game.
 * 
 * This class contains a board of tiles to be
 * used as the Tetris Board, a Tetrimino subclass to
 * be used as a control of a falling piece, and
 * multiple other members for the main Tetris game loop.
 * 
 * 
 * @author Rahul
 */
public class Game {
	private static final int BOARD_MIN_ROW = 0;
	private static final int BOARD_MAX_ROW = 20;
	private static final int BOARD_MIN_COL = 0;
	private static final int BOARD_MAX_COL = 10;
	
	private static final int NUM_TET_TYPES = 7;
	private static final int NUM_ROW_TILES = 10;
	
	private static final int MAX_STACK_SIZE = 5;
	
	private static final String FILENAME = "savefile.txt";
	
	private Tile[][] board;
	private Tetrimino fallingPiece;
	private boolean paused;
	private boolean gameover;
	
	private Vector<GameState> stack;
	
	/**
	 * Default Board Constructor 
	 * 
	 * member paused is set to true when the game is paused. As a result,
	 * tickTock() cuts off before moving a falling piece.
	 * 
	 */
	public Game() {
		//Heap construction of the 2D Tile Array
		board = new Tile[BOARD_MAX_ROW][BOARD_MAX_COL];
		
		for (int row = BOARD_MIN_ROW; row < BOARD_MAX_ROW; row++) {
			for (int col = BOARD_MIN_COL; col < BOARD_MAX_COL; col++) {
				board[row][col] = new Tile();
			}
		}
		stack = new Vector<GameState>();
		paused = false;
		gameover = false;
		
		//start the falling pieces
		putRandPiece();
		//display this falling piece
		Color colorOfPiece = fallingPiece.getTetColor();
		lightUpFallingPiece(colorOfPiece);
	}
	/**
	 * This function initializes the member
	 * fallingPiece to a 
	 * randomly picked Tetrimino piece.
	 * @return Nothing
	 */
	public void putRandPiece() {
		Random randomizer = new Random();
		//Randomly pick a number from 0 to NUM_TET_TYPES-1
		int rand = randomizer.nextInt(NUM_TET_TYPES);
		
		if (rand == 0)
			fallingPiece = new I();
		else if (rand == 1)
			fallingPiece = new J();
		else if (rand == 2)
			fallingPiece = new L();
		else if (rand == 3)
			fallingPiece = new O();
		else if (rand == 4)
			fallingPiece = new S();
		else if (rand == 5)
			fallingPiece = new T();
		else if (rand == 6)
			fallingPiece = new Z();
		
		gameOverCheck();
	}
	/**
	 * This function only activates 
	 * immeadiatedly after putRandPiece().
	 * 
	 * It analyzes if the game is over iff
	 * the new falling piece cannot fall.
	 * @return Nothing
	 */
	private void gameOverCheck() {
		//This means the piece cannot fall from 
		//its initial position, ending the game.
		if (pieceStopsFalling()) {
			gameover = true;
		}
	}
	/**
	 * This function is essentially the time units of
	 * Game. If this function activates, all of these
	 * steps are done (in helper functions)
	 * 
	 * 1. Falling piece is checked if it can stop falling. If so...
	 *    a. Save state of the game without the piece. Unlight the stopped falling piece.
	 *    b. The rows are checked if they are completed (as to remove said 
	 *       row and move all non-falling pieces above it down one space).
	 *    c. Put a new random piece.
	 * 2. the falling piece falls/translates/rotates.
	 *    a. De-light the corresponding Tiles to the original position 
	 *       of the falling piece's blocks.
	 *    b. Check collision detection of falling piece and if it runs off the side
	 *       - If piece would go into another piece, negate the rotation and translation
	 *       - Otherwise, do the rotation and translation
	 *    c. Light up the corresponding Tiles to the new position
	 *       of the falling piece's blocks
	 * 
	 * @return true if the time unit passed
	 * 		   false if the time unit failed to pass (game is paused)
	 */
	public boolean tickTock() { 
		if (paused || gameover)
			return false;
		//Step 1
		if (pieceStopsFalling()) {
			//Step 1a
			saveState();
			//Step 1b
			checkForCompletedRows();
			//Step 1c
			putRandPiece();
		}
		else {
			int[] interm = null;
			//Step 2a
			lightUpFallingPiece(Color.BLACK);
			//Step 2b - pieceRunsOffSide() comes first to short-circuit the conditional
			//in case pieceRunsOffSide() returns true
			if (pieceRunsOutOfBounds() == false && playerInputCausesCollision() == false) {
				interm = fallingPiece.rotatingTetBlockCoords();
				fallingPiece.setAllTetBlockCoords(interm);
				interm = fallingPiece.translatedTetBlockCoords();
				fallingPiece.setAllTetBlockCoords(interm);
			}
			else {
				//reset the rotation and translation if player move is illegal
				fallingPiece.rotate(Rotate.NONE);
				fallingPiece.translate(Move.NONE);
			}
			interm = fallingPiece.fallingTetBlockCoords();
			fallingPiece.setAllTetBlockCoords(interm);
			//Step 2c
			Color colorOfPiece = fallingPiece.getTetColor();
			lightUpFallingPiece(colorOfPiece);
		}
		return true;
	}
	/**
	 * This helper function checks all Tiles one row down from the
	 * falling piece's blocks to see if they are lit. If there is
	 * at least one which is NOT part of the falling piece itself.
	 * the falling piece must stop moving.
	 * 
	 * @return true if piece should stop falling.
	 *         false otherwise
	 */
	private boolean pieceStopsFalling() {
		int[] currTetPos = fallingPiece.getTetBlockCoords();
		int[] oneRowDownVersion = new int[8];
		
		for (int i = 0; i < 8; i += 2) {
			oneRowDownVersion[i] = currTetPos[i] + 1;
			oneRowDownVersion[i + 1] = currTetPos[i + 1];
		}
		
		//check if the piece's block's next one-row-fall goes
		//past the bottom or into another lit square.
		for (int i = 0; i < 8; i += 2) {
			int rowOfBlock = oneRowDownVersion[i];
			int colOfBlock = oneRowDownVersion[i + 1];
			
			if (rowOfBlock >= BOARD_MAX_ROW) {
				return true;
			}
			
			if (board[rowOfBlock][colOfBlock].isLit() == true &&
			    fallingPiece.isCoordABlockCoord(rowOfBlock, colOfBlock) == false) {
				return true;
			}
		}
		return false;
		
	}
	/**
	 * This private helper function will save the state
	 * of the board once the falling Tetrimino stops moving.
	 * 
	 * This function also restricts the size of the stack of game states.
	 * @return Nothing
	 */
	private void saveState() {
		GameState currState = new GameState(this.board, this.fallingPiece);
		
		// remove the least recent game state (bottommost one)
		if (stack.size() == MAX_STACK_SIZE)
			stack.removeElementAt(0);
		
		stack.addElement(currState);
	}
	/**
	 * This helper function finds rows which are filled
	 * up and unlights them. Then, all lit blocks above
	 * are moved down.
	 * @return None
	 */
	private void checkForCompletedRows() {
		int numFilledRowsUnder= 0;
		
		//Iterate upward through the rows.
		for (int rowIter = BOARD_MAX_ROW - 1; rowIter > BOARD_MIN_ROW; rowIter--) {
			//counts how many tiles are lit on one row.
			int numLitTilesOnRow = Tile.numTilesLit(board[rowIter]);
			
			//short-circuit. There cannot possibly be any pieces further up the rows
			if (numLitTilesOnRow == 0)
				return;
	
			boolean isRowFilled = numLitTilesOnRow == NUM_ROW_TILES;
			//All flled rows get eliminated.
			if (isRowFilled) {
				for (int colIter = BOARD_MIN_COL; colIter < BOARD_MAX_COL; colIter++) {
					board[rowIter][colIter].setColor(Color.BLACK);
				}
				numFilledRowsUnder++;
			}
			//All lit blocks on non-filled rows above eliminated rows go down.
			if (numFilledRowsUnder > 0 && isRowFilled == false) {
				for (int colIter = BOARD_MIN_COL; colIter < BOARD_MAX_COL; colIter++) {
					Tile movingTile = board[rowIter + numFilledRowsUnder][colIter];
					//boolean wasOrigTileLit = board[rowIter][colIter].isLit();
					Color origTileColor = board[rowIter][colIter].getColor();
				
					movingTile.setColor(origTileColor);
					board[rowIter][colIter].setColor(Color.BLACK);
				}
			}
		} //end rowIter for loop
	}
	/**
	 * This helper function edits the board by light and unlighting
	 * certain tiles to animate a falling piece.
	 * 
	 * Notice through the other functions that once the piece
	 * stops moving, its lit blocks do not disappear.
	 * 
	 * @param color Color to light the tile with
	 */
	private void lightUpFallingPiece(Color color) {
		int[] currPiecePos = fallingPiece.getTetBlockCoords();
		for (int i = 0; i < 8; i += 2) {
			int row = currPiecePos[i];
			int col = currPiecePos[i + 1];
			
			board[row][col].setColor(color);
		}
	}
	/**
	 * This helper function checks if the new translation and
	 * rotation ordered by the Player wont run a part of the piece out of
	 * the boundaries.
	 * @return true if player move makes piece go out of bounds
	 * 		   false otherwise
	 */
	private boolean pieceRunsOutOfBounds() {
		//We have these values to reset fallingPiece to since we 
		//are editing fallingPiece for out of bounds detection
		int[] origTetPos = fallingPiece.getTetBlockCoords();
		Move origMove = fallingPiece.getTranslation();
		Rotate origRot = fallingPiece.getRotation();
		int origOrientationNum = fallingPiece.getOrientationNum();
		
		//Check if the translations or rotations after the fall don't 
		//make the piece run off the board.
		int[] newTetPos = fallingPiece.fallingTetBlockCoords();
		fallingPiece.setAllTetBlockCoords(newTetPos);
		newTetPos = fallingPiece.translatedTetBlockCoords();
		fallingPiece.setAllTetBlockCoords(newTetPos);
		newTetPos = fallingPiece.rotatingTetBlockCoords();
		
		boolean checker = false;
		for (int i = 0; i < 8; i += 2) {
			int newRow = newTetPos[i];
			int newCol = newTetPos[i + 1];
			if (newRow < BOARD_MIN_ROW || newRow >= BOARD_MAX_ROW 
				|| newCol < BOARD_MIN_COL || newCol >= BOARD_MAX_COL)
				checker = true;
		}
		//restore falling piece to its unedited form
		fallingPiece.setAllMembersOfPiece(origTetPos, origMove, origRot, origOrientationNum);
		return checker;
	}
	/**
	 * This helper function checks if the new translation and
	 * rotation ordered by the Player (after the tickTock() ends)
	 * is legal.
	 * @return true if player move causes collision and is illegal
	 * 		   false otherwise
	 */
	private boolean playerInputCausesCollision() {
		//We have these values to reset fallingPiece to since we 
		//are editing fallingPiece for collision detection
		int[] origTetPos = fallingPiece.getTetBlockCoords();
		Move origMove = fallingPiece.getTranslation();
		Rotate origRot = fallingPiece.getRotation();
		int origOrientationNum = fallingPiece.getOrientationNum();
		
		//Emulate the translations and rotations the player wanted
		int[] newTetPos = fallingPiece.fallingTetBlockCoords();
		fallingPiece.setAllTetBlockCoords(newTetPos);
		newTetPos = fallingPiece.translatedTetBlockCoords();
		fallingPiece.setAllTetBlockCoords(newTetPos);
		newTetPos = fallingPiece.rotatingTetBlockCoords();
		fallingPiece.setAllTetBlockCoords(newTetPos);
		newTetPos = fallingPiece.getTetBlockCoords();
		
		//Check if the translations or rotations are legal.
		boolean checker = false;
		for (int i = 0; i < 8; i += 2) {
			int newRow = newTetPos[i];
			int newCol = newTetPos[i + 1];
			//if there is already a lit block to where the falling
			//block wants to go, player input will cause collision.
			if (board[newRow][newCol].isLit() == true) {
				checker = true;
				break;
			}
		}
		//restore falling piece to its unedited form
		fallingPiece.setAllMembersOfPiece(origTetPos, origMove, origRot, origOrientationNum);
		return checker;
	}
	/**
	 * Modifies the board member to display a fail image.
	 * @return Nothing
	 */
	public void makeFailScreen() {
		//Blacken the board
		for (int row = 0; row < BOARD_MAX_ROW; row++) {
			for (int col = 0; col < BOARD_MAX_COL; col++) {
				board[row][col].setColor(Color.BLACK);
			}
		}
		//Make "F"
		board[1][4].setColor(Color.BLUE);
		board[1][5].setColor(Color.BLUE);
		board[2][4].setColor(Color.BLUE);
		board[3][4].setColor(Color.BLUE);
		//Make left half of "A"
		board[5][3].setColor(Color.PINK);
		board[6][3].setColor(Color.PINK);
		board[6][4].setColor(Color.PINK);
		board[7][3].setColor(Color.PINK);
		//Make right half of "A"
		board[5][4].setColor(Color.ORANGE);
		board[5][5].setColor(Color.ORANGE);
		board[6][5].setColor(Color.ORANGE);
		board[7][5].setColor(Color.ORANGE);
		//Make "I"
		board[9][4].setColor(Color.CYAN);
		board[10][4].setColor(Color.CYAN);
		board[11][4].setColor(Color.CYAN);
		board[12][4].setColor(Color.CYAN);
		//Make "L"
		board[14][4].setColor(Color.ORANGE);
		board[15][4].setColor(Color.ORANGE);
		board[16][4].setColor(Color.ORANGE);
		board[16][5].setColor(Color.ORANGE);
	}
	/**
	 * Checker if the game is paused
	 * @return true if the game is paused
	 * 		   false otherwise
	 */
	public boolean isGamePaused() {
		return paused;
	}
	/**
	 * Setter for member paused
	 * @param toPause boolean that states if the
	 * 				  game should be paused
	 */
	public void setGameToPause(boolean toPause) {
		paused = toPause;
	}
	/**
	 * This saves the game to savefile.txt.
	 * If savefile.txt doesn't exist, it is created.
	 * @return Nothing
	 */
	public void saveGame() {
		//Use of a Board State to reset position of the falling piece
		//for the save.
		GameState toSave = new GameState(this.board, this.fallingPiece);
		Tile[][] boardToSave = toSave.getSavedBoard();
		Tetrimino pieceToSave = toSave.getSavedFallingPiece();
		
        try {
        	//open savefile.txt for writing
            FileWriter fileWriter = new FileWriter(FILENAME);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            //saves board state to file
            for (int row = BOARD_MIN_ROW; row < BOARD_MAX_ROW; row++) {
            	Tile[] boardRow = boardToSave[row];
            	String stringRepOfRow = Tile.rowOfBoardToString(boardRow);
            	
            	bufferedWriter.write(stringRepOfRow);
            	bufferedWriter.newLine();
            }
            //saves name of falling piece to file
            bufferedWriter.write(pieceToSave.toString());
            bufferedWriter.newLine();
            //close savefile.txt
            bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println("Error writing to file '" + FILENAME + "'");
        }
	}
	/**
	 * This loads a game from Tetris
	 * @return Nothing
	 */
	public void loadGame() {
        String line = null;

        try {
        	//open savefile.txt for reading
            FileReader fileReader = new FileReader(FILENAME);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
 
            //loads board row by row from string representation
            //on savefile.txt
            for (int row = BOARD_MIN_ROW; row < BOARD_MAX_ROW; row++) {
            	line = bufferedReader.readLine();
            	Tile[] boardRow = Tile.stringToRowOfBoard(line);
            	//Initialize baord member row by row.
            	board[row] = boardRow;
            }
            //loads falling piece from string representation
            //on savefile.txt
            line = bufferedReader.readLine();
            char charRepOfPiece = line.charAt(0);
            fallingPiece = Tetrimino.charToTetrimino(charRepOfPiece);

            //close savefile.txt
            bufferedReader.close();            
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + FILENAME + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + FILENAME + "'");
        }
	}
	/**
	 * This undoes a move in the Tetris Game by reverting
	 * to the previous falling piece and to the
	 * board state without the lit tiles said falling piece
	 * inhabited when it stopped falling.
	 * @return boolean if undo was successful.
	 */
	public boolean undo() {
		if (stack.isEmpty())
			return false;
		
		//Pops game state off of stack.
		GameState prevState = stack.lastElement();
		stack.removeElementAt(stack.size() - 1);
		
		Tile[][] prevBoard = prevState.getSavedBoard();
		Tetrimino prevPiece = prevState.getSavedFallingPiece();
		
		//Restores board to previous state
		for (int row = BOARD_MIN_ROW; row < BOARD_MAX_ROW; row++) {
			for (int col = BOARD_MIN_COL; col < BOARD_MAX_COL; col++) {
				this.board[row][col] = prevBoard[row][col];
			}
		}
		//Restores falling piece to previous state
		this.fallingPiece = prevPiece;
		
		return true;
	}
	/**
	 * Checker if we can undo or not but analyzing the
	 * stack member.
	 * @return true if undo is possible
	 * 		   false otherwise
	 */
	public boolean canWeUndo() {
		return stack.isEmpty() == false;
	}
	/**
	 * This function returns the gameover member.
	 * @return boolean value of gameover member
	 */
	public boolean isGameOver() {
		return gameover;
	}
	/**
	 * This function returns the fallingPiece member.
	 * This return will allow the GUI to access the fallingPiece
	 * has to do translation and rotation of it
	 * @return boolean value of fallingPiece member
	 */
	public Tetrimino getFallingPiece() {
		return fallingPiece;
	}
	/**
	 * This function returns the board member.
	 * This return will allow the GUI to get a display
	 * of the board and allow for deeper testing.
	 * @return Tile[][] board member
	 */
	public Tile[][] getBoard() {
		return board;
	}
	/**
	 * Static Function that checks if we can load a game
	 * by attempting to load savefile.txt.
	 * @return true if load is possible.
	 * 		   false otherwise.
	 */
	public static boolean canWeLoad() {
        try {
            //Open savefile.txt for reading.
            FileReader fileReader = new FileReader(FILENAME);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //If savefile.txt was opened, close it.
            bufferedReader.close();  
            return true;
        }
        catch(FileNotFoundException ex) {
            //If savefile.txt doesn't exist, that means we didn't save.
        	//So we cannot load a savefile that doesn't exist
        	return false;
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + FILENAME + "'");
            return false;
        }
	}
}
