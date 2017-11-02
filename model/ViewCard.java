package model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.*;
import cards.Suit;
import engine.Texture;

public class ViewCard extends GameObject {
	
	private boolean faceDown;
	private Sprite faceSpr, backSpr;
	private Suit suit;
	private int value;
	
	private boolean highlighted;
	
	public ViewCard(float x, float y, float sx, float sy, Suit suit, int value, Texture tex){
		this.x = x;
		this.y = y;
		this.sx = sx;
		this.sy = sy;
		this.suit=suit;
		this.value = value;
		
		highlighted = false;
		faceDown = false;
		
		this.vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
		vboIdVert = glGenBuffers();
		vboIdInd = glGenBuffers();
		glBindVertexArray(0);
		
		faceSpr = new Sprite(800/13 * (value - 1), 326/4 * suit.value(), 800/13, 326/4, tex, vaoId);
		backSpr = new Sprite(0, 326/4 * 4, 800/13, 326/4, tex, vaoId);
		this.spr = faceSpr;
	}
	
	public void flip(){
		faceDown =  !faceDown;
		spr = faceDown ? backSpr : faceSpr;
	}
	
	public void setFaceDown(){
		faceDown = true;
		spr = backSpr;
	}
	public void setFaceUp(){
		faceDown = false;
		spr = faceSpr;
	}

	public boolean isFaceDown() {
		return faceDown;
	}

	public Suit getSuit() {
		return suit;
	}

	public int getValue() {
		return value;
	}
	
	public void setHighlighted(boolean b){
		if (b){
			faceSpr.setColors(new float[] {
					1.0f,1.0f,1.0f,1.0f,
					1.0f,1.0f,1.0f,1.0f,
					1.0f,0f,0.0f,1.0f,
					1.0f,0f,0.0f,1.0f
			});
		} else {
			faceSpr.setColors(new float[] {
					1.0f,1.0f,1.0f,1.0f,
					1.0f,1.0f,1.0f,1.0f,
					1.0f,1.0f,1.0f,1.0f,
					1.0f,1.0f,1.0f,1.0f
			});
		}
		highlighted = b;
	}
	
	public boolean isClicked(float m_x, float m_y){
		return ((x + sx >= m_x && m_x >= x) 
				&& (y + sy >= m_y && m_y >= y));
	}
	
}
