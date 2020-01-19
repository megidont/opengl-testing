//Java Imports

import java.nio.*;
import java.util.*;
import java.io.*;

//LWJGL Imports

//non-static imports:

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

//static imports:

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

//custom imports:

import us.megidont.util.*;

public class test{

//	create a PNGTexture to store our texture from our png in later!
//	private PNGTexture testTexture;

//	create a global variable for the main window we're testing with and its title
	private long testWindow;
	private String testWindowTitle = "Color Machine Mk. 69";

//	these say whether or not we're calling incrementation on next loop
	private boolean shouldRedInc = false;
	private boolean shouldGreenInc = false;
	private boolean shouldBlueInc = false;

//	these store current colour values
	private float curRed = (float)0x88/0xff;
	private float curGreen = (float)0x22/0xff;
	private float curBlue = (float)0x99/0xff;

//	store previous coordinates for toggling fullscreen, with default values!
	private int prevx = 40;
	private int prevy = 40;

//	increment and return a colour float
	public float incCol(float col){

		if (col == (float)0xff/0xff){

//			avoid overflow by resetting to 0 instead of incrementing past 1.0...
			col = (float)0x00/0xff;

		}else{

//			or just increment by .0625 if it's not a problem.
			col += (float)0x11/0xff;

		}

		return col;

	}

//	run this as the main function so that we don't have to bog down a main function
	public void run(){

//		initialize:
		init();

//		begin the loop:
		loop();

//		If we leave the loop because the window is closed, this cleans up after us

//		free all the callbacks,
		glfwFreeCallbacks(testWindow);

//		destroy the window,
		glfwDestroyWindow(testWindow);

//		end the GLFW instance,
		glfwTerminate();

//		and free the error callback memory up
		glfwSetErrorCallback(null).free();

	}

//	initialize the window
	public void init(){

//		set up our error callbacks to print to System.err
		GLFWErrorCallback.createPrint(System.err).set();

//		throw exceptions if GLFW fails to initialize
		if(!glfwInit()){throw new IllegalStateException("GLFW Machine Broke");}

//		GLFW window hints set up various details for the next window made
//		invisible for the moment so we can set the size properly
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
//		resizeable because sure, why not
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

//		initialize the test window...
		testWindow = glfwCreateWindow(640, 480, testWindowTitle, NULL, NULL);

//		...and check if that failed.
		if(testWindow == NULL){throw new RuntimeException("Run this from cmd, dorkus");}

//		look for keypresses in testWindow and run the callback if a key is pressed
		glfwSetKeyCallback(testWindow, (window, key, scancode, action, mods)->{

			if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){

//				Prepare the window to close on next loop tick
				glfwSetWindowShouldClose(window, true);

			}else if(key == GLFW_KEY_F11 && action == GLFW_RELEASE){

//				check if fullscreen
				if(glfwGetWindowMonitor(window) != NULL){

//					if so, get out of fullscreen
					glfwSetWindowMonitor(window, NULL, prevx, prevy, 640, 480, GLFW_DONT_CARE);

				}else{

//					if not, get current window location...
					try (MemoryStack stack = stackPush()){

//						prepare an intbuffer to store variables in...
						IntBuffer currentTestWindowX = stack.mallocInt(1);
						IntBuffer currentTestWindowY = stack.mallocInt(1);

//						...get the variables...
						glfwGetWindowPos(testWindow, currentTestWindowX, currentTestWindowY);

//						...and store them in our nice spot!
						prevx = currentTestWindowX.get();
						prevy = currentTestWindowY.get();

					}

//					...and get into fullscreen
					glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), GLFW_DONT_CARE, GLFW_DONT_CARE, 640, 480, GLFW_DONT_CARE);

				}

//			Set variables to be checked in the loop!
			}else if(key == GLFW_KEY_R && action == GLFW_RELEASE){

				shouldRedInc = true;

			}else if(key == GLFW_KEY_G && action == GLFW_RELEASE){

				shouldGreenInc = true;

			}else if(key == GLFW_KEY_B && action == GLFW_RELEASE){

				shouldBlueInc = true;

			}

		});

//		we say that this window is the one we're using for all the stuff on this thread
		glfwMakeContextCurrent(testWindow);

//		enable vsync
		glfwSwapInterval(1);

//		reveal our window
		glfwShowWindow(testWindow);

//		and we create our glcapabilities instance!
		GL.createCapabilities();


/*Running before I walk...

//		try-catch for IOE
		try{

//			here we create our new PNGTexture based on test.png...
			testTexture = PNGTexture.loadTexture("./test.png");

		}catch(IOException e){

//			...that is, if we don't get an IOException because it doesn't exist!
			System.out.println("IOException: " + e.getMessage());

		}

*/

//		this disables the cursor to capture the mouse! a suitable mouse trap.
//		glfwSetInputMode(testWindow, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

	}

//	and finally we have our main loop!
	public void loop(){

//		set the colour we use for a cleared buffer when we first start the loop
		glClearColor(curRed, curGreen, curBlue, (float)0x00/0xff);

//		watch for window needing to close, and if it needs to close break the loop
		while(!glfwWindowShouldClose(testWindow)){

//			clear our colour buffer and depth buffer, filling with glClearColor
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

//			switch between writing to the buffer and showing the one we wrote on
//			(next interval ofc!)
			glfwSwapBuffers(testWindow);

//			look for events happening (keyboard for right now!)
			glfwPollEvents();

//			Each of these increments the colour (red, green, or blue) when necessary
//			and also updates the clear colour!
			if(shouldRedInc){

				shouldRedInc = false;
				curRed = incCol(curRed);
				glClearColor(curRed, curGreen, curBlue, (float)0x00/0xff);

			}

			if(shouldGreenInc){

				shouldGreenInc = false;
				curGreen = incCol(curGreen);
				glClearColor(curRed, curGreen, curBlue, (float)0x00/0xff);

			}

			if(shouldBlueInc){

				shouldBlueInc = false;
				curBlue = incCol(curBlue);
				glClearColor(curRed, curGreen, curBlue, (float)0x00/0xff);

			}

		}

	}

	public static void main(String[] args){

//		Test to make sure imports worked right, first LWJGL...
		System.out.println("LWJGL version " + org.lwjgl.Version.getVersion());
//		...then GLFW!
		System.out.println("GLFW  version " + Integer.toString(GLFW_VERSION_MAJOR) + "." + Integer.toString(GLFW_VERSION_MINOR));

//		Run the run() function
//		Ah, yes, the run() here is made out of run()
		new test().run();

	}
}
