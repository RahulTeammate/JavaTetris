package game.tetris.gui;
import game.tetris.lib.Game;
import game.tetris.lib.Tile;
import game.tetris.lib.pieces.*;

import java.util.Observable;
import java.util.Vector;

/**
 * TetrisModel is the Model of the Tetris GUI,
 * where the Tetris Game runs with no graphical work.
 * 
 * However, since TetrisModel extends Observable, it
 * can notify all subclasses of Observer, one of them being TetrisView,
 * when a graphical update needs to be done.
 * 
 * @author Rahul
 *
 */
public class TetrisModel extends Observable {
	//Tetris Game Member
	private Game tetrisGame;
	//Public as to be accesed by TetrisController
	public Tetrimino controlledPiece;
	
	/**
	 * Default Constructor of a TetrisModel
	 */
	public TetrisModel() {
		tetrisGame = null;
		//init isn't called here because there are no observers of the TetrisModel (In TetrisGUI)
		//when it is constructed.
		//TetrisGUI adds the observer later; only TetrisGUI can add observers
	}
	/**
	 * Function which initializes the menu options
	 * of the view when a game isn't running on the TetrisModel.
	 * 
	 * This situation occurs when the GUI is initially loaded
	 * up or a Game Over occurs.
	 * @return Nothing
	 */
	public void setUpViewForNewGame() {
		//If we cannot load due to lack of a save file,
		//set the loadGame menu option to
		//be disabled by updating the menu in TetrisView.
		boolean enableLoad = Game.canWeLoad();
		//Enable newGame. Disable pause, undo, and saveGame.
		//The reason pause, undo, and saveGame are disabled is because
		//no game exists for these options to be used.
		menuBarBroadcast(true, enableLoad, false, false, false);
	}
	/**
	 * Function which starts a new Tetris Game.
	 * @return Nothing
	 */
	public void newGame() {
		tetrisGame = new Game();
		updateControlledPiece();
		//disable newGame, loadGame, undo, and saveGame. Enable pause.
		menuBarBroadcast(false, false, true, false, false);
	}
	/**
	 * Function that runs one frame of the Tetris
	 * Game.
	 * @return Nothing
	 */
	public void runOneFrame() {
		if (tetrisGame.isGameOver()) {
			gameOverOccurs();
		}
		else {
			tetrisGame.tickTock();
			updateControlledPiece();
			boardBroadcast();
		}
	}
	/**
	 * Private helper function that runs all that
	 * occurs when a Tetris Game is Over.
	 * @return Nothing
	 */
	private void gameOverOccurs() {
		setUpViewForNewGame();
		tetrisGame.makeFailScreen();
		boardBroadcast();
	}
	/**
	 * Checker if the Tetris Game is over
	 * @return true if member tetrisGame has game overed.
	 * 		   false otherwise
	 */
	public boolean isGameOver() {
		return tetrisGame.isGameOver();
	}
	/**
	 * Function that updates member controlledPiece
	 * to the currently falling piece in the Tetris Game after
	 * every frame.
	 * @return Nothing
	 */
	public void updateControlledPiece() {
		controlledPiece = tetrisGame.getFallingPiece();
	}
	/**
	 * Function that undoes a move in the Tetris Game.
	 * @return Nothing
	 */
	public void undo() {
		tetrisGame.undo();
		//the board changed through the undo, so we need
		//to update the board in TetrisView.
		boardBroadcast();
		//If we cannot undo any more, set the undo menu option to
		//be disabled by updating the menu in TetrisView.
		boolean enableUndo = tetrisGame.canWeUndo();
		//disable newGame, loadGame. Enable pause and save.
		menuBarBroadcast(false, false, true, enableUndo, true);
	}
	/**
	 * Function that saves a Tetris Game.
	 * @return Nothing
	 */
	public void saveGame() {
		tetrisGame.saveGame();
	}
	/**
	 * Function that pauses the Tetris Game.
	 * @return Nothing
	 */
	public void pause() {
		tetrisGame.setGameToPause(true);
		//If we cannot undo, set the undo menu option to
		//be disabled by updating the menu in TetrisView.
		boolean enableUndo = tetrisGame.canWeUndo();
		//disable newGame, loadGame. Enable pause and save.
		menuBarBroadcast(false, false, true, enableUndo, true);
	}
	/**
	 * Function that unpauses the Tetris Game.
	 * @return Nothing
	 */
	public void unpause() {
		tetrisGame.setGameToPause(false);
		//disable newGame, loadGame, undo, and saveGame. Enable pause.
		menuBarBroadcast(false, false, true, false, false);
	}
	/**
	 * Function that checks if Tetris Game is paused.
	 * @return true if Game class is paused.
	 * 		   false otherwise
	 */
	public boolean isPaused() {
		return tetrisGame.isGamePaused();
	}
	/**
	 * Function that loads a saved Tetris Game from
	 * savefile.txt.
	 * @return Nothing
	 */
	public void loadGame() {
		//To erase the current game for the new load.
		tetrisGame = new Game(); 
		tetrisGame.loadGame();
		updateControlledPiece();
		//disable newGame, loadGame, undo, and saveGame. Enable pause.
		menuBarBroadcast(false, false, true, false, false);
	}
	/**
	 * Function which broadcasts to TetrisView 
	 * a 2D Tile array for updating the board there.
	 * @return Nothing
	 */
	private void boardBroadcast() {
		//This function will notify the sole observer TetrisView that something's going to be broadcasted to it.
		setChanged();
		
		Tile[][] broadcast = tetrisGame.getBoard();
		
		//The broadcast happens. TetrisView will get a 2D Tile array to update the board.
		notifyObservers(broadcast);
	}
	/**
	 * This function broadcasts a boolean[] to TetrisView
	 * @param newGameEnabled boolean
	 * @param saveEnabled boolean
	 * @param pauseEnabled boolean
	 * @param undoEnabled boolean
	 * @param saveEnabled boolean
	 * @return Nothing
	 */
	private void menuBarBroadcast(boolean newGameEnabled, boolean loadEnabled, boolean pauseEnabled,
								  boolean undoEnabled, boolean saveEnabled) {
		//This function will notify the sole observer TetrisView that something's going to be broadcasted to it.
		setChanged();
		
		boolean[] broadcast = new boolean[5];
		broadcast[0] = newGameEnabled;
		broadcast[1] = loadEnabled;
		broadcast[2] = pauseEnabled;
		broadcast[3] = undoEnabled;
		broadcast[4] = saveEnabled;
		
		//The broadcast happens. TetrisView will get boolean[] status to 
		//update the enabl-ility of menu items.
		notifyObservers(broadcast);
	}
}
