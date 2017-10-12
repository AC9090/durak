package cards;

public enum Suit{
	SPADES (0),
	CLUBS (1),
	DIAMONDS (2),
	HEARTS (3);
	
	private final int value;
	Suit(int value ){
		this.value = value;
	}
	
	public int value() {return value;}
}