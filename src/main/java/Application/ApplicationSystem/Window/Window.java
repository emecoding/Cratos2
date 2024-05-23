package Application.ApplicationSystem.Window;


import Application.ApplicationSystem.ApplicationSystem;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class Window implements ApplicationSystem
{
    private String m_Title;
    private int m_Width, m_Height;
    private int m_BufferBitsToClear = GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT;
    private Dictionary<Integer, Integer> m_WindowHints = new Hashtable<>();
    private long m_Window;
    private long m_Monitor = NULL;
    private boolean m_WindowShouldClose = false;
    private Vector4f m_BackgroundColor;

    public Window(String title, int width, int height)
    {
        m_Title = title;
        m_Width = width;
        m_Height = height;
        m_BackgroundColor = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void Initialize()
    {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit())
            throw new IllegalStateException("Failed to initialize GLFW...");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        SetEveryUserWindowHint();

        m_Window = glfwCreateWindow(m_Width, m_Height, m_Title, m_Monitor, NULL);
        if(m_Window == NULL)
            throw new RuntimeException("Failed to create the GLFW window...");

        SetUpWindowInputCallback();

        glfwMakeContextCurrent(m_Window);
        glfwSwapInterval(1);
        glfwShowWindow(m_Window);

        create_capabilities();
    }
    @Override
    public void Destroy()
    {
        glfwDestroyWindow(m_Window);
    }


    public void Center()
    {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(m_Window,
                (vidMode.width() - m_Width) / 2,
                (vidMode.height() - m_Height) / 2
        );
    }
    public void SetWindowHint(int key, int value)
    {
        m_WindowHints.put(key, value);
    }
    public void SetMonitor(long monitor) { m_Monitor = monitor; }
    public void SetWindowShouldClose(boolean value) { m_WindowShouldClose = value; glfwSetWindowShouldClose(m_Window, m_WindowShouldClose);}
    public void SetBufferBitsToClear(int buffer_bits_to_clear) { m_BufferBitsToClear = buffer_bits_to_clear; }
    public void SetBackgroundColor(float r, float g, float b, float a)
    {
        m_BackgroundColor.x = r;
        m_BackgroundColor.y = g;
        m_BackgroundColor.z = b;
        m_BackgroundColor.w = a;
    }
    public void ClearWindow()
    {
        glClearColor(m_BackgroundColor.x, m_BackgroundColor.y, m_BackgroundColor.z, m_BackgroundColor.w);
        glClear(m_BufferBitsToClear);
    }
    public void SwapBuffers() { glfwSwapBuffers(m_Window); }
    public void PollEvents() { glfwPollEvents(); }


    public boolean WindowShouldClose() { return m_WindowShouldClose; }
    public int GetWidth() { return m_Width; }
    public int GetHeight() { return m_Height; }
    public long GetWindowLong() { return m_Window; }

    private void SetEveryUserWindowHint()
    {
        Enumeration<Integer> keys = m_WindowHints.keys();
        while(keys.hasMoreElements())
        {
            Integer key = keys.nextElement();
            Integer value = m_WindowHints.get(key);
            glfwWindowHint(key, value);
        }
    }
    private void SetUpWindowInputCallback()
    {
        glfwSetKeyCallback(m_Window, (window, key, scancode, action, mods) -> {
           if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
               SetWindowShouldClose(true);
        });
    }
    private void create_capabilities() { GL.createCapabilities(); }
}
