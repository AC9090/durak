package model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.*;
import cards.Suit;

public class ViewCard extends GameObject {

	
	public ViewCard(float x, float y, float sx, float sy, Suit suit, int value, Texture tex){
		this.x = x;
		this.y = y;
		this.sx = sx;
		this.sy = sy;
		
		this.vaoId = glGenVertexArrays();
		
		glBindVertexArray(vaoId);
		vboIdVert = glGenBuffers();
		vboIdInd = glGenBuffers();
		glBindVertexArray(0);
		
		this.spr = new Sprite(800/13 * (value - 1), 326/4 * suit.value(), 800/13, 326/4, tex, vaoId);
	}
}
