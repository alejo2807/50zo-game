package Model.Players;

import Model.Cards.CardPile;
import Model.Cards.Deck;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerGPU extends AdapterPlayers {
        public PlayerGPU(Deck deck, int myTurn, Object lock, TurnManager turnManager, CardPile cardPile){
            super(deck, myTurn, lock, turnManager, cardPile);
        }
    @Override
    public void run() {
        while(true){
            synchronized (lock){
                while(turnManager.getActualTurn() != turn){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try{
                    Thread.sleep( ThreadLocalRandom.current().nextInt(2000, 4001));
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                putCard(choseCard(), cardPile);
                try{
                    Thread.sleep(2000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                takeCard(deck.getCard());

                turnManager.passTurn();
                lock.notifyAll();
            }
        }
    }
    //ojo, toca hacer un metodo que verifique que tenga cartas validas, y que si  no tiene, deje de jugar, que se valide aca si el jugador ha perdido primero
    public int choseCard(){
            Random rand = new Random();
            int index;
            while(true){
                index = rand.nextInt(3);
                if(hand.get(index).getValue() + cardPile.getValuePile() <= 50){
                    break;
                }
            }
            return index;

    }
}
