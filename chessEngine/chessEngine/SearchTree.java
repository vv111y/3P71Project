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
	

	/* 
	 * Takes the current gameState board and passes it to maxValue along
	 * with the upper and lower bound for alpha and beta. It returns the
	 * bitboard of the bestMove.
	 */
	
	public String alphaBeta() {
		while (!root.moves.moveList.isEmpty()) {
			int depth = 0;
			// create new node from moveList
			BoardNode nextNode = new BoardNode(makeMove(root));
			maxValue(nextNode, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		}
		return root.bestMove;
	}
	
	
	public int maxValue(BoardNode nextNode, int depth, int alpha, int beta) {
		if (depth == ply) {
			return nextNode.scoreBoard();
		}
		int value; 
		nextNode.setScore(alpha);
		while (!nextNode.moves.moveList.isEmpty()) {
			value = nextNode.setScore(Math.max(nextNode.getScore(), minValue(new BoardNode(makeMove(nextNode), depth - 1, alpha, beta))));
			if (value >= beta) {
				return value;
			}
			alpha = Math.max(alpha, value);
		}
		return value;
	}
	
	public int minValue(BoardNode nextNode, int depth, int alpha, int beta) {
		if (depth == ply) {
			return nextNode.scoreBoard();
		}
		int value; 
		nextNode.setScore(beta);
		while (!nextNode.moves.moveList.isEmpty()) {
			value = nextNode.setScore(Math.min(nextNode.getScore(), maxValue(new BoardNode(makeMove(nextNode), depth - 1, alpha, beta))));
			if (value <= alpha) {
				return value;
			}
			beta = Math.min(beta, value);
		}
		return value;
	}
	
	
	/*
	 * Methods to create bitboards for new children
	 */

	public String[][] makeMove(BoardNode currentNode) {
		String[][] newBoard = currentNode.game.CurrentBoard;
		String nextMove = currentNode.moves.moveList.remove();
		String piece = newBoard[nextMove.charAt(0)][nextMove.charAt(1)];
		newBoard[nextMove.charAt(0)][nextMove.charAt(1)] = " ";
		newBoard[nextMove.charAt(2)][nextMove.charAt(3)] = "piece";
		return newBoard;
	}

}
