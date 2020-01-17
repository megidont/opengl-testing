import java.nio.*;
import java.util.*;

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

	private long testWindow;

	public void run(){

		init();
		loop();

		glfwFreeCallbacks(testWindow);
		glfwDestroyWindow(testWindow);

		glfwTerminate();
		glfwSetErrorCallback(null);

	}

	public void init(){

		if(!glfwInit()){throw new IllegalStateException("GLFW Machine Broke");}

		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		testWindow = glfwCreateWindow(640, 480, "Test", glfwGetPrimaryMonitor(), NULL);

		if(testWindow == NULL){throw new RuntimeException("Run this from cmd, dorkus");}

		glfwSetKeyCallback(testWindow, (window, key, scancode, action, mods)->{

			if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){

				glfwSetWindowShouldClose(window, true);

			}else if(key == GLFW_KEY_F11 && action == GLFW_RELEASE){

				glfwSetWindowMonitor(window, NULL, 40, 40, 640, 480, GLFW_DONT_CARE);

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

	}

	public void loop(){

		GL.createCapabilities();

		glClearColor((float)0x88/0xFF, (float)0x22/0xFF, (float)0x99/0xFF, (float)0x00/0x00);

		while(!glfwWindowShouldClose(testWindow)){

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			glfwSwapBuffers(testWindow);

			glfwPollEvents();

		}

	}

	public static void main(String[] args){

		System.out.println("LWJGL version " + org.lwjgl.Version.getVersion());
		System.out.println("GLFW  version " + Integer.toString(GLFW_VERSION_MAJOR) + "." + Integer.toString(GLFW_VERSION_MINOR));

		new test().run();

	}
}
