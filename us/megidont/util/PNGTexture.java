package us.megidont.util;

//Heavily borrowed code from stackOverflow because.
//if I don't abstract away 40 minutes of openGL nonsence to show a PNG I will literally die.

//cheers!

//Jave imports
import java.nio.*;
import java.io.IOException;

//PNGDecoder imports
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

//LWJGL imports
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GLCapabilities.*;
import static org.lwjgl.opengl.GL30.*;

public class PNGTexture{

//	Stores the ID of the GLObject of the instance
	private int id;

//	Boilerplate constructor, maybe make this private in future?
	public PNGTexture(int id){ this.id = id; }

//	Getter, no need for a setter because the ID is immutable
	public int getId(){ return id; }

//	A static method will allow us to call PNGTexture.loadTexture to create a new instance!
	public static PNGTexture loadTexture(String filename) throws IOException{

//		Create new PNGDecoder from the filename, can easily throw an IOE or
//		quite a few other exceptions, but it's better for us all if we don't have to
//		catch the other types of exceptions. More info for me when things don't work!
		PNGDecoder pngdecoder = new PNGDecoder(
			PNGTexture.class.getClassLoader().getResourceAsStream(filename));

//		Have to allocate a bytebuffer to store the decoded PNG in!
		ByteBuffer buffer = ByteBuffer.allocateDirect(4 * pngdecoder.getWidth() * pngdecoder.getHeight());

//		Decode the PNG and throw it all into the buffer...
		pngdecoder.decode(buffer, pngdecoder.getWidth()*4, PNGDecoder.Format.RGBA);

//		...and flip it because decode shoves it in FIFO.
		buffer.flip();

//		Create a GL Texture Object and store its id to use to call the constructor!
		int id = glGenTextures();

//		Tell our openGL Context that we're working with the GL_TEXTURE_2D id
		glBindTexture(GL_TEXTURE_2D, id);

//		Tell our context that unpacking should align to bytes!
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

//		Tell our context that it should use Nearest filtering because I like it more
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

//		TODO: add an arg to use something other than Nearest if needed!

//		Finally, create the texture image from the ByteBuffer we have...
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, pngdecoder.getWidth(), pngdecoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

//		...Generate the mipmap...
		glGenerateMipmap(GL_TEXTURE_2D);

//		...and construct a wrapper for it to keep track of the ID!
		return new PNGTexture(id);

	}

}
