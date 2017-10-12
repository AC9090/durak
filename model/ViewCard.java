package model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.*;
import cards.Suit;
import engine.Texture;

public class ViewCard extends GameObject {
	
	private boolean faceDown;
	Sprite faceSpr, backSpr;
	public ViewCard(float x, float y, float sx, float sy, Suit suit, int value, Texture tex){
		this.x = x;
		this.y = y;
		this.sx = sx;
		this.sy = sy;
		
		
		
		
		this.vaoId = glGenVertexArrays();
		faceDown = false;
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

	public boolean isFaceDown() {
		return faceDown;
	}
}
