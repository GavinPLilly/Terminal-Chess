// Locomotion move = LMove
public class LMove extends Move {

	final private Piece piece;
	final private Coord start;
	final private Coord end;

	public LMove(int turn, Piece piece, Coord start, Coord end) {
		super(0, turn);
		this.piece = piece.copy(true); // make sure the piece has been set to moved
		this.start = start.copy();
		this.end = end.copy();
	}

	public Piece piece() {
		return this.piece;
	}

	public Coord start() {
		return this.start;
	}

	public Coord end() {
		return this.end;
	}

}
