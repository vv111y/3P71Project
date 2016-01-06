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
//		@author Nick Seelert		TODO std#

class Thinker implements Runnable {
	
	private Thread 		cortex;				// thread object for engine code
	private Command		newCommand;			// reference to newCommand object
	private GameState	currentGame;		// reference to currentGame object

		 

	// Constructor. Engine initialization goes here
  	Thinker(Command newCommand, GameState currentGame) {

		this.newCommand = newCommand;
		this.currentGame = currentGame;		
		cortex = null;
		
	}


	// This method executes the code for the cortex thread
	// Cortex thread does the actual search & evaluation for best move
	public synchronized void run() {
		
		// New search tree
		SearchTree thisSearch = new SearchTree(this.currentGame);
		
		// once built go in wait & notify thinker
		//this.notifyAll();
		System.out.println(this.newCommand.bInc);
		
		
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
