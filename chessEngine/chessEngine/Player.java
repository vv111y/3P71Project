package chessEngine;
import java.util.Scanner;

// Player class. 
//		* Main program loop
//		* Implements the UCI protocol
//		* Initiates object thinker, newCommand, currentGame 
//		* Parses commands, sends information & commands to thinker
//		* thinker does the actual search & evaluation
//		* Method comments from UCI specification (for further information): 
//		  http://www.shredderchess.com/chess-info/features/uci-universal-chess-interface.html
//
// Methods
//		* commGo
//		* commIsReady
//		* commPosition
//		* commStop
//		* commUCI
//		* commNewGame
//		* commPosition
//		* commStop
//		* commUCI
//		* setOptions
//
// Supporting classes
//		* Thinker
//		* GameState
//		* Command
//
// Exceptions & Interrupts
//		* 
//
// COSC 3P71 Main Project, Fall 2015 
// 		@author Willy Rempel		#9951674
// 		@author Nick Seelert		#5304902


public class Player {
	
	private static final String		engineName = "ThePlayer Engine";
	private static final String		version = "v1.0";
	private static final String		author = "Willy Rempel & Nick Seelert";
	private Scanner					input;
	private String					inputString;
	private Thinker					thinker;		
	private Command					newCommand; 	// command object to pass info between player & thinker
	private GameState				currentGame;	// game object to represent current game state
	
	
	// The constructor includes the main input & parsing loop.
	Player () {
		
		
	    newCommand = new Command();				
	    currentGame = new GameState();			
	    thinker = new Thinker(newCommand, currentGame);				// Start engine thread
	    input = new Scanner(System.in);		
	    
	    // Main input loop. Parses initial part of the input command string
	    // Exits when receiving the 'quit' command.
	    while (true)
	    {
	        inputString = input.next();
	        
	        if (inputString.equals("uci"))
	        {
	            commUCI();
	        }
	        else if (inputString.equals("setoption"))
	        {
	        	if (input.hasNext()) {
	        		inputString = input.nextLine();
	        	}
	        	setOptions(inputString);
	        }
	        else if (inputString.equals("register"))
	        {
	        	// registering this engine. FUTURE USE
	        }
	        else if (inputString.equals("isready"))
	        {
	            commIsReady();
	        }
	        else if (inputString.equals("ucinewgame"))
	        {
	            commNewGame();
	        }
	        else if (inputString.startsWith("position"))
	        {
	        	if (input.hasNext()) {
	        		inputString = input.nextLine();
	        	}
	        	commPosition(inputString);
	        }
	        else if (inputString.startsWith("go"))
	        {
	        	if (input.hasNext()) {
	        		inputString = input.nextLine();
	        	}
	        	commGo(inputString);
	        }
	        else if (inputString.equals("stop"))
	        {
	        	commStop();
	        }
	        else if (inputString.equals("debug")) 
	        {
	        	String mode= null;
	        	if (input.hasNext()) {
	        	mode = input.next();
	        	}
	        	
	        	if (mode.equals("on")) {
	        		
	        		// set debug mode to on
	        		// FUTURE USE
	        	} else if (mode.equals("off")) {
	        		
	        		// set debug mode to off
	        	} else {
	        		System.out.println("Malformed debug command.");
	        	}
	        }
	        else if (inputString.equals("ponderhit"))
	        {
	        	// set ponderhit flag true. Only relevant when in ponder mode.
	        	// FUTURE USE
	        }
	        else if (inputString.equals("quit"))			
	        {
	        	// Close Engine.
	        	input.close();
	            System.exit(0);
	        }
	    } 	
	}

	// Stop calculating as soon as possible, don't forget the "bestmove" and 
	// possibly the "ponder" token when finishing the search          
	private void commStop() {

		thinker.stop();

	}
	
	
	// Start calculating on the current position set up with the "position" command.
	// There are a number of commands that can follow this command, all will be sent in the same string.
	private void commGo(String inputArgs) {

		inputArgs.trim();
		String[] goArgs = inputArgs.split(" ");
		
		for (int i = 0; i < goArgs.length; i++) {
			
			try {
				
				// searchmoves functionality skipped for now
//				if (goArgs[i].equals("searchmoves")) 
//				{
//					
//					for (int j = 0; j < goArgs.length; j++) {
//						
//						newCommand.searchMove(goArgs[j]);					
//					} 
//				}
				if (goArgs[i].equals("ponder")) 
				{
					newCommand.ponder = true;
				}
				else if (goArgs[i].equals("wtime"))
				{
					newCommand.wTime = Integer.parseInt(goArgs[i+1]);
					currentGame.wTime = Integer.parseInt(goArgs[i+1]);
				}
				else if (goArgs[i].equals("btime")) 
				{
					newCommand.bTime = Integer.parseInt(goArgs[i+1]);
					currentGame.bTime = Integer.parseInt(goArgs[i+1]);					
				}
				else if (goArgs[i].equals("winc")) 
				{
					newCommand.wInc = Integer.parseInt(goArgs[i+1]);
				}
				else if (goArgs[i].equals("binc")) 
				{
					newCommand.bInc = Integer.parseInt(goArgs[i+1]);
				}
				else if (goArgs[i].equals("movestogo")) 
				{
					newCommand.movestoGo = Integer.parseInt(goArgs[i+1]);
				}				
				else if (goArgs[i].equals("depth")) 
				{
					newCommand.depth = Integer.parseInt(goArgs[i+1]);
				}
				else if (goArgs[i].equals("nodes")) 
				{
					newCommand.nodes = Integer.parseInt(goArgs[i+1]);
				}				
				else if (goArgs[i].equals("mate")) 
				{
					newCommand.mate = Integer.parseInt(goArgs[i+1]);
				}				
				else if (goArgs[i].equals("movetime")) 
				{
					newCommand.moveTime = Integer.parseInt(goArgs[i+1]);
				}				
				else if (goArgs[i].equals("infinite")) 
				{
					newCommand.infinite = true;
				}				
				
			} catch (ArrayIndexOutOfBoundsException e) {
				
				System.out.println("Malformed 'go' command. Please try again.");
			}
		}
		
		thinker.go();
	}

	
	// set up the position described in fenstring on the internal board and
	// play the moves on the internal chess board.
	// If the game was played from the start position the string "startpos" will be sent.
	private void commPosition(String inputArgs) {

		String[] positionArgs = inputArgs.split(" ");
							
		if (positionArgs[1].equals("startpos")) {
			
			// using a truncated fen string for starting positions. 
			String restartFEN = "fen rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
			parseFEN(restartFEN);
			
		} else if (positionArgs[1].startsWith("fen")) {
			 
			parseFEN(positionArgs[1]);
			
		} else {
			
			System.out.println("Malformed position command.");
		}

		// parse and play moves list
		if (positionArgs[2].equals("moves")) {
			
			for (int j = 2; j < positionArgs.length; j++) {
				
				currentGame.makeMove(positionArgs[j]); 
				
			}
		}	
	}

	
	// This method parses a FEN string and adjusts game settings accordingly
	private void parseFEN(String string) {
		
		String[] fenParts = string.split(" ");		
		//String fenParts = fenParts[1];
		
		int rank = 7; 
		int file = 0;
		
		for (int i = 0; i < fenParts[1].length(); i++) {
			
			switch (fenParts[1].charAt(i)) 
			{
			case 'r':
				// white rook
				currentGame.currentBoard[rank][file] = "r";
				currentGame.wOnBoard.add("r");
				file++;
				break;
			case 'n':
				// white knight
				currentGame.currentBoard[rank][file] = "n";
				currentGame.wOnBoard.add("n");
				file++;
				break;
			case 'b':
				// white bishop
				currentGame.currentBoard[rank][file] = "b";
				currentGame.wOnBoard.add("b");
				file++;
				break;
			case 'q':
				// white queen
				currentGame.currentBoard[rank][file] = "q";
				currentGame.wOnBoard.add("q");
				file++;
				break;
			case 'k':
				// white king
				currentGame.currentBoard[rank][file] = "k";
				currentGame.wOnBoard.add("k");
				file++;
				break;
			case 'p':
				// white pawn
				currentGame.currentBoard[rank][file] = "p";
				currentGame.wOnBoard.add("p");
				file++;
				break;
			case 'R':
				// black rook
				currentGame.currentBoard[rank][file] = "R";
				currentGame.bOnBoard.add("R");
				file++;
				break;
			case 'N':
				// black knight
				currentGame.currentBoard[rank][file] = "N";
				currentGame.bOnBoard.add("N");
				file++;
				break;
			case 'B':
				// black bishop
				currentGame.currentBoard[rank][file] = "B";
				currentGame.bOnBoard.add("B");
				file++;
				break;
			case 'Q':
				// black queen
				currentGame.currentBoard[rank][file] = "Q";
				currentGame.bOnBoard.add("Q");
				file++;
				break;
			case 'K':
				// black king
				currentGame.currentBoard[rank][file] = "K";
				currentGame.bOnBoard.add("K");
				file++;
				break;
			case 'P':
				// black pawn
				currentGame.currentBoard[rank][file] = "P";
				currentGame.bOnBoard.add("P");
				file++;
				break;
			case '1':
				currentGame.currentBoard[rank][file] = " ";
				file++;
				break;
			case '2':
				while (file < (file + 2)) { 
					currentGame.currentBoard[rank][file] = " ";
					file++;
				} 
				break;
			case '3':
				while (file < (file + 3)) { 
					currentGame.currentBoard[rank][file] = " ";
					file++;
				} 				
				break;
			case '4':
				while (file < (file + 4)) { 
					currentGame.currentBoard[rank][file] = " ";
					file++;
				} 				
				break;
			case '5':
				while (file < (file + 5)) { 
					currentGame.currentBoard[rank][file] = " ";
					file++;
				} 				
				break;
			case '6':
				while (file < (file + 6)) { 
					currentGame.currentBoard[rank][file] = " ";
					file++;
				} 				
				break;
			case '7':
				while (file < (file + 7)) { 
					currentGame.currentBoard[rank][file] = " ";
					file++;
				} 				
				break;
			case '8':
				while (file < 8) { 
					currentGame.currentBoard[rank][file] = " ";
					file++;
				} 
				break;
			case '/':
				// next rank, reset file
				rank--;
				file = 0;
				break;
			}
		}
		
		// if this is not a position startpos command, then parse rest of fen string
		if (fenParts.length>3) {			
	
			// fenParts[2] who's move w | b
			currentGame.max = fenParts[2];
			
			// fenParts[3] castling availability
			for (int i = 0; i < fenParts[3].length(); i++) {
				
				if (fenParts[3].substring(i, i+1) == "K") 
				{	
					currentGame.bSCastle = true;
				}
				else if (fenParts[3].substring(i, i+1) == "Q")
				{
					currentGame.bLCastle = true;
				}
				else if (fenParts[3].substring(i, i+1) == "k")
				{
					currentGame.wSCastle = true;
				}
				else if (fenParts[3].substring(i, i+1) == "q")
				{
					currentGame.wLCastle = true;
				}
					
				
			}
			
			// fenParts[4] en passant target square, none = "-"
			currentGame.enPassant = fenParts[4];
			
			// fenParts[5] halfmove clock.  
			//            This is used to determine if a draw can be claimed under the fifty-move rule.
			currentGame.halfMoves = Integer.parseInt(fenParts[5]);
			
			// fenParts[6] fullmove number. starts 1, increment after Black move
			currentGame.fullMoves = Integer.parseInt(fenParts[6]); 
			
		}
		

		
	}

	
	// This is sent to the engine when the next search (started with "position" and "go") will be from
	// a different game. This can be a new game the engine should play or a new game it should analyse but
	// also the next position from a testsuite with positions only.
	private void commNewGame() {
		
		commPosition("startpos");
		currentGame.reset();
		newCommand.reset();
		
	}

	
	// This is used to synchronize the engine with the GUI. 
	// This command can be used to wait for the engine to be ready again or
	// to ping the engine to find out if it is still alive.
	// Required to be sent before the engine is asked to do any search.
	// Response can be sent while engine continues searching
	private void commIsReady() {
				
		System.out.println("readyok");
		
	}

	
	// This method changes the options available for the engine.
	// Currently only parses for plydepth
	private void setOptions(String optionArgs) {
		optionArgs.toLowerCase();
		String[] thisOption = optionArgs.split(" ");
		
		for (int i = 0; i < thisOption.length; i++) {
			
			if (thisOption[i].equals("plydepth")) {
				
				newCommand.depth = Integer.parseInt(thisOption[i + 2]);
			}
		}
	}

	
	// Tell the engine to use the UCI protocol
	// Engine responds with id, author, options list, and 'uciok'
	private void commUCI() {
		System.out.println("id name " + engineName + " " + version);
		System.out.println("id author " + author);
		System.out.println("option name plydepth type spin default 4 min 2 max 10");
		System.out.println("uciok");
		
	}
	
	
	// MAIN method
	public static void main(String[] args) {
		try { new Player(); } catch ( Throwable e ) { e.printStackTrace(); }
	}
}
