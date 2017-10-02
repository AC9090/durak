package cards;

import java.util.ArrayList;


public class Hand extends CardGroup {

	public Hand(ArrayList<Card> cards) {
		super(cards);
		// TODO Auto-generated constructor stub
	}
	
	public Hand() {
		super();
	}
	
	public String toString(){
		String string = "";
		for(int i= 0; i < cards.size(); i++){
			string += i + ": " + cards.get(i).toString() + ". "; 
		}
		return string;
	}
	
	public boolean empty(){
		
		return cards.isEmpty();
	}

}
