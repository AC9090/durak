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
		Game game  = new Game(2,0);
		game.run();
	}
	

	public Game(int numPlayersHuman, int numPlayersCpu){
		deck = new Deck();
		deck.buildDeck();
		trump = deck.pickTrump();
		inPlay = new InPlay(trump);
		
		for(int i = 0; i <  numPlayersHuman; i++){
			playersHuman.add(new Hand());
		}
	}
	
	private void run() {
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
		
	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		
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
						System.out
								.println("Please select an attacker from 0 to "
										+ (playersHuman.size() - 1)
										+ " that is not defender " + defender
										+ ".");
						while (true) {

							selection = getSelection();
							if ((selection != defender)
									&& (selection < playersHuman.size())) {
								attacker = selection;
								break;
							} else
								System.out.println("try again...");

						}
						System.out.println("Attacker " + attacker + ":");
						playersHuman.get(attacker).toString();
						selection = getCardChoice(attacker);
						inPlay.attack(playersHuman.get(attacker).remove(
								selection));

						if (inPlay.numBattles() < 6) {
							System.out
									.println("Attack Again (1 for yes 0 for no)?");
							selection = getSelection();
							if (selection == 0) {
								break;
							}
						}
					}
					while (true) {
						System.out.println("Defender:");
						System.out.println("Choose card to defend against:");
						inPlay.toString();
						
						selection = getCardChoice(defender);

						if (selection == -1) {
							playersHuman.get(defender).add(inPlay.fold());
							continue;
						} else {
							inPlay.defend(
									playersHuman.get(defender)
											.remove(selection), 0);
						}
						if (!inPlay.isDefended()) {
							System.out
									.println("Defend again (1 for yes 0 for no)?");
							selection = getSelection();
							if (selection == 0)
								break;
						}
					}
					if(inPlay.isDefended() && inPlay.numBattles() == 6){
						System.out.println("Succesfully defended!");
						defender += 1;
						defender %= playersHuman.size() - 1;
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

}
