package chessEngine;

/*
 * 
 * Evaluations:
 * 		material	sum(# of pieces player has on board)
 * 		moves	sum(# of moves possible for each piece)
 * 		position	sum(each pieces positional score) 
 * 		attack		-sum(# of pieces under attack)
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
		case "N": // knight
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
	
	// takes the unsafe bitboard and returns the number of unsafe positions
	public int attacks(long unsafeBoard) {
		return Long.bitCount(unsafeBoard);
	}
	
	
	// takes the moveList for the leaf node and returns the number of moves possible
	public int moves(String moveList) {
		int i = 0;
		int moveCnt = 0;
		
		while (i < moveList.length()) {
			moveCnt++;
			i += 4;
		}
		
		return moveCnt;
	}
	
	// takes bitboard and string for piece type
	public int position(long bitboard, String type, int material, boolean reverse) {
		int score = 0;
		int[] pcValues = null;
		
		if (reverse)
			Long.reverse(bitboard);
		
		switch (type) {
		case "P": // pawn
			pcValues = pawnScores;
			break;
		case "R": // rook
			pcValues = rookScores;
			break;
		case "N": // knight
			pcValues = knightScores;
			break;
		case "B": // bishop
			pcValues = bishopScores;
			break;
		case "Q": 
			pcValues = queenScores;
			break;
		case "K":
			// if player has less than 1400 pts of material then shift
			if (material < 1400) {
				pcValues = kingEndGame;
			} else {
				pcValues = kingMidGame;
			}
		}
		
		for (int i = 0; i < 64; i++) {
			if ((bitboard & (1L << i)) != 0) {
			   score += pcValues[i];
			}
		}
		
		return score;
	}
}

