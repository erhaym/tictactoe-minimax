import java.util.ArrayList;
import java.util.Map;

public class GameState {
	char[][] board;
	
	/*
		The value 0 is assigned to the first player, 1 is assigned to the second
		To update the turn, after a player plays, we XOR the previous value
	*/
	int turn;

	// FOR NOW X IS THE DEFAULT MAXIMIZING PLAYER
	Map<Integer, Character> turnToMark = Map.of(
		0, 'X',
		1, 'O'
	);

	char minimizingPlayer;

	char maximizingPlayer;

	public GameState(char[][] board, int turn, char minimizingPlayer, char maximizingPlayer) {
		this.board = new char[board.length][board[0].length];

		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				this.board[row][col] = board[row][col];
			}
		}

		this.turn = turn;

		this.minimizingPlayer = minimizingPlayer;
		this.maximizingPlayer = maximizingPlayer;
	}

	public ArrayList<Move> getAvailableMoves() {
		ArrayList<Move> availableMoves = new ArrayList<>();

		char winner = getWinner();

		if (winner != ' ') {
			// We return the empty list
			return availableMoves;
		}

		for (int row = 0; row < this.board.length; row++) {
			for (int col = 0; col < this.board[0].length; col++) {
				if (board[row][col] == ' ') {
					availableMoves.add(new Move(row, col));
				}
			}
		}

		return availableMoves;
	}

	public GameState makeMove(Move move) {
		if (board[move.row][move.col] != ' ') {
			System.out.println("Invalid coordinates.");
			return this;
		}

		char[][] updatedBoard = new char[this.board.length][this.board[0].length];

		for (int row = 0; row < this.board.length; row++) {
			for (int col = 0; col < this.board[0].length; col++) {
				if ((row == move.row) && (col == move.col)) {
					updatedBoard[row][col] = turnToMark.get(turn);
					continue;
				}
				updatedBoard[row][col] = this.board[row][col];
			}
		}

		// XOR
		int updatedTurn = turn ^ 1;

		return new GameState(updatedBoard, updatedTurn, minimizingPlayer, maximizingPlayer);
	}

	public char getWinner() {
		// Check columns
		for (int col = 0; col < this.board[0].length; col++) {
			if ((this.board[0][col] != ' ') 
				&& (this.board[0][col] == this.board[1][col]) 
				&& (this.board[1][col] == this.board[2][col])) {
				return this.board[0][col];
			}
		}

		// Check rows
		for (int row = 0; row < this.board[0].length; row++) {
			if ((this.board[row][0] != ' ') 
				&& (this.board[row][0] == this.board[row][1]) 
				&& (this.board[row][1] == this.board[row][2])) {
				return this.board[row][0];
			}
		}

		// Check diagonals
		if ((this.board[0][0] != ' ') 
			&& (this.board[0][0] == this.board[1][1]) 
			&& (this.board[1][1] == this.board[2][2])) {
			return this.board[0][0];
		}
		if ((this.board[0][2] != ' ') 
			&& (this.board[0][2] == this.board[1][1]) 
			&& (this.board[1][1] == this.board[2][0])) {
			return this.board[0][2];
		}

		// No winner found at this state
		return ' ';
	}

	public boolean isTerminal() {
		return getAvailableMoves().isEmpty();
	}

	// -------------------------------------------------------------------

	/* 
		THIS FUNCTION MISBEHAVES* IF IT IS CALLED IN NON TERMINAL STATE 
		A BETTER CHOICE DESIGN-WISE WOULD BE TO PROHIBIT THIS CASE 
		
		*IT RETURNS 0 IF GAME HAS NOT FNISHED 
		(SO IT DOESN'T ONLY RETURN 0 FOR A DRAW...)
	*/
	// public int evaluate(char maximizingPlayer) {
	public int evaluate() {
		if (!isTerminal()) {
			throw new IllegalArgumentException("Error: Cannot call evaluate function on non-terminal state.\nCall scoreState() instead.");
		}

		// char maximizingPlayer = 'X';
		// char minimizingPlayer = 'O';

		char winner = getWinner();

		if (winner == maximizingPlayer) {
			return 1;
		} else if (winner == minimizingPlayer) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * Get all available next states
	 * */
	public ArrayList<GameState> getNextStates() {
		if (isTerminal()) {
			// return empty ArrayList
			return new ArrayList<>();
		}

		ArrayList<GameState> nextStates = new ArrayList<>();

		ArrayList<Move> availableMoves = getAvailableMoves();

		for (Move move : availableMoves) {
			GameState nextState = makeMove(move);
			nextStates.add(nextState);
		}

		return nextStates;
	}

	public void printAllNextStates(int depth) {
		for (GameState nextState : getNextStates()) {
			nextState.printBoard(2 * depth, depth);

			nextState.printAllNextStates(depth + 1);
		}
	}

	// Pass in a reference to an empty array
	// Returns array filled with terminal scores
	public ArrayList<Integer> getTerminalScores(ArrayList<Integer> terminalScores) {
		for (GameState nextState : getNextStates()) {
			if (nextState.isTerminal()) {
				terminalScores.add(nextState.evaluate());
			} else {
				nextState.getTerminalScores(terminalScores);
			}
		}

		return terminalScores;
	}

	/** 
	 * Returns best score from a list of
	 * */
	public int chooseScore(ArrayList<Integer> childScores, int turn) {
		if (childScores.isEmpty()) {
		    throw new IllegalArgumentException("Error: Cannot choose a score from an empty list.");
		}

		// If the list isn't empty
		// We choose the first value to evaluate min or max
		// We consider it to be the best choice
		int score = childScores.get(0);

		// If it's the maximizing player's turn to splay
		// He benefits from the max evaluation
		if (turnToMark.get(turn) == maximizingPlayer) {
			for (int i = 1; i < childScores.size(); i++) {
				if (childScores.get(i) > score) {
					score = childScores.get(i);
				}
			}
		}

		// If it's the minimizing player's turn to play
		// He benefits from the min evaluation
		if (turnToMark.get(turn) == minimizingPlayer) {
			for (int i = 1; i < childScores.size(); i++) {
				if (childScores.get(i) < score) {
					score = childScores.get(i);
				}
			}
		}

		return score;
	}

	// 
	public int scoreState() {
		if (isTerminal()) {
			return evaluate();
		}

		ArrayList<Integer> childScores = new ArrayList<>();

		for (GameState child : getNextStates()) {
			if (child.isTerminal()) {
				int childScore = child.evaluate();
				childScores.add(childScore);
			} else {
				int childScore = child.scoreState();
				childScores.add(childScore);
			}
		}

		return chooseScore(childScores, turn);
	}

	public Move findBestMove() {
		ArrayList<Move> availableMoves = getAvailableMoves();

		if (availableMoves.isEmpty()) {
			return null;
		}

		// INITIAL VALUES
		// SUPPOSED TO BE IDEAL
		Move bestMove = availableMoves.get(0);
		int bestScore = makeMove(bestMove).scoreState(); 

		// FIND BEST MOVE FOR MAXIMIZING PLAYER
		if (turnToMark.get(turn) == maximizingPlayer) {
			for (Move move : availableMoves) {
				GameState state = makeMove(move);

				if (state.scoreState() > bestScore) {
					bestScore = state.scoreState();
					bestMove = move;
				}
			}
		}

		// FIND BEST MOVE FOR MINIMIZING PLAYER
		if (turnToMark.get(turn) == minimizingPlayer) {
			for (Move move : availableMoves) {
				GameState state = makeMove(move);

				if (state.scoreState() < bestScore) {
					bestScore = state.scoreState();
					bestMove = move;
				}
			}
		}

		return bestMove;
	}

	// -------------------------------------------------------------------

	public void printBoard() {
		for (int row = 0; row < board.length; row++) {
			System.out.println("-------");
			System.out.print("|");

			for (int col = 0; col < board[0].length; col++) {
				System.out.print(board[row][col] + "|");
			}

			System.out.println();
		}

		System.out.println("-------");

		if (!isTerminal()) {
			System.out.println("It is now " + turnToMark.get(turn) + "'s turn to play.");
		} else {
			// X BY DEFAULT THE MAXIMIZING PLAYER
			// TODO: MAKE THIS METHOD MORE AGNOSTIC
			if (evaluate() == 1) {
				System.out.println("Eval: " + evaluate());
				System.out.println("WIN.");
			} else if (evaluate() == -1) {
				System.out.println("Eval: " + evaluate());
				System.out.println("LOSE.");
			} else {
				System.out.println("Eval: " + evaluate());
				System.out.println("DRAW.");
			}
		}
	}

	/* Method to pass in tab and depth counts */
	public void printBoard(int i, int depth) {
		// String tabs = String.valueOf("\t").repeat(i);
		String halfTabs = String.valueOf("  ").repeat(i / 2);

		System.out.println();
		System.out.println(halfTabs + halfTabs + "(" + depth + ")");

		for (int row = 0; row < board.length; row++) {
			System.out.println(halfTabs + '|' + halfTabs + "-------");
			System.out.print(halfTabs + '|' + halfTabs + "|");

			for (int col = 0; col < board[0].length; col++) {
				System.out.print(board[row][col] + "|");
			}

			System.out.println();
		}

		System.out.println(halfTabs + '|' + halfTabs + "-------");

		if (!isTerminal()) {
			System.out.println(halfTabs + '|' + halfTabs + "It is now " + turnToMark.get(turn) + "'s turn to play.");
		} else {
			if (evaluate() == 1) {
				System.out.println(halfTabs + '|' + halfTabs + "Eval: " + evaluate());
				System.out.println(halfTabs + '|' + halfTabs + "WIN.");
			} else if (evaluate() == -1) {
				System.out.println(halfTabs + '|' + halfTabs + "Eval: " + evaluate());
				System.out.println(halfTabs + '|' + halfTabs + "LOSE.");
			} else {
				System.out.println(halfTabs + '|' + halfTabs + "Eval: " + evaluate());
				System.out.println(halfTabs + '|' + halfTabs + "DRAW.");
			}
		}
	}
}
