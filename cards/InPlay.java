package cards;

import game.InvalidMove;

import java.util.ArrayList;
import java.util.Iterator;



public class InPlay extends CardGroup {
	
	private ArrayList<Card[]> battles = new ArrayList<Card[]>();
	Suit trump;
	public InPlay(Suit trump) {
		super();
		this.trump = trump;
	}
	
	public ArrayList<Card> fold(){ //all cards are returned from play
		ArrayList<Card> c = new ArrayList<Card>();
		Iterator<Card[]> bat = battles.iterator();
		while (bat.hasNext()){
			c.add(bat.next()[0]);
			c.add(bat.next()[1]);
		}
		battles.clear();
		return c;
	}
	public void attack(Card c) throws InvalidMove{ //places a card in the attack column checking it is allowed
		boolean valid = false;
		
		Iterator<Card[]> cards = battles.iterator();
		while(cards.hasNext()){
			Card c_[] =cards.next();
			if (c_[0].getValue() == c.getValue())
				valid = true;
			if (!(c_[1] == null)) {
				if (c_[1].getValue() == c.getValue()){
					valid = true;
				}	
			}
			if (valid == true)
				break;
		}
		if (battles.size() == 0)
			valid = true;
		if (valid){
			battles.add(new Card[2]);
			battles.get(battles.size() - 1)[0] = c;
			return;
		} else {
			throw new InvalidMove();
		}
	}
	
	public void defend(Card c, int index) throws InvalidMove{
		System.out.println(battles.size());
		if (battles.size() < index + 1)
			throw new InvalidMove();
		if (battles.get(index)[0] == null)
			throw new InvalidMove();
		if (isDefended(index)) {
			throw new InvalidMove();
		}
		
		if ((battles.get(index)[0].getSuit().equals(c.getSuit()) && battles.get(index)[0].getValue() < c.getValue())
				||( ! battles.get(index)[0].getSuit().equals(trump)) && c.getSuit().equals(trump)){
			battles.get(index)[1] = c;
			return;
		} else {
			throw new InvalidMove();
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
	
	public ArrayList<Card[]> getCardPairs(){
		return battles;
	}

}
