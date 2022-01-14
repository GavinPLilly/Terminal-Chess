public class Coord {
	public int x;
	public int y;

	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Coord copy() {
		return new Coord(this.x, this.y);
	}

	public boolean equals(Coord other) {
		return this.x == other.x && this.y == other.y;
	}
}
