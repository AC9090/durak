package model;

import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import engine.Texture;
/*
 * Note that for the moment the card is just drawn after this object to make it go behind.
 */
public class CardLocator extends GameObject {
	
	
	public CardLocator(float x, float y, float sx, float sy,  Texture tex) {
		this.x = x;
		this.y = y;
		this.sx = sx;
		this.sy = sy;
		
		this.vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
		vboIdVert = glGenBuffers();
		vboIdInd = glGenBuffers();
		glBindVertexArray(0);
		
		this.spr = new Sprite(90, 326,  800/13, 326/4 , tex, vaoId);

	}
}
