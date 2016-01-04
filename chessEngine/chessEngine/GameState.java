package chessEngine;

import java.util.LinkedList;
import java.util.List;

// GameState class. 
//		* Represents the current game state
//		* String representation of the current board layout
//		* Other information describing state of the game 
//		* 
//
//Methods
//		* makeMove
//
// Supporting classes
//
// COSC 3P71 Main Project, Fall 2015 
//	@author Willy Rempel		#9951674
//	@author Nick Seelert		TODO std#

public class GameState {

	public volatile String[][]	currentBoard;		// character representation of the current board state
	private List<String> 		gameHistory;		// list of all the moves in a game
	public int					wTime;				// time left for white
	public int					bTime;				// time left for black
	public String				whoTurn;			// w = whites turn, b = blacks turn
	public boolean				wInCheck;			// true if white is in check
	public boolean 				bInCheck;			// true if black is in check
	public boolean				wCheckMate;			// true if white is checkmated
	public boolean				bCheckMate;			// true if black is checkmated
	public boolean				gameDraw;			// true if the game is a draw
	public boolean				wSCastle;			// whether white can short castle
	public boolean				wLCastle;			// whether white can long castle
	public boolean				bSCastle;			// whether black can short castle
	public boolean				bLCastle;			// whether black can long castle
	public boolean				enPassant;			// whether en Passant attack is available
	public int					plyCount;			// number of plys since game began
	public int					fullMoves;			// number of full moves since game began
	//TODO fen state stuff
	// fenParts[2] who's move w | b
	// fenParts[3] castling availability
	// fenParts[4] en passant target square, none = "-"
	// fenParts[5] halfmove clock. This is the number of halfmoves since the last capture or pawn advance. 
	//            This is used to determine if a draw can be claimed under the fifty-move rule.
	// fenParts[6] fullmove number. starts 1, increment after Black move
				
	
	
	// Constructor
	public GameState () {
		
		gameHistory = new LinkedList<String>();
		whoTurn = "w";
		wTime = 0; bTime = 0;
		wInCheck = false;
		bInCheck = false;
		wCheckMate = false;
		bCheckMate = false;
		gameDraw = false;
		wSCastle = false;
		wLCastle = false;
		bSCastle = false;
		bLCastle = false;
		enPassant = false;
		plyCount = 0;
		fullMoves = 1;
		
		currentBoard = new String[][] {
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "}};
	}
	
	
	// This method makes a move on the current board
	// called by Player.commPosition
	public void makeMove(String aMove) {
		
		//TODO move the piece on the currentBoard rep
		
		// add move to move history list
		gameHistory.add(aMove);
	}

	// This method resets all the game state parameters
	// The move history is cleared and previously the 
	// board should have the pieces returned to initial positions
	public void reset() {
		whoTurn = "w";
		wTime = 0; bTime = 0;
		wInCheck = false;
		bInCheck = false;
		wCheckMate = false;
		bCheckMate = false;
		gameDraw = false;
		wSCastle = true;
		wLCastle = true;
		bSCastle = true;
		bLCastle = true;
		enPassant = false;
		plyCount = 0;
		fullMoves = 1;
		
		gameHistory.clear();
	}
	
	// This method places a single piece on the current board
	// Used by FEN position command
	// @param 	piece 	piece using FEN notation
	// 			rank	the row location
	//			file 	the column location
	//	public void placePiece(char piece, int rank, int file) {
	//		
	//		// TODO build currentBoard piece by piece
	//	}
	
}
