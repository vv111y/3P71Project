package chessEngine;

/*
 * Board will take a 2D string array with char representations of pieces.
 * This array is then converted to a bitboard representation. Class should
 * have option to take string array as input from user to permit any board 
 * initial setup.
 *  
 * Based on Logic Crazy chess engine: https://www.youtube.com/channel/UCmMjMHTeUEBJJZhxix-N-yg
 */

public class BoardGen {
	
	// bitboards for pieces
	// P = pawn, N = night, B = bishop, R = rook, Q = queen, K = king
	long wP = 0L, wN = 0L, wB = 0L, wR = 0L, wQ = 0L, wK = 0L; // white
	long bP = 0L, bN = 0L, bB = 0L, bR = 0L, bQ = 0L, bK = 0L; // black

	public void initStdGame() {
		// TODO: test harness
		// initiates a standard board setup for a new game	
		
		String board[][] = {
				{"bR","bN","bB","bQ","bK","bB","bN","bR"},
				{"bP","bP","bP","bP","bP","bP","bP","bP"},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{"wR","wN","wB","wQ","wK","wB","wN","wR"},
				{"wP","wP","wP","wP","wP","wP","wP","wP"}
        };
		
		arrayToBB(board);
		
	} // initStdGame()
	
	public void initUserBoard(String board[][]) {
		// TODO: test harness
		// initiates a board setup based on user input
		arrayToBB(board);
	}
	
	public void arrayToBB(String[][] board) {
		// TODO: test harness
		// converts string array to bitboard
		String bitboard;
		for (int i = 0; i < 64; i++) { // loop through each char in string array
			bitboard = "0000000000000000000000000000000000000000000000000000000000000000"; // 64bit string
			bitboard = bitboard.substring(i+1) + "1" + bitboard.substring(0, i);
			switch (board[i/8][i%8]) { // look at chars at each position in board
			case "bR": // black rooks
				bR += parseString(bitboard);
				break;
			case "bN": // black knights
				bN += parseString(bitboard);
				break;
			case "bB": // black bishops
				bB += parseString(bitboard);
				break;
			case "bQ": // black queens
				bQ += parseString(bitboard);
				break;
			case "bK": // black king
				bK += parseString(bitboard);
				break;
			case "bP": // black pawns
				bP += parseString(bitboard);
				break;
			case "wR": // white rooks
				wR += parseString(bitboard);
				break;
			case "wN": // white knights
				wN += parseString(bitboard);
				break;
			case "wB": // white bishops
				wB += parseString(bitboard);
				break;
			case "wQ": // white queens
				wQ += parseString(bitboard);
				break;
			case "wK": // white king
				wK += parseString(bitboard);
				break;
			case "wP": // white pawns
				wP += parseString(bitboard);
				break;
			default:
				// TODO: Throw exception for bad board, unable to read char
				break;
			}
		}
	}
	
	public long parseString(String bitboard) {
		// TODO: test harness
		// parses a char string and returns a binary string
		if (bitboard.charAt(0) == '0') { // simple parse
			return Long.parseLong(bitboard, 2);
		} else { // 2s compliment conversion to prevent negative number
			return Long.parseLong("1" + bitboard.substring(2), 2) * 2;
		}
	}
}
