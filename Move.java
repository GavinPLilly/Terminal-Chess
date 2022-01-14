public class Move {

	final int moveType;
	final int turn;
	
	public Move(int moveType, int turn) {
		this.moveType = moveType;
		this.turn = turn;
	}

	public int moveType() {
		return this.moveType;
	}

	public int turn() {
		return this.turn;
	}

}
