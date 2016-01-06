package chessEngine;


/*
 * BoardNode holds all informtion regarding a single game state.
 * It is able to create its own moveList and evaluate its
 * own heuristic score.
 */

public class BoardNode {
	
	// bitboards for pieces
	// P = pawn, N = night, B = bishop, R = rook, Q = queen, K = king
	long wP = 0L, wN = 0L, wB = 0L, wR = 0L, wQ = 0L, wK = 0L; // white
	long bP = 0L, bN = 0L, bB = 0L, bR = 0L, bQ = 0L, bK = 0L; // black
	
	// objects
	GameState game = new GameState();
	MoveList moves;
	NodeEvaluation eval;
	
	String				moveList;			// moves that can be made from current state
	String 				moveMade; 			// move made (x1y1x2y2)
	long				unsafeMoves;		// board for all unsafe moves
	int 				score; 				// evaluation of board
	int 				alpha, beta; 		// search bounds for board


	
	/*
	 * Constructors
	 */
	public BoardNode(GameState g) { // set start board for node
		game = g;
		arrayToBB(game.currentBoard);
		moves = new MoveList(); // create new moveList object
		createMoves();
	}

	/*
	 * Methods to initialize node
	 */

	public void arrayToBB(String[][] board) {
		// TODO: test arrayToBB
		// converts string array to bitboard
		String bitboard;
		for (int i = 0; i < 64; i++) { // loop through each char in string array
			bitboard = "0000000000000000000000000000000000000000000000000000000000000000"; // 64bit string
			bitboard = bitboard.substring(i+1) + "1" + bitboard.substring(0, i);
			switch (board[i/8][i%8]) { // look at chars at each position in board
			case "R": // black rooks
				bR += parseString(bitboard);
				break;
			case "N": // black knights
				bN += parseString(bitboard);
				break;
			case "B": // black bishops
				bB += parseString(bitboard);
				break;
			case "Q": // black queens
				bQ += parseString(bitboard);
				break;
			case "K": // black king
				bK += parseString(bitboard);
				break;
			case "P": // black pawns
				bP += parseString(bitboard);
				break;
			case "r": // white rooks
				wR += parseString(bitboard);
				break;
			case "n": // white knights
				wN += parseString(bitboard);
				break;
			case "b": // white bishops
				wB += parseString(bitboard);
				break;
			case "q": // white queens
				wQ += parseString(bitboard);
				break;
			case "k": // white king
				wK += parseString(bitboard);
				break;
			case "p": // white pawns
				wP += parseString(bitboard);
				break;
			default:
				// TODO: Throw exception for bad board, unable to read char
				break;
			}
		}
	}
	
	public long parseString(String bitboard) {
		// TODO: test parseString
		// parses a char string and returns a binary string
		if (bitboard.charAt(0) == '0') { // simple parse
			return Long.parseLong(bitboard, 2);
		} else { // 2s compliment conversion to prevent negative number
			return Long.parseLong("1" + bitboard.substring(2), 2) * 2;
		}
	}
	
	public void createMoves() {
		switch (game.max) {
		case "W":
			moves.whiteMoves(wP, wN, wB, wR, wQ, wK, bP, bN, bB, bR, bQ, bK, game.wSCastle, game.wLCastle);
			break;
		case "B":
			moves.blackMoves(wP, wN, wB, wR, wQ, wK, bP, bN, bB, bR, bQ, bK, game.bSCastle, game.bLCastle);
			break;
		default:
			// TODO THROW ERROR
			break;
		}
	}
	
	/*
	 * Methods to evaluate node
	 */
	
	public int scoreBoard() {
		switch (game.max) {
		case "W":
			score = scoreWhite() - scoreBlack() + moves.moveList.size();
			break;
		case "B":
			score = scoreBlack() - scoreWhite() + moves.moveList.size();
			break;
		default:
			// TODO THROW ERROR
			break;
		}
		return score;
	}
	
	public int scoreWhite() {
		int playerScore = 0;
		int material;
		
		// calculate material value for each piece type
		playerScore += eval.material(wP, "P");
		playerScore += eval.material(wR, "R");
		playerScore += eval.material(wN, "N");
		playerScore += eval.material(wB, "B");
		playerScore += eval.material(wQ, "Q");
		
		// copy material score for use in position scoring for king
		material = playerScore;
		
		// calculate positional value of each piece type
		// boolean flag for reversing bits to score black positions
		playerScore += eval.position(wP, "P", material, false);
		playerScore += eval.position(wR, "R", material, false);
		playerScore += eval.position(wN, "N", material, false);
		playerScore += eval.position(wB, "B", material, false);
		playerScore += eval.position(wQ, "Q", material, false);
		playerScore += eval.position(wK, "K", material, false);
		
		// calculate penalty for pieces in danger
		unsafeMoves = moves.unsafe(false, bP, bN, bB, bR, bQ, bK);
		unsafeMoves &= (bP | bN | bB | bR | bQ | bK);
		playerScore -= eval.attacks(unsafeMoves);
		
		return playerScore;
	}
	
	public int scoreBlack() {
		int playerScore = 0;
		int material;
		
		// calculate material value for each piece type
		playerScore += eval.material(bP, "P");
		playerScore += eval.material(bR, "R");
		playerScore += eval.material(bN, "N");
		playerScore += eval.material(bB, "B");
		playerScore += eval.material(bQ, "Q");
		
		// copy material score for use in position scoring for king
		material = playerScore;
		
		// calculate positional value of each piece type
		playerScore += eval.position(bP, "P", material, true);
		playerScore += eval.position(bR, "R", material, true);
		playerScore += eval.position(bN, "N", material, true);
		playerScore += eval.position(bB, "B", material, true);
		playerScore += eval.position(bQ, "Q", material, true);
		playerScore += eval.position(bK, "K", material, true);
		
		// calculate penalty for pieces in danger
		unsafeMoves = moves.unsafe(true, wP, wN, wB, wR, wQ, wK);
		unsafeMoves &= (bP | bN | bB | bR | bQ | bK);
		playerScore -= eval.attacks(unsafeMoves);
		
		return playerScore;
	}
	
	


	/*
	 * Setters
	 */

	public int setScore(int eval) { // set evaluation score for start board
		score = eval;
		return score;
	}

	public void setBounds(int a, int b) { // set pruning bounds for start board
		alpha = a;
		beta = b;
	}


	/*
	 * Getters
	 */


	public int getScore() { // get evaluation score for start board
		return score;
	}

	public int getAlpha() { // get pruning bounds for start board
		return alpha;
	}

	public int getBeta() { // get pruning bounds for start board
		return beta;
	}
}