package chessEngine;

public class BoardNode {
	
	// bitboards for pieces
	// P = pawn, N = night, B = bishop, R = rook, Q = queen, K = king
	long wP = 0L, wN = 0L, wB = 0L, wR = 0L, wQ = 0L, wK = 0L; // white
	long bP = 0L, bN = 0L, bB = 0L, bR = 0L, bQ = 0L, bK = 0L; // black
	
	// objects
	MoveList moves;
	
	String				moveList;			// moves that can be made from current state
	long 				bestMove; 			// best move
	long				unsafeMoves;		// board for all unsafe moves
	int 				score; 				// evaluation of board
	int 				alpha, beta; 		// search bounds for board


	
	/*
	 * Constructors
	 */
	public BoardNode(GameState game) { // set start board for node
		arrayToBB(game.currentBoard);
		moves = new MoveList(); // create new moveList object

		switch (game.max) {
		case "W":
			moveList = moves.whiteMoves(wP, wN, wB, wR, wQ, wK, bP, bN, bB, bR, bQ, bK, game.wSCastle, game.wLCastle);
			unsafeMoves = moves.unsafe(false, bP, bN, bB, bR, bQ, bK);
			unsafeMoves &= (bP | bN | bB | bR | bQ | bK);
			break;
		case "B":
			moveList = moves.blackMoves(wP, wN, wB, wR, wQ, wK, bP, bN, bB, bR, bQ, bK, game.bSCastle, game.bLCastle);
			unsafeMoves = moves.unsafe(true, wP, wN, wB, wR, wQ, wK);
			unsafeMoves &= (wP | wN | wB | wR | wQ | wK);
			break;
		default:
			// TODO THROW ERROR
			break;
		}

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

	public long makeMove(long board, String move, char type) {
		if (Character.isDigit(move.charAt(3))) { // just a move
			int start = (Character.getNumericValue(move.charAt(0)) * 8) // multiply by to get proper bit index
					+ (Character.getNumericValue(move.charAt(1)));
			int end = (Character.getNumericValue(move.charAt(2)) * 8) // multiply by to get proper bit index
					+ (Character.getNumericValue(move.charAt(3)));
			if (((board >>> start) & 1) == 1) { // check that starting location exists on board
				board &= ~(1L << start); // remove piece from starting location
				board |= (1L << end); // add piece to destination
			} else {
				board &= ~(1L << end); // removes piece at destination if capture occurs
			}
		} else if (move.charAt(3) == 'P') { // pawn promotion
			int start, end;
			if (Character.isUpperCase(move.charAt(2))) { // if white move
				start = Long.numberOfTrailingZeros(moves.fileMasks[move.charAt(0)-'0'] & moves.rankMasks[1]);
				end = Long.numberOfTrailingZeros(moves.fileMasks[move.charAt(1)-'0'] & moves.rankMasks[0]);
			} else { // black move
				start = Long.numberOfTrailingZeros(moves.fileMasks[move.charAt(0)-'0'] & moves.rankMasks[6]);
				end = Long.numberOfTrailingZeros(moves.fileMasks[move.charAt(1)-'0'] & moves.rankMasks[7]);
			}
			if (type == move.charAt(2)) { // check that piece promotion matches board type
				board |= (1L << end); // add promoted piece to destination
			} else {
				board &= ~(1L << start); // remove pawn from starting location
				board &= ~(1L << end); // remove pawn from destination
			}
		} else if (move.charAt(3)=='E') {//en passant
			int start, end;
			if (move.charAt(2)=='W') { // white move
				start = Long.numberOfTrailingZeros(moves.fileMasks[move.charAt(0)-'0'] & moves.rankMasks[3]);
				end  = Long.numberOfTrailingZeros(moves.fileMasks[move.charAt(1)-'0'] & moves.rankMasks[2]);
				board &= ~(moves.fileMasks[move.charAt(1)-'0'] & moves.rankMasks[3]);
			} else { // black move
				start = Long.numberOfTrailingZeros(moves.fileMasks[move.charAt(0)-'0'] & moves.rankMasks[4]);
				end = Long.numberOfTrailingZeros(moves.fileMasks[move.charAt(1)-'0'] & moves.rankMasks[5]);
				board &= ~(moves.fileMasks[move.charAt(1)-'0'] & moves.rankMasks[4]);
			}
			if (((board >>> start) & 1) == 1) { // check that starting location exists on board
				board &= ~(1L << start); // remove piece from starting location
				board |= (1L << end); // add piece to destination
			}
		} else { // catch errors in move list
			System.out.print("ERROR: Invalid move type");
		}
		return board;
	}

	public long makeMoveEP(long board, String move) {
		if (Character.isDigit(move.charAt(3))) { // check move type
			int start = (Character.getNumericValue(move.charAt(0)) * 8) // multiply by to get proper bit index
					+ (Character.getNumericValue(move.charAt(1)));
			if ((Math.abs(move.charAt(0) - move.charAt(2)) == 2)
					&& (((board >>> start) & 1) == 1)) { // move is a pawn double push
				return moves.fileMasks[move.charAt(1)-'0']; // return mask for file where en passant is possible
			}
		}
		return 0;
	}

	public long makeMoveCastle(long rBoard, long kBoard, String move, char type) {
		int start = (Character.getNumericValue(move.charAt(0)) * 8) // multiply by to get proper bit index
				+ (Character.getNumericValue(move.charAt(1)));
		if ((((kBoard >>> start) & 1) == 1) // ensure king is still in starting position
				&& (("0402".equals(move))  // black, queen side castle
						|| ("0406".equals(move)) // black, king side castle
						|| ("7472".equals(move)) // white, queen side
						|| ("7476".equals(move)))) { // white, king side
			if (type == 'R') { // white move
				switch (move) {
				case "7472": // queen side
					rBoard &= ~(1L << 56); // remove rook from starting location
					rBoard |= (1L << (56 + 3)); // add rook to destination
					break;

				case "7476": // king side
					rBoard &= ~(1L << 63); // remove rook from starting location
					rBoard |= (1L << (63 - 2)); // add rook to destination
					break;
				}
			} else { // black move
				switch (move) {
				case "0402": // queen side
					rBoard &= ~(1L << 0); // remove rook from starting location
					rBoard |= (1L << (0 + 3)); // add rook to destination
					break;
				case "0406": // king side
					rBoard &= ~(1L << 7); // remove rook from starting location
					rBoard |= (1L << (7 - 2)); // add rook to destination
					break;
				}
			}
		}
		return rBoard;
	}


	/*
	 * Setters
	 */
	public void setBest(long bitboard) { // set best move for starting board
		bestMove = bitboard;
	}

	public void setScore(int eval) { // set evaluation score for start board
		score = eval;
	}

	public void setBounds(int a, int b) { // set pruning bounds for start board
		alpha = a;
		beta = b;
	}


	/*
	 * Getters
	 */
	public long getBest() { // get best move for starting board
		return bestMove;
	}

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