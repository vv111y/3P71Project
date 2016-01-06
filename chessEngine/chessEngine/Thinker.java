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
//		* bestMove
//		* sendInfo
//		* makeMove
//		* TODO ??interrupt code
//
// Supporting classes
//		* Command
//		* GameState
// 		* BoardGen
//		* GenerateMoves
//		* MakeMoves
//
// Exceptions & Interrupts
//		* TODO stop command
//		* ALL code end with cortex.notifyAll()
//
// COSC 3P71 Main Project, Fall 2015 
// 		@author Willy Rempel		#9951674
//		@author Nick Seelert		TODO std#

class Thinker implements Runnable {
	
	public final Thread 		cortex;				// thread object for engine code
	public volatile boolean 	cortexReady;		// flag to indicate whether cortex is not busy
	public volatile boolean		terminate;			// flag to close thread
	public volatile boolean 	stopThinking;		// flag to stop cortex from searching
	private 
	private Command				newCommand;			// reference to newCommand object
	private GameState			currentGame;		// reference to currentGame object

		 

	// Constructor. Engine initialization goes here
	//	- new thread started. based on code in run method
  	Thinker(Command newCommand, GameState currentGame) {

		this.newCommand = newCommand;
		this.currentGame = currentGame;	
		// other thinking parameters. Likely from Command object
		
		}

	
	// This method starts a new search thread. Could be for 
  	// the next turn to search, or a new game.
  	// The difference is resolved in the GameState object.
	public void newSearch() {


	}



	// This method executes the code for the cortex thread
	// Cortex thread does the actual search & evaluation for best move
	public synchronized void run() {
		
		// New search tree
		SearchTree thisSearch = new SearchTree();
		
		// once built go in wait & notify thinker
		//this.notifyAll();
		
		
		
		// wait on thinker in try/catch block
		
		// then go. (?more updates), in try/catch or loop with interrupt checking
		// either finish/output/terminate OR interrupt then ditto
		
		
		
		while (!Thread.interrupted()) {		
			try {
				
				// TODO SEARCH&EVAL CODE GOES HERE
				// new searchtree use gamestate
				// wait state
				// go command parse
				// searchtree.startsearch()
				
				// throw InterruptedException somewhere
				// in search loop test cortex.interrupted() to check if interrupt has occurred
//				if (Thread.interrupted()) throw notified;				
//				while (true) {	

//					System.out.println("this is coming from the cortex");
//					Thread.sleep(5000);
//				}
				
			} catch (InterruptedException notified) {
				
				// TODO is this right?
				System.out.println("bestmove " + SearchTree.bestMove());
			    Thread.currentThread().interrupt();
			    return;
								
			}	
		}
	}
	

	
	// This method terminates the cortex thread. 
	// Cortex thread will output bestmove
	public void stop() {
		
		cortex.interrupt();
	
		// give a little time to output
		try { wait(50); } catch (InterruptedException doNothing) {}
		
	}


	// This method starts the thinker thinking via the cortex thread. 
	// The command object parameters are used to specify the type
	// of search. 
	public void go() {
		
		// parse command object for search params & set them
		
		terminate = false;
		cortexReady = false;
		stopThinking = true;
		
		// start searching
		
		cortex = new Thread(this);
		cortex.start();
		
		
	}


	
}
