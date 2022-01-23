public interface ChessViewObject {

	// Getters
	String getInput();

	// Display methods
	void dispMakeMove();
	void dispPawnPromo();
	void dispBoard(int[][] board, int turn, boolean check);
	void dispGameOver(int[][] board, int winner);
	void dispInvalidSyntax();
	void dispInvalidMove();
	void dispInvalidMovementPath();
	void dispCannotMoveCheck();
	void dispInternalError();

}
