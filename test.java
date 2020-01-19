import java.nio.*;
import java.util.*;
import java.io.*;

import org.lwjgl.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class test{

	private PNGTexture testTexture;

	private long testWindow;
	private String testWindowTitle = "Color Machine Mk. 69";

	private boolean shouldRedInc = false;
	private boolean shouldGreenInc = false;
	private boolean shouldBlueInc = false;

	private float curRed = (float)0x88/0xff;
	private float curGreen = (float)0x22/0xff;
	private float curBlue = (float)0x99/0xff;

	public float incCol(float col){

		if (col == (float)0xff/0xff){

			col = (float)0x00/0xff;

		}else{

			col += (float)0x11/0xff;

		}

		return col;

	}

	public void run(){

		init();
		loop();

		glfwFreeCallbacks(testWindow);
		glfwDestroyWindow(testWindow);

		glfwTerminate();
		glfwSetErrorCallback(null).free();

	}

	public void init(){

		GLFWErrorCallback.createPrint(System.err).set();

		if(!glfwInit()){throw new IllegalStateException("GLFW Machine Broke");}

		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		testWindow = glfwCreateWindow(640, 480, testWindowTitle, NULL, NULL);

		if(testWindow == NULL){throw new RuntimeException("Run this from cmd, dorkus");}

		glfwSetKeyCallback(testWindow, (window, key, scancode, action, mods)->{

			if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){

				glfwSetWindowShouldClose(window, true);

			}else if(key == GLFW_KEY_F11 && action == GLFW_RELEASE){

				if(glfwGetWindowMonitor(window) != NULL){

					glfwSetWindowMonitor(window, NULL, 40, 40, 640, 480, GLFW_DONT_CARE);

				}else{

					glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), GLFW_DONT_CARE, GLFW_DONT_CARE, 640, 480, GLFW_DONT_CARE);

				}

			}else if(key == GLFW_KEY_R && action == GLFW_RELEASE){

				shouldRedInc = true;

			}else if(key == GLFW_KEY_G && action == GLFW_RELEASE){

				shouldGreenInc = true;

			}else if(key == GLFW_KEY_B && action == GLFW_RELEASE){

				shouldBlueInc = true;

			}

		});

		try (MemoryStack stack = stackPush()){

			IntBuffer currentTestWindowWidth = stack.mallocInt(1);
			IntBuffer currentTestWindowHeight = stack.mallocInt(1);

			glfwGetWindowSize(testWindow, currentTestWindowWidth, currentTestWindowHeight);

			GLFWVidMode currentVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

//			System.out.println("currentTestWindowWidth: " + Arrays.toString(currentTestWindowWidth.array()));
//			System.out.println("currentTestWindowHeight: " + Arrays.toString(currentTestWindowHeight.array()));

		}

		glfwMakeContextCurrent(testWindow);

		glfwSwapInterval(1);

		glfwShowWindow(testWindow);

		GL.createCapabilities();


/*Running before I walk...

		try{

			testTexture = PNGTexture.loadTexture("./test.png");

		}catch(IOException e){

			System.out.println("IOException: " + e.getMessage());

		}

*/

//Steal the mouse:

//		glfwSetInputMode(testWindow, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

	}

	public void loop(){

		glClearColor(curRed, curGreen, curBlue, (float)0x00/0xff);

		while(!glfwWindowShouldClose(testWindow)){

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			glfwSwapBuffers(testWindow);

			glfwPollEvents();

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

		System.out.println("LWJGL version " + org.lwjgl.Version.getVersion());
		System.out.println("GLFW  version " + Integer.toString(GLFW_VERSION_MAJOR) + "." + Integer.toString(GLFW_VERSION_MINOR));

		new test().run();

	}
}
