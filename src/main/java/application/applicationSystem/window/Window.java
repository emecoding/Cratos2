package application.applicationSystem.window;


import application.applicationSystem.ApplicationSystem;
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
    private String m_title;
    private int m_width, m_height;
    private int m_buffer_bits_to_clear = GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT;
    private Dictionary<Integer, Integer> m_window_hints = new Hashtable<>();
    private long m_window;
    private long m_monitor = NULL;
    private boolean m_window_should_close = false;
    private Vector4f m_background_color;

    public Window(String title, int width, int height)
    {
        m_title = title;
        m_width = width;
        m_height = height;
        m_background_color = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void initialize()
    {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit())
            throw new IllegalStateException("Failed to initialize GLFW...");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        set_every_user_window_hint();

        m_window = glfwCreateWindow(m_width, m_height, m_title, m_monitor, NULL);
        if(m_window == NULL)
            throw new RuntimeException("Failed to create the GLFW window...");

        set_up_window_input_callback();

        glfwMakeContextCurrent(m_window);
        glfwSwapInterval(1);
        glfwShowWindow(m_window);

        create_capabilities();
    }
    public void destroy()
    {
        glfwDestroyWindow(m_window);
    }


    public void center()
    {
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(m_window,
                (vidMode.width() - m_width) / 2,
                (vidMode.height() - m_height) / 2
        );
    }
    public void set_window_hint(int key, int value)
    {
        m_window_hints.put(key, value);
    }
    public void set_monitor(long monitor) { m_monitor = monitor; }
    public void set_window_should_close(boolean value) { m_window_should_close = value; glfwSetWindowShouldClose(m_window, m_window_should_close);}
    public void set_buffer_bits_to_clear(int buffer_bits_to_clear) { m_buffer_bits_to_clear = buffer_bits_to_clear; }
    public void set_background_color(float r, float g, float b, float a)
    {
        m_background_color.x = r;
        m_background_color.y = g;
        m_background_color.z = b;
        m_background_color.w = a;
    }
    public void clear_window()
    {
        glClearColor(m_background_color.x, m_background_color.y, m_background_color.z, m_background_color.w);
        glClear(m_buffer_bits_to_clear);
    }
    public void swap_buffers() { glfwSwapBuffers(m_window); }
    public void poll_events() { glfwPollEvents(); }


    public boolean window_should_close() { return m_window_should_close; }
    public int get_width() { return m_width; }
    public int get_height() { return m_height; }
    public long get_window_long() { return m_window; }

    private void set_every_user_window_hint()
    {
        Enumeration<Integer> keys = m_window_hints.keys();
        while(keys.hasMoreElements())
        {
            Integer key = keys.nextElement();
            Integer value = m_window_hints.get(key);
            glfwWindowHint(key, value);
        }
    }
    private void set_up_window_input_callback()
    {
        glfwSetKeyCallback(m_window, (window, key, scancode, action, mods) -> {
           if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
               set_window_should_close(true);
        });
    }
    private void create_capabilities() { GL.createCapabilities(); }
}
