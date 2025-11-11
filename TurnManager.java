package Model.Players;

public class TurnManager {
    private int actualTurn;
    private final int totalPlayers = 2; //se debe inicializar por la persona que va a jugar, no debe iniciar con valor ya que dicta la cantidad de jugadores y turnos que tendra la partida.

    public TurnManager() {
        actualTurn = 0; // Comienza sin turno asignado
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


    /**Este método permite tener el total de jugadores que el usuario desea, este número se pasa a la clase GameWindowController
     * y desde ahí se crean los jugadores de GPU necesarios, sólo los que se piden, para mayor rendimiento y no desperdiciar cartas**/
    public synchronized int getTotalPlayers(){
        return totalPlayers;
    }
}