public interface ChessModelObject {

	// Getters
	int state();
	int[][] getBoard();
	int getTurn();
	boolean getCheck();

	// Actions
	void runInput(String input);

}
