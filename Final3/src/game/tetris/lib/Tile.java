package game.tetris.lib;
import java.awt.Color;

/**
 * One Tile out of 20x10 on a Tetris board.
 * It can be lit or unlit.
 * 
 * @author Rahul
 *
 */
public class Tile {
	private static final int SIZE_OF_ROW = 10;
	
	private static final String CYAN_STR = "C";
	private static final String BLUE_STR = "B";
	private static final String ORANGE_STR = "O";
	private static final String YELLOW_STR = "Y";
	private static final String GREEN_STR = "G";
	private static final String PINK_STR = "P";
	private static final String RED_STR = "R";
	private static final String BLACK_STR = "X";
	
	private Color tileColor;
	
	/**
	 * Default (and only) Constructor.
	 */
	public Tile() {
		tileColor = Color.BLACK;
	}
	/**
	 * Setter for member tileColor
	 * @param color Color to set member to.
	 * @return None
	 */
	public void setColor(Color color) {
		tileColor = color;
	}
	/**
	 * Getter for member tileColor
	 * @return tileColor member
	 */
	public Color getColor() {
		return tileColor;
	}
	/**
	 * Checker for member tileColor
	 * @return true if tileColor isn't black.
	 * 		   false otherwise.
	 */
	public boolean isLit() {
		if (tileColor != Color.BLACK)
			return true;
		else
			return false;
	}
	
	/**
	 * This static function counts how many Tiles within the
	 * Tile array are lit.
	 * @param array Tile array to count lit tiles from
	 * @return int which his how many tiles were lit.
	 */
	public static int numTilesLit(Tile[] array) {
		int numLit = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i].isLit())
				numLit++;
		}
		return numLit;
	}
	/**
	 * This static function returns an String representation
	 * of a row of a board
	 * @param array Tile array representing a row of the board
	 * @return String representation of a row of the board
	 */
	public static String rowOfBoardToString(Tile[] rowOfBoard) {
		StringBuffer buffer = new StringBuffer();
		
		for (int i = 0; i < SIZE_OF_ROW; i++) {
			Tile currTile = rowOfBoard[i];
			
			//Appends a corresponding unique character for each
			//tile color of the Tiles within this row.
			if (currTile.getColor() == Color.CYAN)
				buffer.append(CYAN_STR);
			else if (currTile.getColor() == Color.BLUE)
				buffer.append(BLUE_STR);
			else if (currTile.getColor() == Color.ORANGE)
				buffer.append(ORANGE_STR);
			else if (currTile.getColor() == Color.YELLOW)
				buffer.append(YELLOW_STR);
			else if (currTile.getColor() == Color.GREEN)
				buffer.append(GREEN_STR);
			else if (currTile.getColor() == Color.PINK)
				buffer.append(PINK_STR);
			else if (currTile.getColor() == Color.RED)
				buffer.append(RED_STR);
			else
				buffer.append(BLACK_STR);
		}
		return buffer.toString();
	}
	/**
	 * This static function returns a heap-allocated 
	 * Tile array representing a row of a board given 
	 * the string representation of the board.
	 * @param repOfRow String representation of a row of the board.
	 * @return Tile array representing a row of the board.
	 */
	public static Tile[] stringToRowOfBoard(String repOfRow) {
		Tile[] rowOfBoard = new Tile[SIZE_OF_ROW];
		
		for (int i = 0; i < SIZE_OF_ROW; i++) {
			char charRepOfTile = repOfRow.charAt(i);
			
			//Changes the color of the row Tile based
			//on one character of the string representation 
			//of the row
			rowOfBoard[i] = new Tile();
			if (charRepOfTile == 'C')
				rowOfBoard[i].setColor(Color.CYAN);
			else if (charRepOfTile == 'B')
				rowOfBoard[i].setColor(Color.BLUE);
			else if (charRepOfTile == 'O')
				rowOfBoard[i].setColor(Color.ORANGE);
			else if (charRepOfTile == 'Y')
				rowOfBoard[i].setColor(Color.YELLOW);
			else if (charRepOfTile == 'G')
				rowOfBoard[i].setColor(Color.GREEN);
			else if (charRepOfTile == 'P')
				rowOfBoard[i].setColor(Color.PINK);
			else if (charRepOfTile == 'R')
				rowOfBoard[i].setColor(Color.RED);
			else
				rowOfBoard[i].setColor(Color.BLACK);
		}
		return rowOfBoard;
	}
}
