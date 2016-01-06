package chessEngine;

/*
 * Receives:
 * 		boardState:		bitboard (Long)
 * 		
 * Returns:
 * 		boardEvaluation:	score (int)
 * 
 * Evaluations:
 * 		rateMaterial	sum(# of pieces player has on board)
 * 		rateMovability	sum(# of moves possible for each piece)
 * 		ratePositional	sum(each pieces positional score) 
 * 		rateAttack		-sum(# of pieces under attack)
 * 
 * Players own values are added, Opponents values are subtracted
 * 	bitboard needs to be reversed to calculate opponents positional ratings
 * 
 * Node evaluation matrices are from http://chessprogramming.wikispaces.com/Simplified+evaluation+function
 * 
 */

public class NodeEvaluation {

	int pawnScores[] = {
			 0,  0,  0,  0,  0,  0,  0,  0,
			50, 50, 50, 50, 50, 50, 50, 50,
			10, 10, 20, 30, 30, 20, 10, 10,
			 5,  5, 10, 25, 25, 10,  5,  5,
			 0,  0,  0, 20, 20,  0,  0,  0,
			 5, -5,-10,  0,  0,-10, -5,  5,
			 5, 10, 10,-20,-20, 10, 10,  5,
			 0,  0,  0,  0,  0,  0,  0,  0
	};
	
	int rookScores[] = {
			 0,  0,  0,  0,  0,  0,  0,  0,
			 5, 10, 10, 10, 10, 10, 10,  5,
			-5,  0,  0,  0,  0,  0,  0, -5,
			-5,  0,  0,  0,  0,  0,  0, -5,
			-5,  0,  0,  0,  0,  0,  0, -5,
			-5,  0,  0,  0,  0,  0,  0, -5,
			-5,  0,  0,  0,  0,  0,  0, -5,
			 0,  0,  0,  5,  5,  0,  0,  0
	};
	
	int knightScores[] = {
			-50,-40,-30,-30,-30,-30,-40,-50,
			-40,-20,  0,  0,  0,  0,-20,-40,
			-30,  0, 10, 15, 15, 10,  0,-30,
			-30,  5, 15, 20, 20, 15,  5,-30,
			-30,  0, 15, 20, 20, 15,  0,-30,
			-30,  5, 10, 15, 15, 10,  5,-30,
			-40,-20,  0,  5,  5,  0,-20,-40,
			-50,-40,-30,-30,-30,-30,-40,-50
	};
	
	int bishopScores[] = {
			-20,-10,-10,-10,-10,-10,-10,-20,
			-10,  0,  0,  0,  0,  0,  0,-10,
			-10,  0,  5, 10, 10,  5,  0,-10,
			-10,  5,  5, 10, 10,  5,  5,-10,
			-10,  0, 10, 10, 10, 10,  0,-10,
			-10, 10, 10, 10, 10, 10, 10,-10,
			-10,  5,  0,  0,  0,  0,  5,-10,
			-20,-10,-10,-10,-10,-10,-10,-20
	};
	
	int queenScores [] = {
			-20,-10,-10, -5, -5,-10,-10,-20,
			-10,  0,  0,  0,  0,  0,  0,-10,
			-10,  0,  5,  5,  5,  5,  0,-10,
			 -5,  0,  5,  5,  5,  5,  0, -5,
			  0,  0,  5,  5,  5,  5,  0, -5,
			-10,  5,  5,  5,  5,  5,  0,-10,
			-10,  0,  5,  0,  0,  0,  0,-10,
			-20,-10,-10, -5, -5,-10,-10,-20
	};
	
	int kingMidGame[] = {
			-30,-40,-40,-50,-50,-40,-40,-30,
			-30,-40,-40,-50,-50,-40,-40,-30,
			-30,-40,-40,-50,-50,-40,-40,-30,
			-30,-40,-40,-50,-50,-40,-40,-30,
			-20,-30,-30,-40,-40,-30,-30,-20,
			-10,-20,-20,-20,-20,-20,-20,-10,
			 20, 20,  0,  0,  0,  0, 20, 20,
			 20, 30, 10,  0,  0, 10, 30, 20
	};
	
	int kingEndGame[] = {
			-50,-40,-30,-20,-20,-30,-40,-50,
			-30,-20,-10,  0,  0,-10,-20,-30,
			-30,-10, 20, 30, 30, 20,-10,-30,
			-30,-10, 30, 40, 40, 30,-10,-30,
			-30,-10, 30, 40, 40, 30,-10,-30,
			-30,-10, 20, 30, 30, 20,-10,-30,
			-30,-30,  0,  0,  0,  0,-30,-30,
			-50,-30,-30,-30,-30,-30,-30,-50
	};
	
	public int scoreBoard(long gameBoard, long unsafeBoard, int depth, String type) {
		int score = 0;
		int mat = material(gameBoard, type);
		score += attacks(unsafeBoard);
		score += mat;
		score += moves(moves);
		score += position(mat);
		
//		AlphaBetaChess.flipBoard();
//		material=rateMaterial();
//		counter-=rateAttack();
//		counter-=material;
//		counter-=rateMoveablitly(list, depth, material);
//		counter-=ratePositional(material);
//		AlphaBetaChess.flipBoard();
//		return -(counter+depth*50);
	}
	
	/*
	 * Scores the value of all pieces on board
	 */
	public int material(long board, String type) {
		int score = 0; 
		int bitCnt = Long.bitCount(board);
		
		switch (type) {
		case "P": // pawn
			score = 100 * bitCnt;
			break;
		case "R": // rook
			score = 500 * bitCnt;
			break;
		case "K": // knight
			score = 300 * bitCnt;
			break;
		case "B": // bishop
			// if only 1 bishop is on board value should be less than half the value
			// of having both since only half the board is accessible
			if (bitCnt == 1) {
				score = 225;
			} else {
				score = 300 * bitCnt;
			}
			break;
		case "Q": 
			score = 900 * bitCnt;
			break;
		}
			
		return score;
	}
	
	public int attacks(long unsafeBoard) {
		return Long.bitCount(unsafeBoard);
	}
	
	
	public int moveability(long bitboard) {
		
		
		// takes bitboard generates possible moves
		// +5 for each move possible
		// -200000*depth for if checkmate
		// -150000*depth for stalemate

	}
	
	public int position(int material) {

		// takes material score, bitboard, type
		// if type is king and score is below a certain level use endgame
		// otherwise use midgame
		
	}
}

}
