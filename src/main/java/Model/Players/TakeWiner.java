package Model.Players;

import View.ShowWiner;
import javafx.application.Platform;
import java.io.IOException;
import java.util.List;

/**
 * The {@code TakeWiner} class is a background thread responsible for detecting
 * when only one player remains in the game, indicating the winner.
 *
 * <p>This thread continuously monitors the {@link TurnManager} and waits for
 * updates using a shared synchronization lock. When the number of active turns
 * is reduced to one, it triggers the JavaFX message window displaying the winner.</p>
 *
 * <h3>Thread Behavior:</h3>
 * <ul>
 *   <li>Runs as a daemon thread so it terminates when the application closes.</li>
 *   <li>Uses {@code lock.wait()} and {@code lock.notifyAll()} for coordination.</li>
 *   <li>Stops automatically once a winner is detected or if interrupted.</li>
 * </ul>
 *
 * <h3>Winner Message:</h3>
 * <p>When a winner is detected, the thread uses {@link Platform#runLater(Runnable)}
 * to safely open the JavaFX {@link ShowWiner} window.</p>
 *
 * <h3>Usage:</h3>
 * <pre>{@code
 * TakeWiner takeWiner = new TakeWiner(turnManager, lock, humanPlayer, gpuPlayers);
 * takeWiner.start();
 * }</pre>
 *
 * @author Juan
 * @author David
 * @author Brandon
 * @since 2025
 */
public class TakeWiner extends Thread {

    /** Manages the turn order and active players. */
    private TurnManager turnManager;

    /** Synchronization lock used to wait for player updates. */
    private Object lock;

    /** Controls the main loop of the monitoring thread. */
    private volatile boolean running = true;

    /** Reference to the human player. */
    private PlayerHuman playerHuman;

    /** List of GPU-controlled players. */
    private List<PlayerGPU> playerGPUList;

    /**
     * Constructs a {@code TakeWiner} monitoring thread.
     *
     * <p>This constructor sets up the winner-checking mechanism and marks the thread
     * as a daemon so that it automatically stops when the JavaFX application exits.</p>
     *
     * @param turnManager the turn manager containing the active turns
     * @param lock the shared lock used for thread synchronization
     * @param humanPlayer the human player instance
     * @param playerGPUList the list of GPU players
     */
    public TakeWiner(TurnManager turnManager, Object lock, PlayerHuman humanPlayer, List<PlayerGPU> playerGPUList) {
        this.turnManager = turnManager;
        this.lock = lock;
        this.playerHuman = humanPlayer;
        this.playerGPUList = playerGPUList;
        this.setDaemon(true); // Ends automatically when the app closes
    }

    /**
     * Main thread loop that waits for changes in the number of active players.
     *
     * <p>The thread checks if only one player remains. If so, it triggers the
     * JavaFX winner message window and terminates.</p>
     */
    @Override
    public void run() {
        System.out.println("🎯 TakeWiner thread started - waiting for winner...");

        while (running) {
            synchronized (lock) {
                try {
                    int remainingPlayers = turnManager.getTotalTurns().size();
                    System.out.println("⏳ TakeWiner waiting... Current players: " + remainingPlayers);

                    // Check before waiting
                    if (remainingPlayers == 1) {
                        System.out.println("🏆 WINNER DETECTED IMMEDIATELY!");
                        showWinnerMessage();
                        running = false;
                        break;
                    }

                    lock.wait();

                    remainingPlayers = turnManager.getTotalTurns().size();
                    System.out.println("🔔 TakeWiner woke up! Players remaining: " + remainingPlayers);

                    // Check after wake-up
                    if (remainingPlayers == 1) {
                        System.out.println("🏆 WINNER FOUND!");
                        showWinnerMessage();
                        running = false;
                        break;
                    } else if (remainingPlayers == 0) {
                        System.out.println("⚠ No players remaining (logic error)");
                        running = false;
                        break;
                    } else {
                        System.out.println("⏭ Still " + remainingPlayers + " players. Waiting...");
                    }

                } catch (InterruptedException e) {
                    System.out.println("⚠ TakeWiner interrupted");
                    running = false;
                    break;
                }
            }
        }
        System.out.println("💤 TakeWiner thread finished");
    }

    /**
     * Opens the message window showing the winner.
     *
     * <p>This method retrieves the remaining player's turn index and safely
     * executes the JavaFX UI update on the application thread.</p>
     */
    private void showWinnerMessage() {
        final int winnerTurn = turnManager.getTotalTurns().get(0);
        System.out.println("🏆 Winning turn: " + winnerTurn);

        Platform.runLater(() -> {
            try {
                ShowWiner showWiner = new ShowWiner(1, playerHuman, playerGPUList, turnManager);
                System.out.println("📢 Displaying win message...");
                showWiner.show();
                System.out.println("✅ Message displayed successfully");

            } catch (IOException e) {
                System.err.println("❌ Error displaying win message:");
                e.printStackTrace();
            }
        });
    }

    /**
     * Stops the monitoring loop and wakes up any waiting thread.
     *
     * <p>This method should be called when the game ends prematurely or
     * when the thread must be manually terminated.</p>
     */
    public void stopChecking() {
        running = false;
        synchronized (lock) {
            lock.notifyAll();
        }
    }
}
