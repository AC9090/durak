package game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cards.Card;
import cards.Deck;
import cards.Hand;
import cards.InPlay;
import cards.Suit;
public class Game {

	//no multithread support
	Deck deck;
	ArrayList<Hand> playersHuman = new ArrayList<Hand>();
	InPlay inPlay;
	Suit trump;
	public static void main(String[] args) {
		//method that starts program
		Game game  = new Game(2,0);
		game.run();
	}
	

	public Game(int numPlayersHuman, int numPlayersCpu){
		//constructs relevant objects
		deck = new Deck();
		deck.buildDeck();
		trump = deck.pickTrump();
		inPlay = new InPlay(trump);
		
		for(int i = 0; i <  numPlayersHuman; i++){
			playersHuman.add(new Hand());
		}
	}
	
	private void run() {
		//deal the cards to the players
		for (int i = 0; i <  2/*6 TODO:*/; i++){
			for (Hand player : playersHuman){
				player.add(deck.pickUp());
			}
		}
		
		boolean isFinished = false;
		//TODO: find player with lowest hand to start
		int defender = 0;
		int attacker;
		int selection = -2;
		
		//for chuvaks that have no cards left
		ArrayList<Hand> notDurak = new ArrayList<Hand>();
	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//each round is contained in this loop until only one remains
		round:
		while(!isFinished){
			//main game loop
			attacker = (defender - 1) % (playersHuman.size() - 1);
			
			System.out.println("Attacker " + attacker + ".");
			selection = getCardChoice(attacker);
			inPlay.attack(playersHuman.get(attacker).remove(selection));
			
			System.out.println("Defender:");
			selection = getCardChoice(defender);
			
			if (selection == -1){
				playersHuman.get(defender).add(inPlay.fold());
				continue;
			} else {
				inPlay.defend(playersHuman.get(defender).remove(selection), 0);
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
					playersHuman.get(attacker).toString();
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
					inPlay.toString();
					
					int selectionDef = getSelection();
					
					//player folds
					if (selection == -1) {
						playersHuman.get(defender).add(inPlay.fold());
						System.out.println("Defender is beaten");
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
							defender %= playersHuman.size() - 1;
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
						defender %= playersHuman.size() - 1;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -2;
	}
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
