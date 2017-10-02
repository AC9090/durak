package cards;

import java.util.ArrayList;


public abstract class CardGroup {

	protected ArrayList<Card> cards = new ArrayList<Card>();
	

	public CardGroup(ArrayList<Card> cards){
		this.cards = cards;
	}
	
	public CardGroup(){
		
	}
	
	public void add(Card c){
		cards.add(c);
	}

	public void add(ArrayList<Card> c){
		cards.addAll(c);
	}
	
	public Card remove(Card c){
		if(cards.remove(c)){			
			return c;
		}
		else{
			System.err.println("Could not find card " + c.toString() +  " in hand." );
			System.exit(1);
		}
		return null;
	}
	
	public Card remove(int i){
		if(cards.size() > i){
			return cards.remove(i);
		}	else{
			System.err.println("Could not find card with index " + i +  " in hand." );
			return null;
		}
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	
}
