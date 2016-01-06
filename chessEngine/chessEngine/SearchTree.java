package chessEngine;

import java.util.LinkedList;

/*
 * Minimax with Alpha-Beta pruning
 * Based on pseudocode from textbook Fig. 5.7
 * 
 * Takes a game state from UCI and creates all possible moves.
 * Uses minimax search with alpha-beta pruning to find the best
 * move to make. Returns the best move to UCI.
 * 
 */

public class SearchTree {
	
	int ply;
	boolean stopSearch;
	BoardNode root;
	
	/*
	 * Constructor
	 */
	
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
		LinkedList<BoardNode> moveList = new LinkedList<>(); // maintain list of nodes off root
		int bestScore = 0;
		
		while (!root.moves.moveList.isEmpty() && !stopSearch) { // create new nodes with moves from moveList
			int depth = 0; // root node is depth 0
			BoardNode newNode = new BoardNode(root.game); // create copy of root node
			String nextMove = newNode.moves.moveList.remove(); // remove move to make node
			newNode = makeMove(newNode, nextMove); // update child node with move
			moveList.add(newNode); // add child to list
			bestScore = maxValue(newNode, depth, Integer.MIN_VALUE, Integer.MAX_VALUE); // run search
		}
		
		BoardNode bestMove = moveList.remove(); // grab first child node
		while (bestMove.score != bestScore) { // keep looking for best child
			bestMove = moveList.remove();
		}
		
		return bestMove.moveMade; // return the best move to UCI
	}
	
	
	public int maxValue(BoardNode nextNode, int depth, int alpha, int beta) {
		if (depth == ply) { // if max ply reached start returning
			return nextNode.scoreBoard(); 
		}
		nextNode.setScore(alpha); // set the score to initial alpha value
		while (!nextNode.moves.moveList.isEmpty()) { // create new nodes from moveList
			BoardNode newNode = new BoardNode(nextNode.game);
			String nextMove = newNode.moves.moveList.remove(); // remove move to make node
			newNode = makeMove(newNode, nextMove); // update child node with move
			nextNode.setScore(Math.max(nextNode.getScore(), minValue(newNode, depth + 1, alpha, beta))); // find minimum max value
			if (nextNode.getScore() >= beta) { // prune if score is greater/equal to current beta
				return nextNode.getScore();
			}
			alpha = Math.max(alpha, nextNode.getScore()); // set alpha to new value
		}
		return nextNode.getScore(); // return the nodes score
	}
	
	
	public int minValue(BoardNode nextNode, int depth, int alpha, int beta) {
		if (depth == ply) { // if max ply reached start returning
			return nextNode.scoreBoard();
		}
		nextNode.setScore(beta); // set the score to initial alpha value
		while (!nextNode.moves.moveList.isEmpty()) { // create new nodes from moveList
			BoardNode newNode = new BoardNode(nextNode.game); 
			String nextMove = newNode.moves.moveList.remove(); // remove move to make
			newNode = makeMove(newNode, nextMove); // update child node with move
			nextNode.setScore(Math.min(nextNode.getScore(), maxValue(nextNode, depth + 1, alpha, beta))); // find maximum min value
			if (nextNode.getScore() <= alpha) {// prune if score is less/equal to current beta
				return nextNode.getScore();
			}
			beta = Math.min(beta, nextNode.getScore());  // set beta to new value
		}
		return nextNode.getScore();  // return the nodes score
	}
	
	
	/*
	 * Methods to create bitboards for new children
	 */

	public BoardNode makeMove(BoardNode node, String move) {
		BoardNode newNode = node;  // temp new node
		String[][] newBoard = newNode.game.currentBoard; // temp new board
		
		newNode.moveMade = move; // set move to make in node for later retrieval if necessary
		
		// create the move
		String piece = newBoard[move.charAt(0)][move.charAt(1)];
		newBoard[move.charAt(0)][move.charAt(1)] = " ";
		newBoard[move.charAt(2)][move.charAt(3)] = piece;
		
		switch (newNode.game.max) { // swap the player who's turn it is for evaluation purposes
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
		
		newNode.arrayToBB(newBoard); // generate new bitboards
		newNode.createMoves(); // update the move list
			
		return newNode;
	}

}
