# Tic-tac-toe Minimax

## The algorithm

Minimax is a decision-making algorithm for adversarial games. It is a recursive algorithm that looks at the current and future states of the game by anticipating future moves for the player and their opponent. Its goal is to find the optimal move for a player, maximizing their advantage while minimizing the opponent's gains towards victory.

### Maximizing player and Minimizing player

Each player is assigned a role in the search for the best move: a maximizing player, who benefits from higher score, and a minimizing player who benefits from lower scores. The maximizing player takes the highest score among their possible next moves, the minimizing player takes the lowest. Minimax assumes that both players play optimally for their role, that makes the result a guaranteed-safe move rather than a good guess.

### Eval function

The evaluation function quantifies the outcome (winning, drawing, or losing) of a given terminal position.

For example, as in this program, and because tic-tac-toe isn't a complex game, a winning base case is assigned the value 1, a neutral position the value 0 and a losing position the value -1.

The set of next possible moves and their respective next possible moves can be viewed as a tree. We then recover the base case and pick the appropriate value for each sub-tree's branches. Because we are expecting that the opponent plays optimally, we recover the value each player would choose according to their role, this way the maximizing player secures the best outcome they can force, never worse than a draw. 

## Running

```bash
cd main/src
```

```bash
javac *.java
```

```bash
java Main
```

## TO-DO

- Separate logic for algorithm and for game-state handling
- Alpha-beta pruning
- Swing GUI
- Tests
- Javadoc
