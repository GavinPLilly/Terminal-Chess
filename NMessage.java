// Normal Message = NMessage
// Passed for when a valid move has been entered and the board has
// 	been updated and the turn has been changed 
public class NMessage extends Message {

	int[][] board;
	int newTurn;
	boolean check;

	public NMessage(int[][] board, int newTurn, boolean check) {
		super(0);
		this.board = board;
		this.newTurn = newTurn;
		this.check = check;
	}

	public int[][] board() {
		return this.board;
	}

	public int newTurn() {
		return this.newTurn;
	}

	public boolean check() {
		return this.check;
	}

}
