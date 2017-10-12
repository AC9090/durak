package model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;


public class Texture {
	
	private int id; 
	private final int SIZEX, SIZEY;
	public int getId() {
		return id;
	}


	public Texture(int id, int sx, int sy){
		this.id = id;
		this.SIZEX = sx;
		this.SIZEY = sy;
	}
	
	public void bind(){
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public int getID(){
		return id;
	}
	public int getSIZEX() {
		return SIZEX;
	}
	
	public int getSIZEY() {
		return SIZEY;
	}
}
