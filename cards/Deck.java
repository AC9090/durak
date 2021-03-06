package cards;
import java.util.ArrayList;
import java.util.Collections;



public class Deck extends CardGroup {
	
	private Card trump;
	
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
		trump = c;
		return s;
	}
	
	public ArrayList<Card> pickUp(int i){
		ArrayList<Card> c = new ArrayList<Card>();
		while (!cards.isEmpty() && c.size() < i){
			c.add(cards.remove(0));
		} 
		return c;
	}
	
	public ArrayList<Card> buildDeck(){ //TODO:choose whether deck starts at 6. also fix value of ace.
		for(int i = 5; i <= 13; i++){
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

	public Card pickUp() {
		if (!cards.isEmpty())
			return cards.remove(0);
		return null;
	}

	public Card getTrump() {
		return trump;
	}
}
