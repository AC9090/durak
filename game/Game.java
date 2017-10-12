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
		//deal the cards to the players
		for (int i = 0; i < 6; i++){ 
			for (Hand player : playersHuman){
				player.add(deck.pickUp());
			}
		}
	}
	
	public void play(Hand h, Card c, int selection) throws InvalidMove, InvalidPlayer{

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

/*
	public void run() {


		boolean isFinished = false;
	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//each round is contained in this loop until only one remains
		round:
		while(!isFinished){
			//main game loop
			
			System.out.println("Attacker " + attacker + ":");
			System.out.println(playersHuman.get(attacker).toString());
			selection = getCardChoice(attacker);
			inPlay.attack(playersHuman.get(attacker).remove(selection));
			
			System.out.println("Defender:");
			System.out.println(playersHuman.get(defender).toString());
			selection = getCardChoice(defender);
			
			if (selection == -1){
				playersHuman.get(defender).add(inPlay.fold());
				continue;
			} else {
				inPlay.defend(playersHuman.get(defender).remove(selection), 0); //TODO: Handle invalid cards (note logic exists returns 1 for invalid)
			}
			
			while (true) {
				while (true) {
					//all attackers that want to can now place their cards. (no support for single attacker yet)
					System.out
							.println("Please select an attacker from 0 to "
									+ (playersHuman.size() - 1)
									+ " that is not defender " + defender
									+ ".");
					//select a player
					while (true) {

						selection = getSelection();
						if ((selection != defender && ! notDurak.contains(playersHuman.get(selection)))
								&& (selection < playersHuman.size())) {
							attacker = selection;
							break;
						} else
							System.out.println("try again...");

					}
					//select a card to attack with
					System.out.println("Attacker " + attacker + ":");
					System.out.println(playersHuman.get(attacker).toString());
					selection = getCardChoice(attacker);
					inPlay.attack(playersHuman.get(attacker).remove(
							selection)); //TODO: Handle invalid choice
					
					//max number of cards placed?
					if (inPlay.numBattles() < 6) {
						System.out
								.println("Attack Again (1 for yes 0 for no)?");
						selection = getSelection();
						if (selection == 0) {
							break;
						}
					}
				notDurak.addAll(notDurak());
				}
				System.out.println("Defender:");
				while (true) {
					//the defender must now defend
					System.out.println("Choose card to defend against:");
					System.out.print(inPlay.toString());
					
					int selectionDef = getSelection();
					
					//player folds
					if (selection == -1) {
						playersHuman.get(defender).add(inPlay.fold());
						System.out.println("Defender is beaten.");
						break round;
					//chosen a card to defend against
					} else if (selection < playersHuman.get(defender).getCards().size()){
						selection = getCardChoice(defender);
						inPlay.defend(playersHuman.get(defender).remove(selection), selectionDef); //TODO: Handle invalid cards
					} else {
						System.out.println("Card is invalid, try again.");
						continue;
					}
					notDurak.addAll(notDurak());
					if(notDurak.contains(playersHuman.get(defender))){
						defender += 1;
						while (notDurak.contains(playersHuman.get(defender))) {
							defender += 1;
							defender %= playersHuman.size();
						}
						break round;
					}
					if (!inPlay.isDefended()) {
						System.out
								.println("Defended current attack.");
						break;
						
					}
				}
				if(inPlay.isDefended() && inPlay.numBattles() == 6){
					System.out.println("Succesfully defended!");
					while (notDurak.contains(playersHuman.get(defender))) {
						defender += 1;
						defender %= playersHuman.size();
					}
					break round;
				}

			}
				
			
		}
		
	}

	
	public int getCardChoice(int player){
		int selection = -2;
		
		playersHuman.get(player).toString();
	
		System.out.println("Please select a card to play with or type -1 to give up.");
		while(true){
			
			selection = getSelection();

			if (-1 <= selection && selection < playersHuman.get(player).getCards().size()){
				return selection;
			}
			else
				System.out.println("Try another selection");
		}
		
	}
	private int getSelection(){
		int selection;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			selection = Integer.parseInt(br.readLine());
			return selection;
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage() + " could not find number");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return -2;
	}
*/
	
	private ArrayList<Hand> notDurak(){
		ArrayList<Hand> nd = new ArrayList<Hand>();
		for(Hand player: playersHuman){
			if(player.getCards().size() == 0)
				nd.add(player);
				System.out.println("Player " + playersHuman.indexOf(player) + "is out!");
				
		}
		return nd;
	}

}
