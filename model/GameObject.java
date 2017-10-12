package model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;


public abstract class GameObject {
	
	protected int vboIdVert;
	protected int vboIdInd;
	protected float x;
	protected float y;
	
	protected float sx, sy;
	
	protected Sprite spr;
	protected int vaoId;
	
	public void update(){
		
	}
	
	public void render(){
		glBindVertexArray(vaoId);

		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(8).put(genVertexArray());
		vertexBuffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vboIdVert);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2, GL_FLOAT,false, 0, 0);
		
		spr.render();
		
		ByteBuffer indexBuffer = BufferUtils.createByteBuffer(6).put(new byte[] {0,1,2,0,2,3});
		indexBuffer.flip();
		glBindVertexArray(0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIdInd);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIdInd);
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, 0);
//		glDrawArrays(GL_QUADS, 0, 1);
		
		glBindVertexArray(0);

	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	private float[] genVertexArray(){
		
		return new float[] {
				x, y,
				x, y + sy,
				x + sx, y + sy,
				x + sx, y
		};
	}

}
