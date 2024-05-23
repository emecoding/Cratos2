package Application.ApplicationSystem.Input;

import Application.Application;
import Application.ApplicationSystem.ApplicationSystem;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class Input implements ApplicationSystem
{
    private long m_WindowLong;

    @Override
    public void Initialize()
    {
        m_WindowLong = Application.Window().GetWindowLong();
    }

    @Override
    public void Destroy()
    {

    }

    public boolean KeyboardKeyPressed(int key) { return glfwGetKey(m_WindowLong, key) == GLFW_PRESS; }

}
