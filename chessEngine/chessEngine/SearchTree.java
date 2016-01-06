package chessEngine;

import java.util.Stack;

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
 * 		best move to make:		bitboard (Long)
 * 
 * 
 */

public class SearchTree {
	
	int ply;
	BoardNode root;
	
	public SearchTree(long board, int maxDepth, String moveList) {
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
	
	public Long alphaBeta(long bitboard) {		
		root = maxValue(bitboard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		return root.bestMove;
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
	
	public BoardNode maxValue(long bitboard, int depth, int alpha, int beta) {
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
	
	public String makeNextMoves(long bitboard) {
		
	}
}
