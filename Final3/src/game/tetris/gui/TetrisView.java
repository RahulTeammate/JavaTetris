package game.tetris.gui;
import game.tetris.lib.Tile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import java.awt.Color;

/**
 * TetrisView is the View of the Tetris GUI,
 * where all the graphical work occurs.
 * 
 * However, since TetrisView extends Observer, it
 * can be notified by all subclasses of Observable, one of them being TetrisModel,
 * where the Tetris Game runs.
 * 
 * @author Rahul
 *
 */
public class TetrisView implements Observer {

	private JLabel[][] board;
	
	//View Hierarchy Members.
	//screen is public as to be accessed in TetrisModel.
	public JFrame screen; 
	private JPanel holderOfAll;
	private JPanel holderOfBoard;
	
	//Constants of the Tetris Board boundaries
	private static final int BOARD_MIN_ROW = 0;
	private static final int BOARD_MAX_ROW = 20;
	private static final int BOARD_MIN_COL = 0;
	private static final int BOARD_MAX_COL = 10;
	
	//Constants of Views
	private static final int WINDOW_HEIGHT = 640;
	private static final int WINDOW_WIDTH = 320;
	
	private TetrisController controller;
	
	//all these JMenuItem members are public as to be accessed in TetrisController
	public JMenuItem newGame;
	public JMenuItem undo;
	public JMenuItem pause;
	public JMenuItem saveGame;
	public JMenuItem loadGame;
	
	/**
	 * Default Constructor for a TetrisView
	 */
	public TetrisView() {
		board = new JLabel[BOARD_MAX_ROW][BOARD_MAX_COL];
		
		screen = new JFrame("Tetris Game");
		screen.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		//You cannot resize the TetrisView
		screen.setResizable(false);
	
		//Hierarchy: holderOfAll (Border) --> holderOfBoard (Grid) -->20x10 JLabels
		holderOfAll = new JPanel();
		holderOfAll.setLayout(new BorderLayout());
		holderOfBoard = new JPanel();
		holderOfBoard.setLayout(new GridLayout(BOARD_MAX_ROW, BOARD_MAX_COL));
		
		buildMenuBar();
		buildBoard();
		
		//Adds JPanel holderOfAll to the TetrisView
		screen.add(holderOfBoard);
		//Allows the TetrisView to be seen
		screen.setVisible(true);
		//Enable Red Exit button.
		screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/**
	 * This helper function creates the TetrisView Menu Bar
	 * @return Nothing
	 */
	private void buildMenuBar() {
		JMenuBar menubar = new JMenuBar();
		
		JMenu gameStart = new JMenu("Game Start");
		newGame = new JMenuItem("New Game");
		loadGame = new JMenuItem("Load Game");
		
		gameStart.add(newGame);
		gameStart.add(loadGame);
		menubar.add(gameStart);
		
		JMenu options = new JMenu("Options");
		
		pause = new JMenuItem("Pause/Unpause");
		undo = new JMenuItem("Undo move");
		saveGame = new JMenuItem("Save Game");
		
		options.add(pause);
		options.add(undo);
		options.add(saveGame);
		menubar.add(options);
		
		screen.setJMenuBar(menubar);
	}
	/**
	 *  This helper function initializes the holderOfBoard
	 *  that was created in the constructor to hold 20x10 JLabels
	 *  @return Nothing
	 */
	private void buildBoard() {
		for (int row = BOARD_MIN_ROW; row < BOARD_MAX_ROW; row++) {
			for (int col = BOARD_MIN_COL; col < BOARD_MAX_COL; col++) {
				JLabel currTile = new JLabel();
				//Alternating color algorithm for a Tetris Board view
				currTile.setOpaque(true);
				currTile.setBackground(Color.BLACK);
				
				holderOfBoard.add(currTile);
				//We also fill up the 2D 20x10 JPanel Array as well.
				//The reason we do that is so we could refer to a JPanel
				//inside the holderOfBoard via that Array.
				board[row][col] = currTile;
			}
		}
		//Adds JPanel that holds the Tetris tiles in a grid to the 
		//main JPanel holderOfAll in the center.
		holderOfAll.add(holderOfBoard, BorderLayout.CENTER);
	}
	/**
	 * Takes a 2D Tile Array and makes each element of
	 * said 2D Tile Array the color of each element of
	 * the 2D JLabel Array.
	 * @param input 2D Tile Array
	 * @return Nothing
	 */
	public void updateBoard(Tile[][] input) {
		for (int row = BOARD_MIN_ROW; row < BOARD_MAX_ROW; row++) {
			for (int col = BOARD_MIN_COL; col < BOARD_MAX_COL; col++) {
				JLabel elemTile = board[row][col];
				elemTile.setOpaque(true);
				
				Color colorOfTile = input[row][col].getColor();
				elemTile.setBackground(colorOfTile);
			}
		}
	}
	/**
	 * Gathers broadcasted information from TetrisModel, an Observable,
	 * and does different things to it depending on what instanceof it is.
	 * @param modelInstr Observable class that is sending information.
	 * @param arg Information being sent from modelInstr
	 * @return Nothing
	 */
	@Override
	public void update(Observable modelInstr, Object arg) { 
		if (!(modelInstr instanceof TetrisModel)) {
			return;
		}
		
		if (arg instanceof Tile[][]) {
			updateBoard((Tile[][])arg);
		}
		else if (arg instanceof boolean[]) {
			boolean[] menuEnables = (boolean[])arg;
			newGame.setEnabled(menuEnables[0]);
			loadGame.setEnabled(menuEnables[1]);
			pause.setEnabled(menuEnables[2]);
			undo.setEnabled(menuEnables[3]);
			saveGame.setEnabled(menuEnables[4]);
		}
	}
	/**
	 * Adds a TetrisController.
	 * This will allow actionPerformed() function calls
	 * in TetrisController to work.
	 * @param controller TetrisController instance
	 */
	public void addController(TetrisController controller) {
		//All the event handlers in the controller don't do anything until you set this up.
		this.controller = controller;
		newGame.addActionListener(controller);
		loadGame.addActionListener(controller);
		pause.addActionListener(controller);
		undo.addActionListener(controller);
		saveGame.addActionListener(controller);
		screen.addKeyListener(controller);
	}
}
