package cards;
import java.util.ArrayList;
import java.util.Collections;



public class Deck extends CardGroup {
	
	public Deck(ArrayList<Card> cards) {
		super(cards);

	}
	
	public Deck(){
		super();
	}
	
	public Suit pickTrump(){
		Card c = cards.remove(0);
		Suit s = c.getSuit();
		cards.add(c);
		return s;
	}
	
	public Card pickUp(){
		if (cards.size() >= 0) {			
			return cards.remove(0);
		} else {
			return null;
		}
	}
	
	public ArrayList<Card> buildDeck(){
		for(int i = 1; i <= 12; i++){
			for(Suit s : Suit.values()){
				cards.add(new Card(i, s));
			}
		}
		shuffle();
		return cards;
	}
	
	public void shuffle(){
		Collections.shuffle(cards);
	}
}
