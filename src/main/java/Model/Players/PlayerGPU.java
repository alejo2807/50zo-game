package Model.Players;

import Model.Cards.CardPile;
import Model.Cards.Deck;
import Model.Cards.Card;
import Model.Exceptions.InvalidCardException;
import Controller.GameWindowController;
import javafx.application.Platform;

import java.util.concurrent.ThreadLocalRandom;

public class PlayerGPU extends AdapterPlayers {

    private final GameWindowController controller;

    public PlayerGPU(Deck deck, int myTurn, Object lock, TurnManager turnManager, CardPile cardPile, GameWindowController controller) {
        super(deck, myTurn, lock, turnManager, cardPile);
        this.controller = controller;
    }

    @Override
    public void run() {
        System.out.println("🤖 GPU " + turn + " hilo iniciado");

        while (isPlaying) {

            synchronized (lock) {
                System.out.println(turnManager.getTotalTurns());
                System.out.println("TUUUUURRRRNOOOO ACTUAAAAAAAAAL  "+turnManager.getActualTurn());
                // Esperar hasta que sea mi turno
                while (isPlaying && turnManager.getActualTurn() != turn) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.err.println("❌ GPU " + turn + " interrumpida esperando turno");
                        return;
                    }
                }
            /*
                // Verificar de nuevo si sigue jugando después de despertar
                if (!isPlaying) {
                    System.out.println("💤 GPU " + turn + " sale del juego");
                    returnCardsToDecK();

                    // Actualizar interfaz para que desaparezcan las cartas
                    Platform.runLater(() -> controller.printCardsGPU());

                    turnManager.passTurn();
                    turnManager.eliminatePlayer();
                    turnManager.setLasTurnEliminate(turn);
                    this.isPlaying = false;
                    lock.notifyAll();
                    break;  // Sale del bucle y termina el hilo
                }
*/
                // Verificar si tiene cartas válidas
                if (!hasValidCards()) {
                    System.out.println("🚫 GPU " + turn + " queda fuera del juego");


                    // Devolver todas las cartas al mazo antes de salir
                    returnCardsToDecK();

                    // Actualizar interfaz para que desaparezcan las cartas
                    Platform.runLater(() -> controller.printCardsGPU());
                   // turnManager.eliminatePlayer();
                    turnManager.setLasTurnEliminate(turn);
                    this.isPlaying = false;
                    turnManager.passTurn();
                    lock.notifyAll();

                    break;  // Sale del bucle y termina el hilo
                }

                System.out.println("🤖 GPU " + turn + " comienza su turno");

                try {
                    // Simula "pensar"
                    Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2500));
                } catch (InterruptedException e) {
                    System.err.println("❌ GPU " + turn + " interrumpida pensando");
                    return;
                }

                // Jugar carta válida
                if (!hand.isEmpty()) {
                    boolean cardPlayed = false;

                    // Intentar jugar cartas hasta encontrar una válida
                    for (int i = 0; i < hand.size() && !cardPlayed; i++) {
                        try {
                            Card cardToPlay = hand.get(i);
                            putCard(i, cardPile);

                            final Card finalCard = cardToPlay;
                            Platform.runLater(() -> controller.updatePileImage(finalCard));
                            System.out.println("🤖 GPU " + turn + " jugó: " + cardToPlay.getSymbol() +
                                    " | Nuevo valor pila: " + cardPile.getValuePile());
                            cardPlayed = true;

                        } catch (InvalidCardException e) {
                            // Esta carta no es válida, probar la siguiente
                            System.out.println("🤖 GPU " + turn + " intentó jugar carta inválida, probando siguiente...");
                        }
                    }

                    if (!cardPlayed) {
                        System.out.println("⚠️ GPU " + turn + " no pudo jugar ninguna carta (esto no debería pasar)");
                    }
                } else {
                    System.out.println("⚠️ GPU " + turn + " no tiene cartas para jugar");
                }

                try {
                    // Pequeña pausa antes de tomar nueva carta
                    Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1000));
                } catch (InterruptedException e) {
                    System.err.println("❌ GPU " + turn + " interrumpida antes de tomar carta");
                    return;
                }

                // Tomar nueva carta
                Card newCard = deck.getCard();
                if (newCard != null) {
                    takeCard(newCard);
                    System.out.println("🤖 GPU " + turn + " tomó: " + newCard.getSymbol());
                } else {
                    System.out.println("⚠️ GPU " + turn + ": mazo vacío, no puede tomar carta");
                }

                // Actualizar interfaz
                Platform.runLater(() -> controller.printCardsGPU());

                // Pasar turno
                System.out.println("🤖 GPU " + turn + " termina su turno");
                turnManager.passTurn();
                lock.notifyAll();
            }
        }

        System.out.println("💤 GPU " + turn + " hilo terminado");

    }
    public void returnCardsToDecK() {
        if (hand.isEmpty()) {
            System.out.println("⚠️ Jugador " + turn + " no tiene cartas para devolver");
            return;
        }

        int cartasDevueltas = hand.size();
        System.out.println("🔄 Jugador " + turn + " devuelve " + cartasDevueltas + " cartas al mazo");

        // Agregar todas las cartas al mazo
        for (Card card : hand) {
            deck.getDeck().add(card);
        }

        // Limpiar la mano del jugador
        hand.clear();

        // Barajar el mazo
        deck.shuffle();

        System.out.println("🔀 Mazo barajado. Cartas disponibles: " + deck.getDeck().size());
    }
    public void setTurn(int turn) {
        this.turn = turn;
    }
}