public class Printer {

	final int WHITE = 1;
	final int BLACK = -1;

	boolean invertedColors;

	public Printer(boolean invertedColors) {
		this.invertedColors = invertedColors;
	}

	public void print(Message message) {
		int messageType = message.messageType();
		// Invalid input syntax case
		if(messageType == 17) {
			invalidSyntax();
			return;
		}
		// Invalid move shape case
		if(messageType == 1) {
			invalidMoveShape();
			return;
		}
		// Moved into check case
		if(messageType == 3) {
			movedIntoCheck();
			return;
		}
		// Normal move case
		if(messageType == 0) {
			NMessage nm = (NMessage)message; // cast to specific type
			normalMove(nm.board(), nm.newTurn(), nm.check());
			return;
		}
		// Game over case
		if(messageType == 2) {
			GOMessage gm = (GOMessage)message; //cast to specific type
			gameOver(gm.board(), gm.winner());
			return;
		}
		// Internal error case
		if(messageType == 99) {
			internalError();
			return;
		}
		internalError();
	}

	private void printBoard(int[][] board) {
		for(int y = 7; y >= 0; y--) {
			System.out.println("---------------------------------");
			for(int x = 0; x < 8; x++) {
				System.out.print("| ");
				printPiece(board[x][y]);
				System.out.print(" ");
			}
			System.out.println("|");
		}
		System.out.println("---------------------------------");
	}

	private void printPiece(int piece) {
		if(this.invertedColors) {
			printPieceInverted(piece);
			return;
		}
		printPieceNormal(piece);
	}

	private void printPieceNormal(int piece) {
		switch(piece) {
			// White pieces
			case 0:
				System.out.print(" ");
				break;
			case 1:
				System.out.print("♔");
				break;
			case 2:
				System.out.print("♕");
				break;
			case 3:
				System.out.print("♗");
				break;
			case 4:
				System.out.print("♘");
				break;
			case 5:
				System.out.print("♖");
				break;
			case 6:
				System.out.print("♙");
				break;
			case 7:
				System.out.print(" ");
				break;
			// Black pieces
			case 8:
				System.out.print("♚");
				break;
			case 9:
				System.out.print("♛");
				break;
			case 10:
				System.out.print("♝");
				break;
			case 11:
				System.out.print("♞");
				break;
			case 12:
				System.out.print("♜");
				break;
			case 13:
				System.out.print("♟︎");
				break;
			case 14:
				System.out.print(" ");
				break;
		}
	}

	private void printPieceInverted(int piece) {
		switch(piece) {
			// White pieces
			case 0:
				System.out.print(" ");
				break;
			case 1:
				System.out.print("♚");
				break;
			case 2:
				System.out.print("♛");
				break;
			case 3:
				System.out.print("♝");
				break;
			case 4:
				System.out.print("♞");
				break;
			case 5:
				System.out.print("♜");
				break;
			case 6:
				System.out.print("♟︎");
				break;
			case 7:
				System.out.print(" ");
				break;
			// Black pieces
			case 8:
				System.out.print("♔");
				break;
			case 9:
				System.out.print("♕");
				break;
			case 10:
				System.out.print("♗");
				break;
			case 11:
				System.out.print("♘");
				break;
			case 12:
				System.out.print("♖");
				break;
			case 13:
				System.out.print("♙");
				break;
			case 14:
				System.out.print(" ");
				break;
		}
	}

	private void invalidSyntax() {
		System.out.println("Invalid syntax entered. Try again: ");
	}

	private void invalidMoveShape() {
		System.out.println("Invalid movement path entered. Try again: ");
	}

	private void movedIntoCheck() {
		System.out.println("You cannot move into check");
	}

	private void normalMove(int[][] board, int newTurn, boolean check) {
		// print board
		// print whose turn it is
		// print if in check
		printBoard(board);
		String color = (newTurn == WHITE) ? "White" : "Black";
		if(check) {
			System.out.println(color + " to move. You are in check");
		}
		System.out.println(color + " to move. Enter move: ");
	}

	private void gameOver(int[][] board, int winner) {
		// print board
		// print winner
		printBoard(board);
		String color = winner == WHITE ? "White" : "Black";
		System.out.println("Game over");
		System.out.println(color +" wins!");
	}

	private void internalError() {
		System.out.println("An internal error occured");
	}

}
