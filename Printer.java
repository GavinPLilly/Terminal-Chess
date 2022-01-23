import java.util.Scanner;

public class Printer implements ChessViewObject {

	final int WHITE = 1;
	final int BLACK = -1;

	boolean invertedColors;
	Scanner scanner;

	public Printer() {
		this.scanner = new Scanner(System.in);
	}

	public void init() {
		System.out.println("Two kings are printed below. What which king is white? Enter L for left, R for right");
		System.out.println("♔   ♚");
		System.out.print(":");
		setInvertedColors();
	}

	private void setInvertedColors() {
		String input;
		while(true) {
			input = this.scanner.nextLine();
			input = input.toUpperCase();
			if(input.equals("L")) {
				this.invertedColors = false;
				return;
			}
			if(input.equals("R")) {
				this.invertedColors = true;
				return;
			}
			System.out.print(":");
		}
	}

	public String getInput() {
		return this.scanner.nextLine();
	}

	public void dispMakeMove() {
		System.out.println("Enter a move");
	}

	public void dispPawnPromo() {
		System.out.println("A pawn in being promoted");
		System.out.println("Enter the letter for the piece you want to promote to.");
		System.out.println("q = queen, b = bishop, n = knight, r = rook");
	}

	public void dispBoard(int[][] board, int turn, boolean check) {
		printBoard(board);
		String turnString = turn == WHITE ? "White" : "Black";
		System.out.println(turnString + " to move.");
		if(check) {
			System.out.println("You are in check");
		}
	}

	public void dispGameOver(int[][] board, int winner) {
		this.printBoard(board);
		String winnerString = winner == WHITE ? "White" : "Black";
		System.out.println(winnerString + " wins!");
	}

	public void dispInvalidSyntax() {
		System.out.println("Invalid syntax");
	}

	public void dispInvalidMove() {
		System.out.println("Invalid move");
	}

	public void dispInvalidMovementPath() {
		System.out.println("The coordinates entered are not a valid movement path");
	}

	public void dispCannotMoveCheck() {
		System.out.println("You cannnot move into check. Try again");
	}

	public void dispInternalError() {
		System.out.println("INTERNAL ERROR");
		System.exit(1);
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

}
