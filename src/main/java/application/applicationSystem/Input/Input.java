package application.applicationSystem.Input;

import application.Application;
import application.applicationSystem.ApplicationSystem;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class Input implements ApplicationSystem
{
    private long m_window_long;

    @Override
    public void initialize()
    {
        m_window_long = Application.Window().get_window_long();
    }

    @Override
    public void destroy()
    {

    }

    public boolean keyboard_key_pressed(int key) { return glfwGetKey(m_window_long, key) == GLFW_PRESS; }

}
