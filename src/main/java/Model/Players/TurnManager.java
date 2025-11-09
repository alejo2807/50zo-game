package Model.Players;

public class TurnManager {
    private int actualTrurn;
    public TurnManager(){
        actualTrurn = 1;
    }
    public int getActualTurn(){
        return actualTrurn;
    }
    public void passTurn(){
        if(actualTrurn == 4){
            actualTrurn = 1;
        }
        else{
            actualTrurn++;
        }
    }

}
