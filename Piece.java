public class Piece {

	private final int color;
	private final int type;
	private boolean moved;
	private boolean dead;

	public Piece(int color, int type) {
		this.color = color;
		this.type = type;
		this.moved = false;
		this.dead = false;
	}

	public Piece(int color, int type, boolean moved) {
		this.color = color;
		this.type = type;
		this.moved = moved;
		this.dead = false;
	}

	public int color() {
		return this.color;
	}

	public int type() {
		return this.type;
	}

	public boolean moved() {
		return this.moved;
	}

	public void setMoved() {
		this.moved = true;
	}

	public boolean dead() {
		return this.dead;
	}

	public void setDead() {
		this.dead = true;
	}

	public Piece copy() {
		return new Piece(this.color, this.type, this.moved);
	}

	public Piece copy(boolean moved) {
		return new Piece(this.color, this.type, true);
	}

}
