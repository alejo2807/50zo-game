package Model.Cards;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
public class Card {
	private int value;
    private String symbol;
    private String url;
    List<String> valores2_8 =  Arrays.asList("2","3","4","5","6","7","8");
    List<String> valoresJQK = Arrays.asList("J", "Q","K");

    public Card(String symbol, String url) {
        this.symbol = symbol;
        this.url = url;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public String getSymbol() {
        return symbol;
    }
    public String getUrl() {
        return url;
    }
    public int getValue() {
        return value;
    }
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
            setValue(10);//for default
        }
     }

}
