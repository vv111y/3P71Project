package chessEngine;

import java.util.LinkedList;

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
		LinkedList<BoardNode> moveList = new LinkedList();
		int bestScore = 0;
		
		while (!root.moves.moveList.isEmpty()) {
			int depth = 0;
			// create new node from moveList
			BoardNode newNode = new BoardNode(root.game);
			newNode = makeMove(newNode);
			moveList.add(newNode);
			bestScore = maxValue(newNode, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		}
		
		BoardNode bestMove = moveList.remove();
		while (bestMove.score != bestScore) {
			bestMove = moveList.remove();
		}
		
		return bestMove.moveMade;
	}
	
	
	public int maxValue(BoardNode nextNode, int depth, int alpha, int beta) {
		if (depth == ply) {
			return nextNode.scoreBoard();
		}
		nextNode.setScore(alpha);
		while (!nextNode.moves.moveList.isEmpty()) {
			// create new node from moveList
			BoardNode newNode = new BoardNode(nextNode.game);
			newNode = makeMove(newNode);
			nextNode.setScore(Math.max(nextNode.getScore(), minValue(newNode, depth + 1, alpha, beta)));
			if (nextNode.getScore() >= beta) {
				return nextNode.getScore();
			}
			alpha = Math.max(alpha, nextNode.getScore());
		}
		return nextNode.getScore();
	}
	
	
	public int minValue(BoardNode nextNode, int depth, int alpha, int beta) {
		if (depth == ply) {
			return nextNode.scoreBoard();
		}
		nextNode.setScore(beta);
		while (!nextNode.moves.moveList.isEmpty()) {
			// create new node from moveList
			BoardNode newNode = new BoardNode(nextNode.game);
			newNode = makeMove(newNode);
			nextNode.setScore(Math.min(nextNode.getScore(), maxValue(nextNode, depth + 1, alpha, beta)));
			if (nextNode.getScore() <= alpha) {
				return nextNode.getScore();
			}
			beta = Math.min(beta, nextNode.getScore());
		}
		return nextNode.getScore();
	}
	
	
	/*
	 * Methods to create bitboards for new children
	 */

	public BoardNode makeMove(BoardNode node) {
		BoardNode newNode = node;
		String[][] newBoard = newNode.game.currentBoard;
		String nextMove = newNode.moves.moveList.remove();
		newNode.moveMade = nextMove;
		String piece = newBoard[nextMove.charAt(0)][nextMove.charAt(1)];
		newBoard[nextMove.charAt(0)][nextMove.charAt(1)] = " ";
		newBoard[nextMove.charAt(2)][nextMove.charAt(3)] = "piece";
		
		switch (newNode.game.max) {
		case "W":
			newNode.game.max = "B";
			break;
		case "B":
			newNode.game.max = "W";
			break;
		default:
			//TODO throw exception
			break;
		}
		
		newNode.arrayToBB(newBoard);
		newNode.updateMoves();
			
		return newNode;
	}

}
