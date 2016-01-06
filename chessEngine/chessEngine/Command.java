package chessEngine;

import java.util.LinkedList;
import java.util.List;

// Command class.
//		* This class represents the possible parameters and additional commands
//		* associated with the UCI 'go' command.
//		* Used to keep code cleaner.
//
// Methods
//		* searchMove
//
// Supporting classes
//
// COSC 3P71 Main Project, Fall 2015 
//	@author Willy Rempel		#9951674
//	@author Nick Seelert		#5304902


public class Command {
	
	public String				newCommand;			// new action thinker should do: go, stop, debug, setOptions, update via gameState
	public List<String>			searchMoves;		// engine instructed to search just these move subbranches
	public boolean				ponder;				// true if engine can enter ponder mode
	public int					wTime;				// time left for white
	public int					bTime;				// time left for black
	public int					wInc;				// white time increment/move in milliseconds
	public int					bInc;				// black time increment/move in milliseconds
	public int					movestoGo;			// x moves to next time control. Sudden death indicator (see uci spec)
	public int					depth;				// search x plies only
	public int					nodes;				// search x nodes only
	public int					mate;				// search for a mate in x moves 
	public int					moveTime;			// search exactly x millliseconds
	public boolean				infinite;			// search until receive 'stop' command
	
	// Constructor
	Command () {
		
		newCommand = null;
		searchMoves = new LinkedList<String>();
		ponder = false;
		infinite = false;
		int wTime =0, bTime =0, wInc =0, bInc =0, movestoGo =0, depth =0, nodes =0, mate =0, moveTime =0;
				
	}

	// This method adds a move to the list of moves to search
	public void searchMove(String aMove) {
		
		searchMoves.add(aMove);
		
	}
	
	// This method resets the Command object to inital state
	// Usually done when 'newgame' command issued.
	public void reset() {
		
		newCommand = null;
		searchMoves = new LinkedList<String>();
		ponder = false;
		infinite = false;
		wTime =0; bTime =0; wInc =0; bInc =0; movestoGo =0; depth =0; nodes =0; mate =0; moveTime =0;
		searchMoves.clear();
		
	}

}
