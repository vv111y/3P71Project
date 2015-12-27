package chessEngine;

// UCI protocol implementation

import java.util.Scanner;

public class UCI {

	private static String		engineName = "Seelert&Rempel Engine v1";
	private static String		author = "Willy Rempel & Nick Seelert";
	
	public UCI () {
		
        Scanner input = new Scanner(System.in);
 
        while (true)
        {
            String inputString=input.nextLine();
            
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
            else if (inputString.equals("quit"))
            {
                commQuit();
            }
            else if (inputString.equals("print"))
            {
                commPrint();
            }
        }
	}

	private void commPrint() {
		// TODO Auto-generated method stub
		
	}

	private void commQuit() {
		System.exit(0);
		
	}

	private void commGo() {
		// TODO Auto-generated method stub
		
	}

	private void commPosition(String inputString) {
		// TODO Auto-generated method stub
		
	}

	private void commUCINewGame() {
		// TODO Auto-generated method stub
		
	}

	private void commIsReady() {
		// TODO Auto-generated method stub
		
	}

	private void setOptions(String inputString) {
		// TODO Auto-generated method stub
		
	}

	private void commUCI() {
		// TODO Auto-generated method stub
		
	}
	
}
