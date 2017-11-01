package game;

import java.util.ArrayList;
import java.util.Iterator;

import cards.Card;
import cards.Deck;
import cards.Discard;
import cards.Hand;
import cards.InPlay;
import cards.Suit;
public class Game {

	//no multithread support
	private Deck deck;
	private ArrayList<Hand> playersHuman = new ArrayList<Hand>();
	private ArrayList<Hand> notDurak = new ArrayList<Hand>(); 	//for chuvaks that have no cards left
	private ArrayList<Hand> folded = new ArrayList<Hand>();
	private InPlay inPlay;
	private Discard discard;
	private Suit trump;
	

	private enum PlayState{
		INIT_ATT, INIT_DEF, MAIN
	}
	private PlayState playState;
	
	private Hand defender;
	private Hand attacker;
	
	private boolean gameOver;
	
	public Game(int numPlayersHuman){
		//constructs relevant objects
		gameOver = false;
		
		deck = new Deck();
		deck.buildDeck();
		trump = deck.pickTrump();
		inPlay = new InPlay(trump);
		discard = new Discard();
		
		playState = PlayState.INIT_ATT;
		for(int i = 0; i <  numPlayersHuman; i++){
			playersHuman.add(new Hand());
		}
		
		defender = playersHuman.get(0); //TODO: find player with lowest hand to start
		int attNum = (playersHuman.indexOf(defender) - 1) % (playersHuman.size());
		if (attNum < 0)
		{
		    attNum += playersHuman.size();
		}
		attacker = playersHuman.get(attNum);

	}
	
	public void play(Card c, int selection) throws InvalidMove, InvalidPlayer{
		
		Hand h = findHand(c); 
		if (c == null)
			throw new InvalidPlayer();
		
		switch (playState){
		case INIT_ATT:
			if (!h.equals(attacker)){
				throw new InvalidPlayer();
			}
			inPlay.attack(c);
			h.remove(c);
			playState = PlayState.MAIN;
			break;
		case MAIN:
			if(!h.equals(defender)){
				inPlay.attack(c);
				h.remove(c);
			} else {
				inPlay.defend(c, selection);
				h.remove(c);
			}
			break;
		}
			
		updateState();
		
	}
	public void play(Hand h, OtherAction b) throws InvalidMove, InvalidPlayer{
		// TODO make arguments make sense
		switch (b){
		case FOLD:
			switch (playState){
			case INIT_ATT:
				throw new InvalidMove();
			
			case MAIN:
				if(!h.equals(defender)){
					if (!folded.contains(h))
						folded.add(h);
				} else {
					h.add(inPlay.fold());
					pickUp(playersHuman.get(playersHuman.indexOf(defender) + 1));
					nextDefender();
					if(playersHuman.size() - notDurak.size() > 2) // TODO: Check this rule is correct.
						nextDefender();
					playState = PlayState.INIT_ATT;
					return;
				}
			}
		}
		updateState();
	}

	private void updateState(){
		
		notDurak.addAll(notDurak());
		
		if (notDurak.size() == playersHuman.size() - 1){
			gameOver = true;
			return;
		}

		if((inPlay.isDefended() && inPlay.numBattles() == 6)
				|| notDurak.contains(defender)){ // round is finished
			
			discard.add(inPlay.fold());
			pickUp(defender);
			nextDefender();
						
			playState = PlayState.INIT_ATT;
		}
		if (folded.size() == playersHuman.size() - 1){
			discard.add(inPlay.fold());
			pickUp(defender);
			nextDefender();
			folded.clear();
			
			playState = PlayState.MAIN;
		}
		
	}


	private void pickUp(Hand start) {
		Iterator<Hand> playersIt = playersHuman.listIterator(playersHuman.indexOf(start));
		while(playersIt.hasNext() && !deck.getCards().isEmpty()){
			Hand h = playersIt.next();
			if (h.getCards().size() < 6)
			h.add(deck.pickUp(6 - h.getCards().size()));
			
		}
	}

	private void nextDefender(){
		Iterator<Hand> playersIt = playersHuman.listIterator(playersHuman.indexOf(defender) + 1);
		Hand h = playersIt.next();
		while (playersIt.hasNext()) {
			if(!notDurak.contains(h)){
				defender = h;
		}else
				h = playersIt.next();
		}
		
		int attNum = (playersHuman.indexOf(defender) - 1) % (playersHuman.size());
		if (attNum < 0)
		{
		    attNum += playersHuman.size();
		}
		attacker = playersHuman.get(attNum);
	}
	
	public void deal(){
		for (int i = 0; i < 6; i++){ 
			for (Hand player : playersHuman){
				player.add(deck.pickUp());
			}
		}
	}
	
	public Hand findHand(Card c){
		for (Hand h : playersHuman){
			if (h.getCards().contains(c))
				return h;
		}
		return null;
	}
	
	private ArrayList<Hand> notDurak(){
		ArrayList<Hand> nd = new ArrayList<Hand>();
		for(Hand player: playersHuman){
			if(player.getCards().size() == 0){
				nd.add(player);
				System.out.println("Player " + playersHuman.indexOf(player) + " is out!");
			}
		}
		return nd;
	}

	public ArrayList<Card> getDeckCards() {
		return deck.getCards();
	}
	
	public ArrayList<Card[]> getInPlayCards(){
		return inPlay.getCardPairs();
	}
	
	public ArrayList<Hand> getHands(){
		return playersHuman;
	}
	public Discard getDiscard(){
		return discard;
	}

}
