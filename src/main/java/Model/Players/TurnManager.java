package Model.Players;

public class TurnManager {
    private int actualTurn;
    private final int totalPlayers;

    public TurnManager(int totalPlayers) {
        actualTurn = 0; // Comienza sin turno asignado
        this.totalPlayers = totalPlayers;
    }

    public synchronized int getActualTurn() {
        return actualTurn;
    }

    public synchronized void passTurn() {
        if (actualTurn == totalPlayers || actualTurn == 0) {
            actualTurn = 1;
        } else {
            actualTurn++;
        }
        System.out.println("🔄 TurnManager.passTurn(): Nuevo turno = " + actualTurn);
    }

    public synchronized void startGame() {
        actualTurn = 1;
        System.out.println("🎮 TurnManager.startGame(): Juego iniciado, turno = " + actualTurn);
    }
}