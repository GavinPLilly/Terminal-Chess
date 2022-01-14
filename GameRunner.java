public class GameRunner {
	
	Reader reader;
	Chess chess;
	Printer printer;

	public GameRunner(boolean invertedColors) {
		this.reader = new Reader();
		this.chess = new Chess();
		this.printer = new Printer(invertedColors);
	}

	public void runGame() {
		Message m1;
		Message m2;
		this.printer.print(this.chess.runInput(new PBMessage()));
		while(this.chess.isGameOver() == false) {
			// get message from reader
			m1 = this.reader.getInput();
			// pass this message to chess and get a view message
			m2 = this.chess.runInput(m1);
			// pass this view message to print output
			this.printer.print(m2);
		}
	}

	public static void main(String[] args) {
		GameRunner gameRunner = new GameRunner(true);
		gameRunner.runGame();
	}

}
