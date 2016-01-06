package chessEngine;

import java.util.LinkedList;
import java.util.List;

// This class represents the main Engine object
//		* A separate thread 'cortex' performs the search and evaluation functions.
//		  This allows the engine to be able to receive more commands while it is 
//		  working. 
// 		* Allows timed control of search & evaluation
//
// Methods
//		* run
//		* go
//		* stop
//		* newSearch
//
// Supporting classes
//		* Command
//		* GameState
// 		* BoardGen
//		* GenerateMoves
//		* MakeMoves
//
// Exceptions & Interrupts
//		* stop command
//		* ALL code end with cortex.notifyAll()
//
// COSC 3P71 Main Project, Fall 2015 
// 		@author Willy Rempel		#9951674
//		@author Nick Seelert		#5304902

class Thinker implements Runnable {
	
	private Thread 		cortex;				// thread object for engine code
	private Command		newCommand;			// reference to newCommand object
	private GameState	currentGame;		// reference to currentGame object
	private volatile boolean stopSearch;	// terminate a search in progress
	private volatile String bestMove;

		 

	// Constructor. Engine initialization goes here
  	Thinker(Command newCommand, GameState currentGame) {

		this.newCommand = newCommand;
		this.currentGame = currentGame;		
		cortex = null;
		stopSearch = false;
		
	}


	// This method executes the code for the cortex thread
	// Cortex thread does the actual search & evaluation for best move
	public synchronized void run() {
		
		// TODO stopSearch is another arg. put it in your main while loop and break
		// if true. 
		SearchTree thisSearch = new SearchTree(this.currentGame, this.newCommand.depth);
		bestMove = thisSearch.alphaBeta();
		System.out.println(bestMove);
	}
	

	
	// This method terminates the cortex thread. 
	// Cortex thread will output bestmove
	public void stop() {
		
		stopSearch = true;
		
	}


	// This method starts the thinker thinking via the cortex thread. 
	// The command object parameters are used to specify the type
	// of search. 
	public void go() {
		
		// parse command object for search params & set them
		
		// start searching
		cortex = new Thread(this);
		cortex.start();
		
		// timed search. Wait and then force stop
		if (newCommand.moveTime > 0) { 
			
			try {
				Thread.sleep(newCommand.moveTime);
			} catch (InterruptedException e) { }
			
			this.stop();
		}
		
		
	}


	
}
