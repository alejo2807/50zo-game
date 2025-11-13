package Model.Cards;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a playing card with a symbol, URL, and associated value.
 * This class implements a card value system where different card symbols
 * have specific numeric values according to game rules. The card value
 * is automatically calculated based on its symbol during construction
 * or when the symbol is changed.
 * <p>
 * Card value rules:
 * <ul>
 *   <li>Cards 2-8 and 10 have their face value</li>
 *   <li>J, Q, K have a value of -10</li>
 *   <li>9 has a value of 0</li>
 *   <li>A (Ace) has a default value of 10</li>
 * </ul>
 * </p>
 *
 * @author Juan-David-Brandon
 * @version 1.0
 * @since 2025
 */
public class Card {
    /**
     * The numeric value assigned to this card based on its symbol.
     */
    private int value;

    /**
     * The symbol representing this card (e.g., "A", "2", "K", "Q").
     */
    private String symbol;

    /**
     * The file path or URL to the card's visual image resource.
     */
    private String url;

    /**
     * List containing symbols for cards with numeric face values (2-8 and 10).
     * These cards retain their face value as their numeric value.
     */
    List<String> valores2_8 = Arrays.asList("2", "3", "4", "5", "6", "7", "8", "10");

    /**
     * List containing symbols for face cards (Jack, Queen, King).
     * These cards have a value of -10.
     */
    List<String> valoresJQK = Arrays.asList("J", "Q", "K");

    /**
     * Constructs a new Card with the specified symbol and URL.
     * The card value is automatically assigned based on the symbol according to the game rules:
     * numeric cards (2-8, 10) use their face value, face cards (J, Q, K) have value -10,
     * 9 has value 0, and Ace (A) has default value 10.
     *
     * @param symbol the symbol of the card (e.g., "A", "2", "K", "Q")
     * @param url the file path or URL pointing to the card's image resource
     */
    public Card(String symbol, String url) {
        this.symbol = symbol;
        this.url = url;
        if(valores2_8.contains(this.symbol)){
            setValue(Integer.parseInt(this.symbol));
        }
        else if(valoresJQK.contains(this.symbol)){
            setValue(-10);
        }
        else if(symbol.equals("9")){
            setValue(0);
        }
        else if(symbol.equals("A")){
            setValue(10); // default value for Ace
        }
    }

    /**
     * Sets the numeric value of this card.
     *
     * @param value the value to assign to this card
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Returns the symbol of this card.
     *
     * @return the card symbol (e.g., "A", "2", "K", "Q")
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the file path or URL associated with this card's image.
     *
     * @return the URL or file path of the card's image resource
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the numeric value of this card.
     *
     * @return the card's value according to the game rules
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the symbol of this card and automatically updates its value based on the new symbol.
     * The value assignment follows the same rules as in the constructor:
     * numeric cards (2-8, 10) use their face value, face cards (J, Q, K) have value -10,
     * 9 has value 0, and Ace (A) has default value 10.
     *
     * @param symbol the new symbol for this card (e.g., "A", "2", "K", "Q")
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
        if(valores2_8.contains(symbol)){
            setValue(Integer.parseInt(symbol));
        }
        else if(valoresJQK.contains(symbol)){
            setValue(-10);
        }
        else if(symbol.equals("9")){
            setValue(0);
        }
        else if(symbol.equals("A")){
            setValue(10); // default value for Ace
        }
    }
}