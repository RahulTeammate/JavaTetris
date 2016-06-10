package game.tetris.gui;
import game.tetris.lib.pieces.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

/**
 * 
 * TetrisController is the Controller of the Tetris GUI,
 * an interface for the user to interact with the view via Actions.
 * 
 * Actions are used for choose menu options.
 * 
 * @author Rahul
 *
 */
public class TetrisController implements ActionListener, MouseListener, KeyListener {
	private static final int ONE_SECOND = 500;
	
	//Members model and view, for getting info and modifying.
	private TetrisModel model;
	private TetrisView view;
	//Member timer, for events that activate without user input
	private Timer timer;
	
	/**
	 * Default Construction of a Controller that will use a view and model.
	 * @param model - controller will use menu options
	 * @param view - controller will enable or disable menu options
	 */
	public TetrisController(TetrisModel model, TetrisView view) {
		this.model = model;
		this.view = view;
		
		this.timer = new Timer(ONE_SECOND, 
			/**
			 * This function is an ActionListener that will 
			 * activate when member timer goes off.
			 * @return Nothing
			 */
			new ActionListener() {
		    	public void actionPerformed(ActionEvent event) {
		    		//runOneFrame must not be run now that the game is over.
		    		if (model.isGameOver()) {
		    			timer.stop();
		    		}
		    		model.runOneFrame();
		        }    
		    });
	}
	/**
	 * Function that initializes the Observerables- 
	 * like TetrisModel instances- when they are
	 * first created.
	 * 
	 * This function results in the connected Observers-
	 * like TetrisView instances- having their menu options
	 * set to only allow a new game or a load of a saved game.
	 * @return Nothing
	 */
	public void initModel() {
		model.setUpViewForNewGame();
	}
	/**
	 * Function that activates if a key is pressed.
	 * It will translate or rotate the falling piece.
	 * @param event KeyEvent with info of which keyboard key was pressed.
	 * @return Nothing
	 */
	@Override
	public void keyPressed(KeyEvent event) {
		//This prevents tickTock from activating until the translation and rotation is set.
		//That way, tickTock won't erase the inputted translation or rotation.
		timer.stop();
		 if(event.getKeyCode() == KeyEvent.VK_LEFT) {
			 model.controlledPiece.translate(Move.LEFT);
		 }
		 else if(event.getKeyCode() == KeyEvent.VK_RIGHT) {
			 model.controlledPiece.translate(Move.RIGHT);
		 }
		 else if(event.getKeyCode() == KeyEvent.VK_UP) {
			 model.controlledPiece.rotate(Rotate.CW);
		 }
		 timer.start();
	}
	/**
	 * Function that activates if menu items "New Game", "Load Game", 
	 * "Pause", "Undo", or "Save Game" are chosen
	 * @param event ActionEvent with info of which menu option was chosen.
	 * @return Nothing
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		//Choose "New Game"
		if (event.getSource().equals(view.newGame)) {
			model.newGame();
			timer.start();
		}
		//Choose "Load Game"
		else if (event.getSource().equals(view.loadGame)) {
			model.loadGame();
			timer.start();
		}
		//Choose "Pause"
		else if (event.getSource().equals(view.pause)) {
			//Pause or unpause, depending on what state the game is in.
			if (model.isPaused()) {
				model.unpause();
				timer.start();
			}
			else {
				model.pause();
				timer.stop();
			}
		}
		//Choose "Undo"
		else if (event.getSource().equals(view.undo)) {
			model.undo();
		}
		//Choose "Save Game"
		else if (event.getSource().equals(view.saveGame)) {
			model.saveGame();
		}
	}
	/**
	 * Unused
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		return;
	}
	/**
	 * Unused
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		return;
	}
	/**
	 * Unused
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		return;
	}
	/**
	 * Unused
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		return;
	}
	/**
	 * Unused
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		 
	}
	/**
	 * Unused
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		return;
	}
	/**
	 * Unused
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		return;
	}

	
}
