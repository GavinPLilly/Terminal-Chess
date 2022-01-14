// Spawning move = SMove
public class SMove extends Move {

	final private Piece piece;
	final private Coord spot;

	public SMove(int turn, piece Piece, Coord spot) {
		super(2, turn);
		this.piece = Piece.copy();
		this.spot = spot.copy();
	}

	public Piece piece() {
		return this.piece;
	}

	public Coord spot() {
		return this.spot;
	}

}
