package chessEngine;

/*
 * Board will take a 2D string array with char representations of pieces.
 * This array is then converted to a bitboard representation. Class should
 * have option to take string array as input from user to permit any board 
 * initial setup.
 *  
 *  Piece representation on board lower case are black piece and upper case are white pieces
 *  
 * Based on Logic Crazy chess engine: https://www.youtube.com/channel/UCmMjMHTeUEBJJZhxix-N-yg
 */

public class BoardGen {
	
	// bitboards for pieces
	// P = pawn, N = night, B = bishop, R = rook, Q = queen, K = king
	long wP = 0L, wN = 0L, wB = 0L, wR = 0L, wQ = 0L, wK = 0L; // white
	long bP = 0L, bN = 0L, bB = 0L, bR = 0L, bQ = 0L, bK = 0L; // black

	public void initStdGame() {
		// TODO: test initStdGame
		// initiates a standard board setup for a new game
		// 
		
		String board[][] = {
				{"r","n","b","q","k","b","n","r"},
				{"p","p","p","p","p","p","p","p"},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{"P","P","P","P","P","P","P","P"},
				{"R","N","B","Q","K","B","N","R"}
		};
		
		arrayToBB(board);
		
	} // initStdGame()
	
	public void initUserBoard(String board[][]) {
		// TODO: update initUserBoard to take FEN string
		// TODO: test initUserBoard
		// initiates a board setup based on user input
		arrayToBB(board);
	}
	
	public void arrayToBB(String[][] board) {
		// TODO: test arrayToBB
		// converts string array to bitboard
		String bitboard;
		for (int i = 0; i < 64; i++) { // loop through each char in string array
			bitboard = "0000000000000000000000000000000000000000000000000000000000000000"; // 64bit string
			bitboard = bitboard.substring(i+1) + "1" + bitboard.substring(0, i);
			switch (board[i/8][i%8]) { // look at chars at each position in board
			case "r": // black rooks
				bR += parseString(bitboard);
				break;
			case "n": // black knights
				bN += parseString(bitboard);
				break;
			case "b": // black bishops
				bB += parseString(bitboard);
				break;
			case "q": // black queens
				bQ += parseString(bitboard);
				break;
			case "k": // black king
				bK += parseString(bitboard);
				break;
			case "p": // black pawns
				bP += parseString(bitboard);
				break;
			case "R": // white rooks
				wR += parseString(bitboard);
				break;
			case "N": // white knights
				wN += parseString(bitboard);
				break;
			case "B": // white bishops
				wB += parseString(bitboard);
				break;
			case "Q": // white queens
				wQ += parseString(bitboard);
				break;
			case "K": // white king
				wK += parseString(bitboard);
				break;
			case "P": // white pawns
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
}
