package Model.Players;

import java.util.ArrayList;

/**
 * The {@code TurnManager} class manages the sequence of turns for all players in the game.
 * It handles advancing turns, tracking eliminated players, and maintaining a consistent
 * order of play.
 *
 * <p>This class ensures synchronization when accessing or modifying the turn state,
 * so it can be safely used in a multithreaded environment where multiple player threads
 * may interact simultaneously.</p>
 *
 * @author Juan-David-Brandon
 * @since 2025
 */
public class TurnManager {
    /** Current active turn identifier (corresponds to the player’s number). */
    private int actualTurn;

    /** Total number of players at the start of the game. */
    private int totalPlayers;

    /** Counter of players who have been eliminated from the game. */
    private int playersEliminate = 0;

    /** List of all players’ turn identifiers still in the game. */
    private ArrayList<Integer> totalTurns;

    /** Iterator that tracks the current position within the list of turns. */
    private int iterator;

    /**
     * Constructs a {@code TurnManager} for a given number of players.
     * Initializes the list of active turns and sets the turn iterator.
     *
     * @param totalPlayers total number of players participating in the game
     */
    public TurnManager(int totalPlayers) {
        actualTurn = 0; // Starts with no active turn assigned
        this.totalPlayers = totalPlayers;
        totalTurns = new ArrayList<>();
        for (int i = 1; i <= totalPlayers; i++) {
            totalTurns.add(i);
        }
        iterator = 0;
    }

    /**
     * Returns the current active turn identifier.
     *
     * @return the current player’s turn number
     */
    public synchronized int getActualTurn() {
        return actualTurn;
    }

    /**
     * Advances the turn to the next player in the list.
     * <p>If the iterator reaches the end of the list, it loops back to the beginning.
     * Eliminated players are automatically skipped, since they are removed from
     * {@code totalTurns}.</p>
     */
    public synchronized void passTurn() {
        System.out.println(totalTurns);
        iterator++;
        if (iterator == totalTurns.size() || iterator > totalTurns.size()) {
            iterator = 0;
        }

        System.out.println(iterator);
        actualTurn = totalTurns.get(iterator);

        System.out.println("🔄 TurnManager.passTurn(): New turn = " + actualTurn + " | Iterator: " + iterator);
    }

    /**
     * Starts the game by assigning the first turn to player 1.
     */
    public synchronized void startGame() {
        actualTurn = 1;
        System.out.println("🎮 TurnManager.startGame(): Game started, current turn = " + actualTurn);
    }

    /**
     * Removes a player’s turn from the active list when they are eliminated.
     * <p>This method also ensures that the iterator position remains valid after
     * removal, preventing skipped or repeated turns.</p>
     *
     * @param lasTurnEliminate the turn number corresponding to the eliminated player
     */
    public synchronized void setLasTurnEliminate(int lasTurnEliminate) {
        for (int i = 0; i < this.totalTurns.size(); i++) {
            if (this.totalTurns.get(i) == lasTurnEliminate) {

                // Adjust iterator if the removed turn is before or at the current index
                if (i < iterator) {
                    iterator--;
                } else if (i == iterator) {
                    iterator--;
                }

                totalTurns.remove(i);
                System.out.println("🚫 Player " + lasTurnEliminate + " eliminated. Remaining turns: " + totalTurns);
                break; // Important to exit after removing
            }
        }
    }

    /**
     * Returns the list of all active player turns.
     *
     * @return an {@code ArrayList<Integer>} containing the remaining player turns
     */
    public ArrayList<Integer> getTotalTurns() {
        return totalTurns;
    }
}
