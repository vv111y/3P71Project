package chessEngine;

public class BoardNode {
	BoardGen			currentBoard; 		// initial board configuration
	long 				bestMove; 			// best move
	long				unsafeMoves;		// board for all unsafe moves
	int 				score; 				// evaluation of board
	int 				alpha, beta; 		// search bounds for board
	int					halfMoves;			// The number of halfmoves since the last capture or pawn advance. For 50-move rule.
	int					fullMoves;			// number of full moves since game began, increment after Black move
	boolean				wSCastle;			// whether white can short castle
	boolean				wLCastle;			// whether white can long castle
	boolean				bSCastle;			// whether black can short castle
	boolean				bLCastle;			// whether black can long castle
	String 				colour; 			// player colour to track which moveGen to call first
	String 				moveList; 			// list of possibleMoves
	String				enPassant;			// whether en Passant attack is available
	
	
	/*
	 * Constructors
	 */
	public BoardNode() { // set start board for node
		this.currentBoard = new BoardGen();
		currentBoard.initBoard(GameState.currentBoard); // initiate bitboard with string from GameState()
		this.wSCastle = GameState.wSCastle; // initiate all castling rights
		this.wLCastle = GameState.wLCastle;
		this.bSCastle = GameState.bSCastle;
		this.bLCastle = GameState.bLCastle;
		this.halfMoves = GameState.halfMoves;
		this.fullMoves = GameState.fullMoves;
		colour = GameState.max;
		
		// TODO how is en passant string formatted??
		
		
	}
	
	/*
	 * Methods
	 */
	
	public void getUnsafe() {
		if (colour = "W") { // get unsafe for white
			unsafeMoves = moveGen.unsafe(false, currentBoard.bP, currentBoard.bN, currentBoard.bB, currentBoard.bR, currentBoard.bQ, currentBoard.bK);
			unsafeMoves &= (currentBoard.bP | currentBoard.bN | currentBoard.bB | currentBoard.bR | currentBoard.bQ | currentBoard.bK);
		} else { // get unsafe for black
			unsafeMoves = moveGen.unsafe(true, currentBoard.wP, currentBoard.wN, currentBoard.wB, currentBoard.wR, currentBoard.wQ, currentBoard.wK);
			unsafeMoves &= (currentBoard.wP | currentBoard.wN | currentBoard.wB | currentBoard.wR | currentBoard.wQ | currentBoard.wK);
		}
	}
	
	
	/*
	 * Setters
	 */
	public void setBest(long bitboard) { // set best move for starting board
		bestMove = bitboard;
	}
	
	public void setScore(int eval) { // set evaluation score for start board
		score = eval;
	}
	
	public void setBounds(int a, int b) { // set pruning bounds for start board
		alpha = a;
		beta = b;
	}
	
	
	/*
	 * Getters
	 */
	public long getBest() { // get best move for starting board
		return bestMove;
	}
	
	public int getScore() { // get evaluation score for start board
		return score;
	}
	
	public int getAlpha() { // get pruning bounds for start board
		return alpha;
	}
	
	public int getBeta() { // get pruning bounds for start board
		return beta;
	}
}