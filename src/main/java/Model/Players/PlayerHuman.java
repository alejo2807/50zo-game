package Model.Players;

import Controller.GameWindowController;
import Model.Cards.CardPile;
import Model.Cards.Deck;
import View.GameWindow;
import View.Messages;
import javafx.application.Platform;

import java.io.IOException;

public class PlayerHuman extends AdapterPlayers {
    private Thread thread;
    private int indexCard;
    private boolean turnFinished = false;
    private GameWindowController controller;
    private boolean isWin = false;
    private Messages messages;
    public PlayerHuman(Deck deck, int myTurn, Object lock, TurnManager turnManager, CardPile cardPile) {
        super(deck, myTurn, lock, turnManager, cardPile);
        takeHand();
        controller = new GameWindowController();

    }

    public PlayerHuman(Deck deck) {
        super(deck);
    }

    @Override
    public void run() {
        while (isPlaying) {
            synchronized (lock) {



                // Esperar turno
                while (turnManager.getActualTurn() != turn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Verificar si tiene cartas válidas
                if (!hasValidCards()) {
                    Platform.runLater(() -> {
                           // GameWindow.getInstance(3).close();
                            new Thread(() -> {
                                try {
                                    Thread.sleep(300); // espera 0.3 segundos
                                    Platform.runLater(() -> {
                                        try {
                                            new Messages(2).show();
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                                } catch (InterruptedException ignored) {}
                            }).start();
                    });
                    System.out.println("🚫 Jugador Humano queda fuera del juego");
                    isPlaying = false;
                    lock.notifyAll();


                    break;  // Sale del juego
                }

                // Esperar hasta que el jugador humano termine (juegue + tome carta)
                turnFinished = false;
                System.out.println("👤 Es turno del jugador humano. Esperando acción...");
                if(turnManager.getActualTurn() == turn && !isWin){
                    Platform.runLater(() -> {
                        try {
                            GameWindow window = GameWindow.getInstance(3);
                            window.getScene().getRoot().setMouseTransparent(true);
                             messages = new Messages(3);
                             messages.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                while (!turnFinished && turnManager.getActualTurn() == turn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // El turno ya fue pasado por el controlador, solo notificamos
                System.out.println("👤 Jugador humano terminó su turno.");
                if(turnManager.getTotalTurns().size() == 1 && isPlaying){
                    // isWin = true;
                    Platform.runLater(() -> {
                            new Thread(() -> {
                                try {
                                    Thread.sleep(300); // espera 0.3 segundos
                                    Platform.runLater(() -> {
                                        try {
                                            messages.close();
                                            new Messages(1).show();
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                                } catch (InterruptedException ignored) {}
                            }).start();

                    });

                }

            }
        }
    }

    public void finishTurn() {
        synchronized (lock) {
            turnFinished = true;
            lock.notifyAll();
        }
    }

    public void setTurnFinished(boolean finished) {
        this.turnFinished = finished;
    }

    public void setIndexCard(int indexCard) {
        this.indexCard = indexCard;
    }
    private void showWinLoseMessage(int i) {
        // isWin = true;
        Platform.runLater(() -> {
            try {
                GameWindow.getInstance(3).close();
                new Thread(() -> {
                    try {
                        Thread.sleep(300); // espera 0.3 segundos
                        Platform.runLater(() -> {
                            try {
                                new Messages(i).show();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } catch (InterruptedException ignored) {}
                }).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}