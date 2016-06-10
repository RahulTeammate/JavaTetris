package game.tetris.lib.pieces;
/**
 * Enum Move is used for Tetrimino translation
 * LEFT = tetrimino translates left
 * RIGHT = tetrimino translates right
 * NONE = do nothing to tetrimino
 * 
 * @author Rahul
 *
 */
public enum Move {
	LEFT, RIGHT, NONE;
	/**
	 * 
	 * @return int version of the enum's value
	 */
	public int toInt() {
		if (this == Move.LEFT) {
			return -1;
		}	
		else if (this == Move.RIGHT) {
			return +1;
		}
		else {
			return 0;
		}
	}
}
