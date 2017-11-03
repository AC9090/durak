package model;

import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import engine.Texture;

public class LRArrow extends GameObject {
	public LRArrow(float x, float y, float sx, float sy, boolean isL,  Texture tex) {
		this.x = x;
		this.y = y;
		this.sx = sx;
		this.sy = sy;
		
		this.vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
		vboIdVert = glGenBuffers();
		vboIdInd = glGenBuffers();
		glBindVertexArray(0);
		
		
		if (isL) {
			this.spr = new Sprite(380, 326, 20, 50 , tex, vaoId);
		} else {
			this.spr = new Sprite(410, 326, 20, 50 , tex, vaoId);			
		}
	}
	
	public boolean isClicked(float m_x, float m_y){
		return ((x + sx >= m_x && m_x >= x) 
				&& (y + sy >= m_y && m_y >= y));
	}
}
