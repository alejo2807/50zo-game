package Model.Players;

import Model.Cards.Card;
import Model.Cards.CardPile;
import Model.Cards.Deck;
import Model.Exceptions.InvalidCardException;

import java.util.ArrayList;
import java.util.List;

public abstract class AdapterPlayers extends Thread implements IPlayers {
    protected List<Card> hand = new ArrayList<>();
    protected boolean isPlaying;
    protected int turn;
    protected Object lock;
    protected TurnManager turnManager;
    protected CardPile cardPile;
    protected Deck deck;

    public AdapterPlayers(Deck deck, int myTurn, Object lock, TurnManager turnManager, CardPile cardPile){
        this.deck = deck;
        this.isPlaying = false;  // 👈 CAMBIO 1: Inicializar en true
        this.turn = myTurn;
        this.lock = lock;
        this.turnManager = turnManager;
        this.cardPile = cardPile;
    }

    public AdapterPlayers(Deck deck){
        this.deck = deck;
        this.isPlaying = true;  // 👈 CAMBIO 2: También aquí
    }

    @Override
    public void takeCard(Card card) {
        hand.add(card);
    }

    @Override
    public void putCard(int indexCard, CardPile cardPile) throws InvalidCardException {
        if (indexCard < 0 || indexCard >= hand.size()) {
            throw new IllegalArgumentException("Índice de carta inválido: " + indexCard);
        }

        Card card = hand.get(indexCard);
        int newValue = card.getValue() + cardPile.getValuePile();

        // Validar que la carta no supere el límite de 50
        if (newValue > 50) {
            throw new InvalidCardException(card, cardPile.getValuePile());
        }

        // Si pasa la validación, jugar la carta
        hand.remove(indexCard);
        cardPile.addCard(card);
    }

    @Override
    public void takeHand(){
        hand.clear();
        for (int i = 0; i < 4; i++) {
            Card card = deck.getCard();
            if (card != null) {
                hand.add(card);
            }
        }
    }

    @Override
    public List<Card> getHand(){
        return hand;
    }

    public boolean getIsPlaying(){
        return isPlaying;
    }

    void setIsPlaying(boolean isPlaying){
        this.isPlaying = isPlaying;
    }

    public boolean hasValidCards(){
        int cont = 0;
        for (Card card : hand) {
            if(card.getValue() + cardPile.getValuePile() <= 50){
                cont++;
            }
        }
        boolean hasValid = cont > 0;
        if (!hasValid) {
            isPlaying = false;  // Sale del juego si no tiene cartas válidas
            System.out.println("❌ Jugador " + turn + " eliminado: sin cartas válidas (pila=" + cardPile.getValuePile() + ")");
        }
        return hasValid;
    }

    public boolean getIsplaying(){
        return isPlaying;
    }

    public int getTurn(){
        return turn;
    }

    public void initializePlayer(){
        isPlaying = true;
        takeHand();  // 👈 CAMBIO 3: Tomar cartas iniciales
        System.out.println("✅ Jugador " + turn + " inicializado con " + hand.size() + " cartas");
    }
}