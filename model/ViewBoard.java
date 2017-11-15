package model;

/* Since there can only be a certain amount of cards displayed on the screen at a time,
 * the hand and cards within those hands can be scrolled through to find the required card.
 * Gaps between cards need to be defined as well as positiions of all elements on the board.
 * There may be a more staic way to implement this class.
 * Vectors would make this easire but would require a full refactor.
 *  All sizes should be defined here!
 */
public class ViewBoard {

	
	private float csX, csY;
	
	private final int visiblePlayers = 4;
	private final int visibleHandCards = 7;
	private final float gap = 0.02f;
	
	public ViewBoard (float csX, float csY) {
		this.csX = csX;
		this.csY = csY;
	}
	
	public float posDeckX() {
		return -1f;
	}
	
	public float posDeckY() {
		return 1f - csY;
	}
	
	
	public float posDiscX() {
		return 1f - csX;
	}
	
	public float posDiscY() {
		return 1f - csY;
	}
	
	public float[] posHandCardX() {
		float[] posX = new float[visibleHandCards];
		posX[0] = -1f + csX;
		for (int i = 1; i < posX.length; i++) {
			posX[i] = posX[i -1] + gap + csX;
		}
		return posX;
	}
	
	public float[] posHandY() {
		float[] posY = new float[visiblePlayers];
		posY[0] = -1f;
		for (int i = 1; i < posY.length; i++) {
			posY[i] = posY[i -1] + gap + csY;
		}
		return posY;
	}
	
	public float[] posInPlayX() {
		float[] posX = new float[6];
		posX[0] = 0f - (3 * csX + 2.5f * gap);
		for (int i = 1; i < posX.length; i++) {
			posX[i] = posX[i -1] + gap + csX;
		}
		return posX;
	}
	
	public float[] posInPlayY() {
		float[] posY = new float[2];
		posY[0] = 1f -( 2 * csY + gap);
		posY[1] = posY[0] + gap + csY;
		return posY;
	}
	
	public float posFoldX() {
		return 1f - (2* 100f / 800f);
	}
	
	public float[] posFoldY() {
		float[] posY = new float[visiblePlayers];
		posY[0] = -1f + gap;
		for (int i = 1; i < visiblePlayers; i++) {
			posY[i] = posY[i -1] + gap + csY;
		}
		return posY;
	}
	
	public float posDefenderTokenX() {
		return posFoldX() + (2* 50f / 800f);
	}
	
	public float[] posDefenderTokenY() {
		return posPlayerMarkY();
	}
	
	public float posPlayerMarkX() {
		return posFoldX();
	}
	
	public float[] posPlayerMarkY() {
		float[] posY = new float[visiblePlayers];
		posY[0] = -1f + csX;
		for (int i = 1; i < visiblePlayers; i++) {
			posY[i] = posY[i -1] + gap + csY;
		}
		return posY;
	}

	public float posLeftArrowX() {
		return -1f + gap;
	}
	public float posRightArrowX() {
		return posFoldX() - csX - gap;
	}
	
	public float[] posLRArrowY() {
		return posHandY();
	}

	public float getCsX() {
		return csX;
	}

	public float getCsY() {
		return csY;
	}
	
	public int getVisiblePlayers() {
		return visiblePlayers;
	}
	
	public int getVisibleHandCards() {
		return visibleHandCards;
	}
	
}
