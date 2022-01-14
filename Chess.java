import java.util.Stack;
import java.lang.Math;

public class Chess {

	// Colors
	final int WHITE = 1;
	final int BLACK = -1;

	// Piece types
	final int KING = 1;
	final int QUEEN = 2;
	final int BISHOP = 3;
	final int KNIGHT = 4;
	final int ROOK = 5;
	final int PAWN = 6;
	final int EN_PASSANT = 7;

	// Move types
	final int LMOVE_TYPE = 0;
	final int KMOVE_TYPE = 1;
	final int SMOVE_TYPE = 2;


	Piece[][] board; // [x][y]	[0][0] is bottom left
	int turn;
	Stack<Move> moveStack; 

	// General Markers
	Coord EPMarker;
	Coord whiteKing; 
	Coord blackKing;
	Coord[] whitePieces;
	Coord[] blackPieces;

	// Turn reliant Markers
	Coord ownKing; // King of the current turn based on this.turn
	Coord eneKing; // King of the enemey based on this.turn
	Coord[] ownPieces;
	Coord[] enePieces;

	public Chess() {
		this.board = new Piece[8][8]; // Initialize board
		initializeBoard();
		this.turn = WHITE;
		this.moveStack = new Stack<Move>();
		initializeArrays(); // Initialize arrays
		initializeMarkers(); // Initialize markers
	}

	public Message runInput(Message message) {
		int messageType = message.messageType();
		// Invalid syntax case
		if(messageType == 17) {
			return new IIMessage();
		}
		// Valid syntax case
		if(messageType == 16) {
			message = (VIMessage)message;
			return handleGeneralInput(((VIMessage)message).input());
		}
		if(messageType == 18) {
			return generateNormalMessage();
		}
		// ...
		return new InternalErrorMessage();
	}

	public boolean isGameOver() {
		// TODO 0
		return false;
	}

	private Message handleGeneralInput(String input) {
		if(isCastlingInput(input)) {
			return handleCastlingInput(input);
		}
		return handleNormalInput(input);
	}

	private Message handleCastlingInput(String input) {
		// check that castle is valid
		if(isValidCastle(input) == false) {
			return new IMSMessage();
		}
		// execute move and add submoves to stack
		executeMoveCastle(input);
		// Generate and return message
		return generateNormalMessage();
	}

	private Message handleNormalInput(String input) {
		// Update board, change turn
		// Check the game state and pass the relevant message

		// get the start, end coords
		Coord[] coords = getCoordsFromInput(input);
		Coord start = coords[0];
		Coord end = coords[1];
		// Check validity of move
		if(isValidMove(start, end) == false) {
			// If invalid, pass message
			return new IMSMessage();
		}
		// execute move and add submoves to stack
		executeMove(start, end);
		// Flip turn
		flipTurn(); 
		// Generate and return message
		return generateNormalMessage();
	}

	// ###################################################
	// Move testing methods
	// ###################################################
	private boolean isCastlingInput(String input) {
		switch(input.charAt(0)) {
			case '0':
			case 'O':
				return true;
			default:
				return false;
		}
	}

	private boolean isQueenSide(String input) {
		return input.length() == 5;
	}

	private boolean isValidCastle(String input) {
		// Decode kingside/queenside
		// Check neither piece has moved (king rook)
		// Check the squares are empty
		// Check king doesn't move through/into check
		// (I don't actually need to try/revert the
		// 	move became I already checked if the any 
		// 	of the squares will be vulnearble)

		// Queenside Case
		if(isQueenSide(input)) {
			// White Case
			if(this.turn == WHITE) {
				// Check that the rook and king are in place
				if(this.board[0][0] == null || this.board[4][0] == null) {
					return false;
				}
				// Check that the rook and king are in place
				if(this.board[0][0].type() != ROOK || this.board[4][0].type() != KING) {
					return false;
				}
				// Check that niether piece has moved
				if(this.board[0][0].moved() || this.board[4][0].moved() ) {
					return false;
				}
				// Check that the right squares are empty
				if(this.board[0][1] != null || this.board[0][2] != null || this.board[0][3] != null) {
					return false;
				}
				// Check that the king won't move through check
				if(canEneAttack(0, 1) || canEneAttack(0, 2) || canEneAttack(0, 3)) {
					return false;
				}
				return true;
			}
			// Black Case
			// Check that the rook and king are in place
			if(this.board[0][7] == null || this.board[4][0] == null) {
				return false;
			}
			// Check that the rook and king are in place
			if(this.board[0][7].type() != ROOK || this.board[4][0].type() != KING) {
				return false;
			}
			// Check that niether piece has moved
			if(this.board[0][7].moved() || this.board[4][0].moved()) {
				return false;
			}
			// Check that the right squares are empty
			if(this.board[1][0] != null || this.board[2][0] != null || this.board[3][0] != null) {
				return false;
			}
			// Check that the king won't move through check
			if(canEneAttack(1, 0) || canEneAttack(2, 0) || canEneAttack(3, 0)) {
				return false;
			}
			return true;
		}
		// Kingside Case
		// White Case
		if(this.turn == WHITE) {
			// Check that the rook and king are in place
			if(this.board[7][0] == null || this.board[4][0] == null) {
				return false;
			}
			// Check that the rook and king are in place
			if(this.board[7][0].type() != ROOK || this.board[4][0].type() != KING) {
				return false;
			}
			// Check that neither piece has moved
			if(this.board[7][0].moved() || this.board[4][0].moved()) {
				return false;
			}
			// Check that the king won't move through check
			if(canEneAttack(5, 0) || canEneAttack(6, 0)) {
				return false;
			}
			return true;
		}
		// Black Case
		// Check that the rook and king are in place
		if(this.board[7][7] == null || this.board[4][7] == null) {
			return false;
		}
		// Check that the rook and king are in place
		if(this.board[7][7].type() != ROOK || this.board[4][7].type() != KING) {
			return false;
		}
		// Check that neither piece has moved
		if(this.board[7][7].moved() || this.board[4][7].moved()) {
			return false;
		}
		// Check that the king won't move through check
		if(canEneAttack(5, 7) || canEneAttack(6, 7)) {
			return false;
		}
		return true;
	}

	private boolean isValidMove(Coord start, Coord end) {
		// check validity of move
		// 	check movement shape
		// 	check for check
		//
		if(isValidMovement(start, end) == false) {
			return false;
		}
		if(movesIntoCheck(start, end)) {
			return false;
		}
		return true; 
	}

	// Checks for obvious things that would invalidate a move
	// 	pieces are the right color
	// 	squares in bound
	// 	is the movement path allowed for the piece type
	private boolean isValidMovement(Coord start, Coord end) {
		// get piece type, pass to that relevant method

		// Check that the start square is bound
		if(start.x < 0 || start.x > 7 || start.y < 0 || start.y > 7) {
			return false;
		}
		if(end.x < 0 || end.x > 7 || end.y < 0 || end.y > 7) {
			return false;
		}
		// Check to make sure the starting and ending squares are not the same
		if(start.equals(end)) {
			return false;
		}
		Piece killer = board[start.x][start.y];
		Piece victim = board[end.x][end.y];
		// Check that there is a piece at the starting coord
		if(killer == null) {
			return false;
		}
		// Check that the killer is not dead
		if(killer.dead()) {
			return false;
		}
		// Check that the killer is the same color as the turn
		if(killer.color() != this.turn) {
			return false;
		}
		// Check to make sure the piece is not an EP marker
		if(killer.type() == EN_PASSANT) {
			return false;
		}
		// If the ending square is not empty,
		// 	check to make sure the ending sqaure does not contain 
		// 	a piece of the same color as the killer
		if(victim != null && victim.color() == this.turn) {
			return false;
		}
		// Is the movement allowed for the piece
		switch(killer.type()) {
			case KING:
				return isValidKingMovement(start, end);
			case QUEEN:
				return isValidQueenMovement(start, end);
			case BISHOP:
				return isValidBishopMovement(start, end);
			case KNIGHT:
				return isValidKingMovement(start, end);
			case ROOK:
				return isValidRookMovement(start, end);
			case PAWN:
				return isValidPawnMovement(start, end);
			default:
				return false;
		}
	}

	// Assume that start and end have already been checked with
	// 	isValidMovement()
	private boolean movesIntoCheck(Coord start, Coord end) {
		// TODO
		// try to move
		// test with canEneAttack
		// revert move
		return false;
	}

	private boolean isValidKingMovement(Coord start, Coord end) {
		if(Math.abs(start.x - end.x) <= 1 && Math.abs(start.y - end.y) <= 1) {
			return true;
		}
		return false;
	}
	private boolean isValidQueenMovement(Coord start, Coord end) {
		if(isValidBishopMovement(start, end)) {
			return true;
		}
		if(isValidRookMovement(start, end)) {
			return true;
		}
		return false;
	}
	private boolean isValidBishopMovement(Coord start, Coord end) {
		// Check sure move is diagonal
		if(Math.abs(start.x - end.x) != Math.abs(start.y - end.y)) {
			return false;
		}
		int xDir = end.x > start.x ? 1 : -1;
		int yDir = end.y > start.y ? 1 : -1;
		int x = start.x + xDir;
		int y = start.y + yDir;
		// Walk through the move path and make sure it's clear
		while(x != end.x) {
			// Check that each square is empty. En passant markers don't count
			if(this.board[x][y] != null && this.board[x][y].type() != EN_PASSANT) {
				return false;
			}
			x += xDir;
			y += yDir;
		}
		return true;
	}
	private boolean isValidKnightMovement(Coord start, Coord end) {
		if(Math.abs(start.x - end.x) == 3 && Math.abs(start.y - end.y) == 2) {
			return true;
		}
		if(Math.abs(start.x - end.x) == 2 && Math.abs(start.y - end.y) == 3) {
			return true;
		}
		return false;
	}
	private boolean isValidRookMovement(Coord start, Coord end) {
		// A rook can only move along one axis
		if(start.x != end.x && start.y != end.y) {
			return false;
		}
		int xDir = end.x == start.x ? 0 : end.x > start.x ? 1 : -1;
		int yDir = end.y == start.y ? 0 : end.y > start.y ? 1 : -1;
		int x = start.x + xDir;
		int y = start.y + yDir;
		// Walk through the move path and make sure it's clear
		while(x != end.x || y != end.y) {
			// Check that each square is empty. En passant markers don't count
			if(this.board[x][y] != null && this.board[x][y].type() != EN_PASSANT) {
				return false;
			}
			x += xDir;
			y += yDir;
		}
		return true;
	}
	private boolean isValidPawnMovement(Coord start, Coord end) {
		// Attacking case
		// Double move case
		// Normal case
		return isValidPawnAdvance(start, end) || isValidPawnAttack(start, end) || isValidPawnJump(start, end);
	}

	private boolean isValidPawnAttack(Coord start, Coord end) {
		// The pawn moves one spaces laterally 		  && pawn moves one space vertically && end square in not empty
		// 						     is the right direction
		if((start.x == end.x - 1 || start.x == end.x + 1) && end.y == start.y + this.turn && (board[end.x][end.y] != null)) {
			return true;
		}
		return false;
	}

	// Test the validity of a pawn moving out 2 spaces
	// 	as its first move
	private boolean isValidPawnJump(Coord start, Coord end) {
		// Pawn stays in    && Pawn advances twice                && Pawn has not moved				   && the sqaure ahead is empty      && end square is empty
		// same column	       is right direction								      hi
		if(start.x == end.x && end.y == start.y + (this.turn * 2) && this.board[start.x][start.y].moved() == false && this.board[start.x][start.y + this.turn] == null && this.board[end.x][end.y] == null) {
			return true;
		}
		return false;
	}

	// Test for a normal pawn move of forward one space
	private boolean isValidPawnAdvance(Coord start, Coord end) {
		// End squares is empty           && Pawn stays in same     && Pawn advances once in right
		// 				     column		       Direction
		if(this.board[end.x][end.y] == null && start.x == end.x && end.y == start.y + this.turn) {
			return true;
		}
		return false;
	}

	// ###################################################
	// Move execution methods
	// ###################################################


	// Executes a castle move on the board and adds the submoves 
	// 	to the moveStack
	private void executeMoveCastle(String input) {
		// Queenside case
		if(isQueenSide(input)) {
			// White case
			if(this.turn == WHITE) {
				// Generating and adding submoves to moveStack
				Move rookMove = new LMove(this.turn, this.board[0][0], new Coord(0, 0), new Coord(3, 0));
				this.moveStack.push(rookMove);
				Move kingMove = new LMove(this.turn, this.board[4][0], new Coord(4, 0), new Coord(2, 0));
				this.moveStack.push(kingMove);

				// Executing move
				// Moving the rook
				this.board[3][0] = this.board[0][0];
				this.board[0][0] = null;
				// Moving the king
				this.board[2][0] = this.board[4][0];
				this.board[4][0] = null;
				return;
			}
			// Black case
			// Generating and adding submoves to moveStack
			Move rookMove = new LMove(this.turn, this.board[0][7], new Coord(0, 7), new Coord(3, 7));
			this.moveStack.push(rookMove);
			Move kingMove = new LMove(this.turn, this.board[4][7], new Coord(4, 7), new Coord(2, 7));
			this.moveStack.push(kingMove);

			// Executing move
			// Moving the rook
			this.board[3][7] = this.board[0][7];
			this.board[0][7] = null;
			// Moving the king
			this.board[2][7] = this.board[4][7];
			this.board[4][7] = null;
			return;
		}
		// Kingside case
		// White case
		if(this.turn == WHITE) {
			Move rookMove = new LMove(this.turn, this.board[7][0], new Coord(7, 0), new Coord(5, 0));
			this.moveStack.push(rookMove);
			Move kingMove = new LMove(this.turn, this.board[4][0], new Coord(4, 0), new Coord(6, 0));
			this.moveStack.push(kingMove);

			// Executing move
			// Moving the rook
			this.board[5][0] = this.board[7][0];
			this.board[7][0] = null;
			// Moving the king
			this.board[6][0] = this.board[4][0];
			this.board[4][0] = null;
			return;
		}
		// Black case
			Move rookMove = new LMove(this.turn, this.board[7][7], new Coord(7, 7), new Coord(5, 7));
			this.moveStack.push(rookMove);
			Move kingMove = new LMove(this.turn, this.board[4][7], new Coord(4, 7), new Coord(6, 7));
			this.moveStack.push(kingMove);

			// Executing move
			// Moving the rook
			this.board[5][7] = this.board[7][7];
			this.board[7][7] = null;
			// Moving the king
			this.board[6][7] = this.board[4][7];
			this.board[4][7] = null;
			return;
	}

	// Executes a move on the board and add the submoves to the moveStack
	private void executeMove(Coord start, Coord end) {
		// Special cases:
		if(this.board[start.x][start.y].type() == PAWN) {
			// Pawn jump
			if(end.y == start.y + (this.turn * 2)) {
				clearEP();
				executeMovePawnJumpHelper(start, end);
				return;
			}
		//	Pawn promotion
			if(end.y == 0 || end.y == 7) {
				clearEP();
				executeMovePawnPromotion(start, end);
				return;
			}
			// EP Attack
			if(this.board[end.x][end.y] != null && this.board[end.x][end.y].type() == EN_PASSANT) {
				clearEP();
				executeMoveEPAttackHelper(start, end);
				return;
			}
		}
		clearEP();
		// Normal case:
		executeMoveNormalHelper(start, end);
	}

	private void executeMoveNormalHelper(Coord start, Coord end) {
		// 	is an enemy is killed push move to stack
		// 	push locomotive move
		// 	exe the move
		// 	set the piece to moved
		// 	updateOwnArrayCoords

		// Pushing submoves to moveStack
		
		Piece killer = this.board[start.x][start.y];
		Piece victim = this.board[end.x][end.y];

		// Check if a piece is getting killed
		if(victim != null) {
			// Add killing move to moveStack
			this.moveStack.push(new KMove(this.turn, victim, end));
		}
		// Add locomotive move to stack
		this.moveStack.push(new LMove(this.turn, killer, start, end));

		// Executing the move

		// If a piece is getting killed, set it to dead
		if(victim != null) {
			victim.setDead();
		}
		this.board[end.x][end.y] = killer;
		this.board[start.x][start.y] = null;

		// update array Coords of killer
		updateOwnArrayCoords(start, end);
	}

	private void executeMovePawnJumpHelper(Coord start, Coord end) {
		Piece killer = this.board[start.x][start.y];

		// No KMove necessary since a pawn jump is guarantee to not kill a piece

		this.moveStack.push(new LMove(this.turn, killer, start, end));

		// Executing the move
		this.board[end.x][end.y] = killer;
		this.board[start.x][start.y] = null;

		// Adding in the new EP marker
		this.EPMarker = new Coord(start.x, start.y + this.turn);
		this.board[start.x][start.y + this.turn] = new Piece(this.turn, EN_PASSANT);
		
		// update the array Coord of killer
		updateOwnArrayCoords(start, end);
	}

	private void executeMoveEPAttackHelper(Coord start, Coord end) {
		Coord victimCoord = new Coord(end.x, end.y + (this.turn * -1));
		Piece killer = this.board[start.x][start.y];
		Piece victim = this.board[victimCoord.x][victimCoord.y];

		// Add submoves to stack
		this.moveStack.push(new KMove(this.turn, victim, victimCoord));
		this.moveStack.push(new LMove(this.turn, killer, start, end));

		// Executing the move
		// Moving the killer
		this.board[end.x][end.y] = killer;
		this.board[start.x][start.y] = null;
		// Killing the victim
		this.board[victimCoord.x][victimCoord.y] = null;

		// Updating array Coord of killer
		updateOwnArrayCoords(start, end);
	}

	private void executeMovePawnPromotion(Coord start, Coord end) {
		// TODO
	}

	// Updates the coords of a piece being in the array of the ownPieces
	private void updateOwnArrayCoords(Coord start, Coord end) {
		for(Coord coord: this.ownPieces) {
			if(coord.equals(start)) {
				coord.x = end.x;
				coord.y = end.y;
				return;
			}
		}
		System.out.println("Something when wrong in updateOwnArrayCoords()");
		System.exit(1);
	}

	// ###################################################
	// Reachability methods
	// ###################################################

	private boolean canEneAttack(int x, int y) {
		return canEneAttack(new Coord(x, y));
	}
	// NOTE: this method does NOT account for the enemy moving into check
	private boolean canEneAttack(Coord coord) {
		for(Coord eneCoord : this.enePieces) {
			if(isValidMovement(eneCoord, coord)) {
				return true;
			}
		}
		return false;
	}

	// ###################################################
	// Game managment methods
	// ###################################################

	private Coord[] getCoordsFromInput(String input) {
		input = input.toUpperCase();
		Coord[] retArray = new Coord[2];
		Coord start = new Coord(input.charAt(0) - 'A', input.charAt(1) - '1');
		Coord end = new Coord(input.charAt(3) - 'A', input.charAt(4) - '1');
		retArray[0] = start;
		retArray[1] = end;
		return retArray;
	}


	// Flip the internal turn var
	// 	along with the general marker pointers
	private void flipTurn() {
		Coord tmpCoord;
		Coord[] tmpPieces;

		this.turn *= -1;

		tmpCoord = this.eneKing;
		this.eneKing = this.ownKing;
		this.ownKing = tmpCoord;

		tmpPieces = this.enePieces;
		this.enePieces = this.ownPieces;
		this.ownPieces = tmpPieces;
	}

	// Clears the class EPMarker var and the EP marker on the board
	// Add this "move" to the move stack
	private void clearEP() {
		if(this.EPMarker == null) {
			return;
		}
		this.moveStack.push(new KMove(this.turn, this.board[EPMarker.x][EPMarker.y], this.EPMarker));
		this.board[EPMarker.x][EPMarker.y] = null;
		this.EPMarker = null;
	}

	private Message generateNormalMessage() {
		return new NMessage(getIntBoard(), this.turn, false);
	}

	private int[][] getIntBoard() {
		int[][] intBoard = new int[8][8];
		int color;
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				if(this.board[x][y] == null) {
					continue;
				}
				if(this.board[x][y].color() == BLACK) {
					color = 7;
				}
				else {
					color = 0;
				}
				intBoard[x][y] = this.board[x][y].type() + color;
			}
		}
		return intBoard;
	}

	// ###################################################
	// Set up methods
	// ###################################################

	private void initializeBoard() {
		// Setting up the white pieces
		this.board[0][0] = new Piece(WHITE, ROOK);
		this.board[1][0] = new Piece(WHITE, KNIGHT);
		this.board[2][0] = new Piece(WHITE, BISHOP);
		this.board[3][0] = new Piece(WHITE, QUEEN);
		this.board[4][0] = new Piece(WHITE, KING);
		this.board[5][0] = new Piece(WHITE, BISHOP);
		this.board[6][0] = new Piece(WHITE, KNIGHT);
		this.board[7][0] = new Piece(WHITE, ROOK);

		// Setting up the black pieces
		this.board[0][7] = new Piece(BLACK, ROOK);
		this.board[1][7] = new Piece(BLACK, KNIGHT);
		this.board[2][7] = new Piece(BLACK, BISHOP);
		this.board[3][7] = new Piece(BLACK, QUEEN);
		this.board[4][7] = new Piece(BLACK, KING);
		this.board[5][7] = new Piece(BLACK, BISHOP);
		this.board[6][7] = new Piece(BLACK, KNIGHT);
		this.board[7][7] = new Piece(BLACK, ROOK);
		
		for(int x = 0; x < 8; x++) {
			this.board[x][1] = new Piece(WHITE, PAWN);
			this.board[x][6] = new Piece(BLACK, PAWN);
		}
	}

	private void initializeArrays() {
		this.whitePieces = new Coord[16];
		this.blackPieces = new Coord[16];
		// Initial Piece Arrays
		for(int y = 0; y < 2; y++) {
			for(int x = 0; x < 8; x++) {
				this.whitePieces[x + (8 * y)] = new Coord(x, y);
				this.blackPieces[x + (8 * y)] = new Coord(x, y + 6);
			}
		}

	}

	private void initializeMarkers() {
		// Set up general vars
		this.EPMarker = null;
		this.whiteKing = new Coord(4, 0);
		this.blackKing = new Coord(4, 7);

		// Set up turn reliant vars
		this.ownKing = this.whiteKing;
		this.eneKing = this.blackKing;
		this.ownPieces = this.whitePieces;
		this.enePieces = this.blackPieces;
	}

}