// Game Over Message = GO Message
public class GOMessage extends Message{

	private int[][] board;
	private int winner;

	public GOMessage(int[][] board, int winner) {
		super(2);
		this.board = board;
		this.winner = winner;
	}

	public int[][] board() {
		return this.board;
	}

	public int winner() {
		return this.winner;
	}

}
