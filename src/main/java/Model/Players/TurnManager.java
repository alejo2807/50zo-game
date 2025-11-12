package Model.Players;

import java.util.ArrayList;

public class TurnManager {
    private int actualTurn;
    private int totalPlayers;
    private int playersEliminate=0;
    private ArrayList<Integer> totalTurns;
    private int iterator;

    public TurnManager(int totalPlayers) {
        actualTurn = 0; // Comienza sin turno asignado
        this.totalPlayers = totalPlayers;
        totalTurns= new ArrayList<>();
        for(int i=1;i<=totalPlayers;i++){
            totalTurns.add(i);
        }
        iterator=0;
    }

    public synchronized int getActualTurn() {
        return actualTurn;
    }

    public synchronized void passTurn() {
        System.out.println(totalTurns);
        iterator++;
        if(iterator == totalTurns.size() || iterator > totalTurns.size()){
            iterator=0;
        }

        System.out.println(iterator);
            actualTurn = totalTurns.get(iterator);
        /*
        if (actualTurn == totalPlayers || actualTurn == 0) {
            actualTurn = 1;
        } else if(actualTurn == 1){

            while(eliminatedTurns.contains(actualTurn)){
                actualTurn++;
            }
        }*/
        System.out.println("🔄 TurnManager.passTurn(): Nuevo turno = " + actualTurn+ "iterador " + iterator);
    }

    public synchronized void startGame() {
        actualTurn = 1;
        System.out.println("🎮 TurnManager.startGame(): Juego iniciado, turno = " + actualTurn);
    }

    public synchronized void setLasTurnEliminate(int lasTurnEliminate) {
        for(int i = 0; i < this.totalTurns.size(); i++){
            if(this.totalTurns.get(i) == lasTurnEliminate){

                // Si eliminamos un turno ANTES del iterator actual, ajustamos
                if(i < iterator){
                    iterator--;
                }

                // Si eliminamos el turno actual, retrocedemos el iterator
                else if(i == iterator){
                    iterator--;
                }

                totalTurns.remove(i);
                System.out.println("🚫 Jugador " + lasTurnEliminate + " eliminado. Turnos restantes: " + totalTurns);
                break; // Importante: salir después de eliminar


                //totalTurns.remove(i);
            }
        }
    }

    public ArrayList<Integer> getTotalTurns() {
        return totalTurns;
    }
}