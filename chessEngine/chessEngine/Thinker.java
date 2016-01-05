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
//		* TODO all the other classes: boards, etc
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
	private Command				newCommand;			// reference to newCommand object
	private GameState			currentGame;		// reference to currentGame object

		 

	// Constructor. Engine initialization goes here
	//	- new thread started. based on code in run method
	//  - TODO other initializations: boards, evaluators, etc
  	Thinker(Command newCommand, GameState currentGame) {

		terminate = false;
		cortexReady = false;
		stopThinking = true;
		
		this.newCommand = newCommand;
		this.currentGame = currentGame;
		
		cortex = new Thread(this);
		cortex.start();

		}

	
	
	// This method executes the code for the cortex thread
	// Cortex thread is the actual AI engine
	public synchronized void run() {

		InterruptedException notified = new InterruptedException();
		
		while (true) {		
			try {
				
				// TODO SEARCH&EVAL CODE GOES HERE
				// all code here is run from within the cortex thread
				// throw InterruptedException somewhere
				// in search loop test cortex.interrupted() to check if interrupt has occurred
				// set flag cortexReady = false before crunching
									
				if (Thread.interrupted()) throw notified;				
				while (true) {	

//					System.out.println("this is coming from the cortex");
//					Thread.sleep(5000);
				}
				
			} catch (InterruptedException e) {
				
				// TODO need stop code here
				// make sure bestmove is set
				// gracefully finish work
				
				if (terminate) {
					
					stop();
					break;
					
				} else if (stopThinking) {
					
					bestMove();
					cortexReady = true;
					stopThinking = false;
					cortex.notifyAll();	
				}
			}	
		}
	}
	

	// This method returns the engines chosen move.	
	// 
	public void bestMove() {
		String bestMove = "";
		// TODO return the best move available & also ponder move2

		System.out.println("bestmove " + bestMove);
		notifyAll();
		// goto wait state ie. newCommand.wait()
		
		//return bestMove;
	}

	
	// This method outputs variety of information to the chess program
	// about the engines analysis while it is thinking.
	// Called from within the run method by cortex thread.
	private void sendInfo() {
		
		// TODO any info we decide to send. see uci spec
		// likely put in run. may need to give cortex 
		// the main threads System.out
		
	}
	
	
	// This method terminates the thread and cleans up
	// Ignoring exception as not vital to closing executable.
	public void stop() {
		
		try {
			cortex.join();
		} catch (InterruptedException e) {}		
	}
	
}
