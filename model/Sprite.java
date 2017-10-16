package model;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import engine.Texture;

public class Sprite {
	private final int x, y;
	private final int SIZEX, SIZEY;
	private Texture tex;
	private float[] colors = {
		1.0f,1.0f,1.0f,1.0f,
		1.0f,1.0f,1.0f,1.0f,
		1.0f,1.0f,1.0f,1.0f,
		1.0f,1.0f,1.0f,1.0f};
	
	private float[] texVert;
	
	private final int vaoId;
	private int vboIdTex;
	private int vboIdCol;
	public Sprite(int x, int y, int sx, int sy, Texture tex, int vaoId) {
		this.x = x;
		this.y = y;
		this.SIZEX = sx;
		this.SIZEY = sy;
		this.tex = tex;
		
		texVert = genVertexArray();
		
		this.vaoId = vaoId;
		glBindVertexArray(vaoId);
		vboIdCol = glGenBuffers();
		vboIdTex = glGenBuffers();
		glBindVertexArray(0);
	}

	public void render() {
		glBindVertexArray(vaoId);
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(16).put(colors);
		colorBuffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, vboIdCol);
		glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(8).put(texVert);
		vertexBuffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, vboIdTex);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
		tex.bind();
		
			
		
	}
	
	
	private float[] genVertexArray(){
		float f_x = ((float) x) / ((float) tex.getSIZEX());
		float f_y = ((float) y) / ((float) tex.getSIZEY());
		float f_sx = ((float) SIZEX) / ((float) tex.getSIZEY());
		float f_sy = ((float) SIZEY) / ((float) tex.getSIZEY());
		return new float[] {
				f_x, f_y + f_sy,
				f_x, f_y,
				f_x + f_sx, f_y,
				f_x + f_sx, f_y + f_sy,
		};
	}
	
	public void setColors(float[] a){
		if (a.length != 16) return;
		
		colors = a;
	}

}
