//Heavily borrowed code from stackOverflow because.
//if I don't abstract away 40 minutes of openGL nonsence to show a PNG I will literally die.

//cheers!

import java.nio.*;
import java.io.IOException;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GLCapabilities.*;
import static org.lwjgl.opengl.GL30.*;

public class PNGTexture{

	private int id;

	public PNGTexture(int id){ this.id = id; }

	public int getId(){ return id; }

	public static PNGTexture loadTexture(String filename) throws IOException{

		PNGDecoder pngdecoder = new PNGDecoder(
			PNGTexture.class.getClassLoader().getResourceAsStream(filename));

		ByteBuffer buffer = ByteBuffer.allocateDirect(4 * pngdecoder.getWidth() * pngdecoder.getHeight());

		pngdecoder.decode(buffer, pngdecoder.getWidth()*4, PNGDecoder.Format.RGBA);

		buffer.flip();

		int id = glGenTextures();

		glBindTexture(GL_TEXTURE_2D, id);

		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, pngdecoder.getWidth(), pngdecoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		glGenerateMipmap(GL_TEXTURE_2D);

		return new PNGTexture(id);

	}

}
