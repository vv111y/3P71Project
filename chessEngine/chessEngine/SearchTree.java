package chessEngine;


/*
 * Minimax with Alpha-Beta pruning
 * Based on psuedocode from textbook Fig. 5.7
 * 
 * Receives:
 * 		current board state:	bitboard (Long)
 * 		max search depth:		ply (int)
 * 
 * Creates:
 * 		nodes
 * 
 * Returns:
 * 		best move to make:		String (x1y1x2y2 format)
 * 
 * 
 */

public class SearchTree {
	
	int ply;
	BoardNode root;
	
	public SearchTree(GameState board, int maxDepth) {
		ply = maxDepth;
		root = new BoardNode(board); // bitboard
	}
	

	/* ---PSEUDOCODE----
	 * alphaBeta(gameState)
	 * 	value = maxValue(gameState, depth, Integer.MINVALUE, Integer.MAXVALUE)
	 * 	return best move based on value
	 * 
	 * 
	 * Takes the current gameState board and passes it to maxValue along
	 * with the upper and lower bound for alpha and beta. It returns the
	 * bitboard of the bestMove.
	 */
	
	public String alphaBeta() {
		while (!root.moveList.isEmpty()) {
			maxValue(bitboard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
			return root.bestMove;
		}
	}
	
	
	/* ---PSEUDOCODE----
	 *  maxValue(gameState, alpha, beta)
	 * 	if depth = maxPly
	 * 		return evaluate(gameState)
	 * 	value = Integer.MINVALUE
	 * 	for each move
	 * 		value = MAX(value, minValue(makeNextMoves, alpha, beta))
	 * 		if value >= beta
	 * 			return value
	 * 		alpha = MAX(alpha, value)
	 * 	return value
	 */
	
	public void maxValue(long bitboard, int depth, int alpha, int beta) {
		if (depth == ply) {
			return scoreBoard(bitboard);
		}
		root.setScore(alpha);
		for (int i = 0; i < nextMoves.length(); i=+4) {
			root.setScore(Math.max(root.getScore(), minValue(makeNextMoves)));
		}
	}
	
	
	/* ---PSEUDOCODE----
	 *  minValue(gameState, depth, alpha, beta)	
	 * 	if depth = maxPly
	 * 		return evaluate(gameState)
	 *  value = Integer.MAXVALUE
	 *  for each move
	 *  	value = MIN(value, maxValue(makeNextMoves, alpha, beta))
	 * 		if value <= alpha
	 * 			return value
	 * 		beta = MIN(alpha, value)
	 * 	return value
	 */
	
	
	/*
	 * Methods to create bitboards for new children
	 */

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

}
