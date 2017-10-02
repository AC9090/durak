package cards;

import java.util.ArrayList;
import java.util.Iterator;



public class InPlay extends CardGroup {
	
	//make these into iterator
	private ArrayList<Card[]> battles = new ArrayList<Card[]>();
	Suit trump;
	public InPlay(Suit trump) {
		super();
		this.trump = trump;
	}
	
	public ArrayList<Card> fold(){
		ArrayList<Card> c = new ArrayList<Card>();
		Iterator<Card[]> bat = battles.iterator();
		while (bat.hasNext()){
			c.add(bat.next()[0]);
			c.add(bat.next()[1]);
		}
		battles.clear();
		return c;
	}
	
	public int attack(Card c){
		boolean valid = false;
		
		Iterator<Card[]> cards = battles.iterator();
		while(cards.hasNext()){
			Card c_[] =cards.next();
			if (c_[0].getValue() == c.getValue() 
					|| c_[1].getValue() == c.getValue()){
				valid = true;
				break;
			}
		}
		if (battles.size() == 0)
			valid = true;
		if (valid){
			battles.add(new Card[2]);
			battles.get(battles.size() - 1)[0] = c;
			return 0;
		} else {
			return 1;
		}
	}
	
	public int defend(Card c, int index){
		
		if ((battles.get(index)[0].getSuit().equals(c.getSuit()) && battles.get(index)[0].getValue() < c.getValue())
				||( ! battles.get(index)[0].getSuit().equals(trump)) && c.getSuit().equals(trump)){
			battles.get(index)[1] = c;
			return 0;
		} else {
			return 1;
		}
	}
	
	public String toString(){
		String str = ""; 
		for(Card[] bat : battles){
			str += bat[0].toString() + " : " + bat[1].toString() + '\n';
		}
		return str;
	}

	public int numBattles() {
		return battles.size();
	}
	
	public boolean isDefended(){
		boolean def = false;
		for(Card[] bat : battles){
			if (bat[1] == null)
				return def;
		}
		return true;
	}
	public boolean isDefended(int i){
		if (battles.get(i)[1] == null)
			return false;
		else return true;

	}

}
