package chessEngine;

// UCI Protocol Implementation
//
// COSC 3P71 PROJECT
// December, 2015
// Willy Rempel		#9951674
// Nick Seelert		#

import java.util.Scanner;

public class UCI {

	private static String		engineName = "Seelert&Rempel Engine v1";
	private static String		author = "Willy Rempel & Nick Seelert";
	private String				inputString;
	
	public UCI () {
		
        Scanner input = new Scanner(System.in);
 
        while (true)
        {
            inputString = input.nextLine();
            
            if (inputString.equals("uci"))
            {
                commUCI();
            }
            else if (inputString.startsWith("setoption"))
            {
                setOptions(inputString);
            }
            else if (inputString.equals("isready"))
            {
                commIsReady();
            }
            else if (inputString.equals("ucinewgame"))
            {
                commUCINewGame();
            }
            else if (inputString.startsWith("position"))
            {
                commPosition(inputString);
            }
            else if (inputString.startsWith("go"))
            {
            	commGo();
            }
            else if (inputString.equals("quit"))			// Close Engine.
            {
                input.close();
	            System.exit(0);
            }
        }           
	}


	// Start calculating on the current position set up with the "position" command.
	// There are a number of commands that can follow this command, all will be sent in the same string.
	private void commGo() {
		// TODO Auto-generated method stub
		
	}

	// set up the position described in fenstring on the internal board and
	// play the moves on the internal chess board.
	// If the game was played from the start position the string "startpos" will be sent.
	private void commPosition(String inputString) {
		// TODO Auto-generated method stub
		
	}

	// This is sent to the engine when the next search (started with "position" and "go") will be from
	// a different game. This can be a new game the engine should play or a new game it should analyse but
	// also the next position from a testsuite with positions only.
	private void commUCINewGame() {
		// TODO Auto-generated method stub
		
	}

	//	This is used to synchronize the engine with the GUI. When the GUI has sent a command or
	//	multiple commands that can take some time to complete,
	//	this command can be used to wait for the engine to be ready again or
	//	to ping the engine to find out if it is still alive.
	private void commIsReady() {
		// TODO Auto-generated method stub
		
	}

	// This method changes the options available for the engine.
	private void setOptions(String inputString) {
		// TODO Auto-generated method stub
		
	}

	// Tell the engine to use the UCI protocol
	// Engine responds with id, author, options list, and 'uciok'
	private void commUCI() {
		System.out.println("id name " + engineName);
		System.out.println("id author " + author);
		System.out.println("options ");
		System.out.println("uciok");
		
	}
	
}
