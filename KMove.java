// Killing Move = KMove
public class KMove extends Move {

	private final Piece piece;
	private final Coord spot;

	public KMove(int turn, Piece piece, Coord spot) {
		super(1, turn);
		this.piece = piece.copy();
		this.spot = spot.copy();
	}

	public Piece piece() {
		return this.piece;
	}

	public Coord spot() {
		return this.spot;
	}

}
