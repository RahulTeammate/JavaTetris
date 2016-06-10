package game.tetris.gui;
/**
 * TetrisGUI is the Graphical Application
 * of my game.tetris.lib using MVC Architecture
 * 
 * @author Rahul
 *
 */
public class TetrisGUI {
	
	private TetrisModel model;
	private TetrisView view;
	private TetrisController controller;
	
	/**
	 * Default Constructor of a TetrisGUI
	 */
	public TetrisGUI() {
		model = new TetrisModel();
		view = new TetrisView();
		
		//model's notifyObserver(broadcast) calls will now notify view, an Observable.
		model.addObserver(view);
		
		//Controller can now access and modify model and view.
		controller = new TetrisController(model,view);
		
		//Initializes the loadup state of the game.
		controller.initModel();
		//the view now knows what controller is seeing it
		view.addController(controller);
	}
	/**
	 * Main method which loads the Tetris Game
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new TetrisGUI();
			}
		});
		// Credit to http://stackoverflow.com/questions/12077245/what-is-swingutilities-invokelater
		// Stephen Halm explained that this was the correct way to create a GUI.
	}

}
