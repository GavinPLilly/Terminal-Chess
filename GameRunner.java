// Controller class for the model-view-controller chess system

public class GameRunner {

	// Model state values
	final int NEW_BOARD_READY = 0; // new board ready
	final int GAME_OVER = 1; // game over
	final int GET_MOVE_INPUT = 2; // get move input
	final int GET_PP_INPUT = 3; // get pawn promo input
	final int INVALID_SYNTAX = 4; // invalid syntax entered
	final int INVALID_MOVE = 5; // general invalid move state
	final int INVALID_MOVEMENT = 6; // coords entered are invalid
	final int CANNOT_MOVE_CHECK = 7;  // invalid move due to moving into check
	final int INTERNAL_ERROR = 8; // general internal error state

	Chess model;
	ChessViewObject view;

	public GameRunner() {
		this.model = new Chess();
		this.view = new Printer();
		this.model.init(this);
		this.view.init(this);
	}

	public void runGame() {
		int state;
		String input;
		while(true) {
			state = this.model.state();
			if(state == NEW_BOARD_READY) {
				this.view.dispBoard(getBoard(), getTurn(), getCheck());
				continue;
			}
			if(state == GAME_OVER) {
				this.view.dispGameOver(getBoard(), getTurn());
				return;
			}
			if(state == GET_MOVE_INPUT) {
				input = this.view.getMoveInput();
				this.model.runInput(input);
				continue;
			}
			if(state == GET_PP_INPUT) {
				input = this.view.getPawnPromoInput();
				this.model.runInput(input);
				continue;
			}
			if(state == INVALID_SYNTAX) {
				this.view.dispInvalidSyntax();
				input = this.view.getMoveInput();
				this.model.runInput(input);
				continue;
			}
			if(state == INVALID_MOVE) {
				this.view.dispInvalidMove();
				input = this.view.getMoveInput();
				this.model.runInput(input);
				continue;
			}
			if(state == INVALID_MOVEMENT) {
				this.view.dispInvalidMovement();
				input = this.view.getMoveInput();
				this.model.runInput(input);
				continue;
			}
			if(state == CANNOT_MOVE_CHECK) {
				this.view.dispCannotMoveCheck();
				this.view.getMoveInput();
				this.model.runInput(input);
				continue;
			}
			if(state == INTERNAL_ERROR) {
				this.view.dispInternalErro();
				return;
			}
		}
	}

	public int[][] getBoard() {
		return this.model.getBoard();
	}

	public int getTurn() {
		return this.model.getTurn();
	}

	public boolean getCheck() {
		return this.model.getCheck();
	}

	public static void main(String[] args) {
		GameRunner gamerunner = new GameRunner();
		gamerunner.runGame();
	}
}
