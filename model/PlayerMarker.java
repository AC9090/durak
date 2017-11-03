package model;

import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import engine.Texture;

public class PlayerMarker extends GameObject {
	public PlayerMarker(float x, float y, float sx, float sy, int playerNo,  Texture tex) {
		this.x = x;
		this.y = y;
		this.sx = sx;
		this.sy = sy;
		
		this.vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
		vboIdVert = glGenBuffers();
		vboIdInd = glGenBuffers();
		glBindVertexArray(0);
		
		this.spr = new Sprite(491 + 50 * playerNo, 326, 50, 50 , tex, vaoId);

	}
	
	public boolean isClicked(float m_x, float m_y){
		return ((x + sx >= m_x && m_x >= x) 
				&& (y + sy >= m_y && m_y >= y));
	}
}
