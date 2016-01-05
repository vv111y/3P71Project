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
	public String				enPassant;			// whether en Passant attack is available
	public int					halfMoves;			// The number of halfmoves since the last capture or pawn advance. For 50-move rule.
	public int					fullMoves;			// number of full moves since game began, increment after Black move
	

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
		
		// l4xl5Q l4l5Q l4l5
		// Promotion case
		if (moveTokens.length > (4 + offset)) {
			
			//TODO
			//'N' | 'B' | 'R' | 'Q' | 'K'
			
		}
		
		//TODO
		// castling
		
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
		enPassant = "-";
		halfMoves = 0;
		fullMoves = 1;
		
		gameHistory.clear();
	}
	

	
}
