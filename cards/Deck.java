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
		cards.add(new Card(1, Suit.CLUBS));
		cards.add(new Card(12, Suit.CLUBS));
		cards.add(new Card(2, Suit.SPADES));
		cards.add(new Card(4, Suit.SPADES));
		cards.add(new Card(3, Suit.HEARTS));
		cards.add(new Card(5, Suit.SPADES));
		cards.add(new Card(3, Suit.CLUBS));
		//shuffle();
		return cards;
	}
	
	public void shuffle(){
		Collections.shuffle(cards);
	}
}
