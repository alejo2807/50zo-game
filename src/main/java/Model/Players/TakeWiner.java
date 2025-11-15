package Model.Players;

import View.Messages;
import javafx.application.Platform;
import java.io.IOException;
import java.util.List;

public class TakeWiner extends Thread {
    private TurnManager turnManager;
    private Object lock;
    private volatile boolean running = true;
    private PlayerHuman playerHuman;
    private List<PlayerGPU> playerGPUList;

    public TakeWiner(TurnManager turnManager, Object lock, PlayerHuman humanPlayer, List<PlayerGPU> playerGPUList) {
        this.turnManager = turnManager;
        this.lock = lock;
        this.playerHuman = humanPlayer;
        this.playerGPUList = playerGPUList;
        this.setDaemon(true); // Para que termine cuando la aplicación cierre
    }
    @Override
    public void run() {
        System.out.println("🎯 TakeWiner thread started - waiting for winner...");

        while (running) {
            synchronized (lock) {
                try {
                    int remainingPlayers = turnManager.getTotalTurns().size();
                    System.out.println("⏳ TakeWiner waiting... Current players: " + remainingPlayers);

                    // Verificar ANTES de esperar
                    if (remainingPlayers == 1) {
                        System.out.println("🏆 WINNER DETECTED IMMEDIATELY!");
                        showWinnerMessage();
                        running = false;
                        break;
                    }

                    lock.wait();

                    remainingPlayers = turnManager.getTotalTurns().size();
                    System.out.println("🔔 TakeWiner woke up! Players remaining: " + remainingPlayers);

                    // Verificar si solo queda un jugador
                    if (remainingPlayers == 1) {
                        System.out.println("🏆 ¡HAY UN GANADOR!");
                        showWinnerMessage();
                        running = false;
                        break;
                    } else if (remainingPlayers == 0) {
                        System.out.println("⚠ No quedan jugadores (error de lógica)");
                        running = false;
                        break;
                    } else {
                        System.out.println("⏭ Aún quedan " + remainingPlayers + " jugadores. Esperando...");
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

    private void showWinnerMessage() {
        final int winnerTurn = turnManager.getTotalTurns().get(0);
        System.out.println("🏆 Turno ganador: " + winnerTurn);

        Platform.runLater(() -> {
            try {
                Messages messages = new Messages(1, playerHuman, playerGPUList, turnManager);
                System.out.println("📢 Mostrando mensaje...");
                messages.show();
                System.out.println("✅ Mensaje mostrado correctamente");

            } catch (IOException e) {
                System.err.println("❌ Error al mostrar mensaje de victoria:");
                e.printStackTrace();
            }
        });
    }

    public void stopChecking() {
        running = false;
        synchronized (lock) {
            lock.notifyAll();
        }
    }
}