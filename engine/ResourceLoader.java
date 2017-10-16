package engine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class ResourceLoader {
	
	
	public static Texture loadTexture(String fileName, int textureUnit){
		ByteBuffer buf = null;
		int tWidth = 0;
		int tHeight = 0;
		try {
			InputStream in = new FileInputStream("./res/" + fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			buf = ByteBuffer.allocateDirect(
					4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
			tWidth = decoder.getWidth();
			tHeight = decoder.getHeight();
			
			buf.flip();
			
			in.close();
			
		}catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		glActiveTexture(textureUnit);
		int texId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texId);
		
		 glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		 glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		 glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		 glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, tWidth, tHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
		 glGenerateMipmap(GL_TEXTURE_2D);
		 
		 return new Texture(texId, tWidth, tHeight);
	}
}
