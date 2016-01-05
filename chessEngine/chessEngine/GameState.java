package chessEngine;

import java.util.LinkedList;
import java.util.List;

// GameState class. 
//		* Represents the current game state
//		* String representation of the current board layout
//		* Other information describing state of the game 
//		* Note UCI is a stateless protocol - this class is not necessary but 
//		* aids in organizing information & available for future functionality
//
//Methods
//		* makeMove
//		* reset
//
// Supporting classes
//
// COSC 3P71 Main Project, Fall 2015 
//	@author Willy Rempel		#9951674
//	@author Nick Seelert		TODO std#

public class GameState {

	public volatile String[][]	currentBoard;		// character representation of the current board state TODO volatile?
	public List<String> 		gameHistory;		// list of all the moves in a game
	public List<String>			wOnBoard;			// list of white pieces on board
	public List<String>			wCaptured;			// list of white pieces that are captured
	public List<String>			bOnBoard;			// list of black pieces on board
	public List<String>			bCaptured;			// list of black pieces that are captured
	public int					wTime;				// time left for white
	public int					bTime;				// time left for black
	public String				max;				// w = engine is white, b = engine is black
	public boolean				wInCheck;			// true if white is in check
	public boolean 				bInCheck;			// true if black is in check
	public boolean				wCheckMate;			// true if white is checkmated
	public boolean				bCheckMate;			// true if black is checkmated
	public boolean				gameDraw;			// true if the game is a draw
	public boolean				wSCastle;			// whether white can short castle
	public boolean				wLCastle;			// whether white can long castle
	public boolean				bSCastle;			// whether black can short castle
	public boolean				bLCastle;			// whether black can long castle
	public String				enPassant;			// whether en Passant attack is available
	public int					halfMoves;			// The number of halfmoves since the last capture or pawn advance. For 50-move rule.
	public int					fullMoves;			// number of full moves since game began, increment after Black move
	

	// Constructor
	// Starts with an empty board and no pieces in any list. 
	// Timers set to 0, all flags to false, fullMove to 1, turn is whites.
	// Engine expects position command to populate initial board
	// and go command to set other game state parameters
	public GameState () {
		
		gameHistory = new LinkedList<String>();
		wOnBoard = new LinkedList<String>();
		wCaptured = new LinkedList<String>();
		bOnBoard = new LinkedList<String>();
		bCaptured = new LinkedList<String>();
		max = "w";
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
		enPassant = "-";
		halfMoves = 0;
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
	// UCI does not actually use LAN, but modified version
	// <mLAN move descriptor piece moves> ::= <from square>[''|'x']<to square>
	// <mLAN move descriptor pawn moves>  ::= <from square>[''|'x']<to square>[<promoted to>]
	public void makeMove(String aMove) {
		
		// check for castling first before breaking up move string
		// short castle
		if (aMove.equals("O-O")) {
			
			if (max.equals("w")) {
				
				// TODO make white short castle move
				return;

			} else {
				// make black short castle move
				return;
			}
			
		}
		
		// long castle
		if (aMove.equals("O-O-O")) {
			
			if (max.equals("w")) {
				
				// make white long castle move
				return;

			} else {
				// make black long castle move
				return;
			}
			
		}
		
		String[] moveTokens = aMove.split("(?!^)");		// see http://stackoverflow.com/questions/5235401/split-string-into-array-of-character-strings
		int fileFrom =0, fileTo =0;
		int rankFrom =0, rankTo =0;
		boolean capture = false;
		int offset =0;
		
		if (moveTokens[2].equalsIgnoreCase("x")) { 
			capture = true; 
			offset = 1;
		}
		
		fileFrom = parseFilePos(moveTokens[0]);		
		rankFrom = Integer.parseInt(moveTokens[1]);
		
		fileTo = parseFilePos (moveTokens[2 + offset]);
		rankTo = Integer.parseInt(moveTokens[3 + offset]);
		
		// what is located at given squares: a piece or empty
		String atFromSquare = currentBoard[rankFrom][fileFrom];
		String atToSquare = currentBoard[rankTo][fileTo];
		
		// l4xl5Q l4l5Q l4l5
		// Promotion case
		if (moveTokens.length > (4 + offset)) {
			
			//TODO
			//'N' | 'B' | 'R' | 'Q' | 'K'
			switch (moveTokens[4 + offset])
			{
			case "N":
				// promote to knight
				break;
			case "R":
			// promote to rook
			break;
			case "B":
			// promote to bishop
			break;
			case "Q":
			// promote to queen

			}
		}	
		
		// cases: move to empty, capture, promote empty, promote capture, 
		// distinguish bishops, but not knight or rook 
		// capture piece info
		// update strings in currentBoard
		
		// update pieces list
		
		// add move to move history list
		gameHistory.add(aMove);
	}
	
	
	private int parseFilePos (String token) {
		int file = 0;
		
		switch (token)
		{
		case "a":
			file =1;
			break;
		case "b":
			file =2;
			break;
		case "c":
			file=3;
			break;
		case "d":
			file =4;
			break;
		case "e":
			file =5;
			break;
		case "f":
			file =6;
			break;
		case "g":
			file =7;
			break;
		case "h":
			file=8;
			break;
		}
		
		return file;		
	}

	
	// This method resets all the game state parameters
	// The move history is cleared and previously the 
	// board should have the pieces returned to initial positions
	public void reset() {
		max = "w";
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
		enPassant = "-";
		halfMoves = 0;
		fullMoves = 1;
		
		gameHistory.clear();
		wOnBoard.clear();
		wCaptured.clear();
		bOnBoard.clear();
		bCaptured.clear();
		
	}
	

	
}
