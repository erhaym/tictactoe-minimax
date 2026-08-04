import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		/* char[][] board = new char[][] {
			{'X', 'O', 'X'},
			{'O', ' ', ' '},
			{' ', ' ', 'X'}
		}; */

		/* char[][] board = {
		    {'X', 'O', 'X'},
		    {'O', 'X', ' '},
		    {' ', ' ', 'O'}
		}; */

		char[][] board = {
			{'O', ' ', ' '},
			{'O', 'X', ' '},
			{'X', 'O', 'X'}
		};

		/* 0 : 'X'
		   1 : 'O' */
		int turn = 1; // X's turn

		// X -> MINIMIZING | O -> MAXIMIZING
		GameState state = new GameState(board, turn, 'O', 'X');
		board[0][0] = 'O';

		/* ArrayList<Move> availableMoves = state.getAvailableMoves(); */

		/* // Let's get the first available move for example
		Move move = new Move(availableMoves.get(0).row, availableMoves.get(0).col);
		GameState updatedState = state.makeMove(move); */

		System.out.println("ORIGINAL:");
		state.printBoard();
		// System.out.println("Original turn: " + state.turn);

		// state.printNextStates();

		// ArrayList<GameState> nextStates = state.getNextStates();

		state.printAllNextStates(1);

		ArrayList<Integer> terminalScores = new ArrayList<>();

		System.out.println(state.getTerminalScores(terminalScores));

		System.out.println("X should play: " + state.findBestMove());		

		/* System.out.println("\n" + availableMoves + "\n");

		System.out.println("UPDATED:");
		updatedState.printBoard();
		System.out.println("Updated turn: " + updatedState.turn);

		char winner = updatedState.getWinner();

		System.out.println("\nWinner: " + winner);

		System.out.println("\nEval: " + updatedState.evaluate()); */

		// System.out.println("\nGame ended: " + updatedState.isTerminal());
	}
}
