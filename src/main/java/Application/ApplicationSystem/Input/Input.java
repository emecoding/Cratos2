package Application.ApplicationSystem.Input;

import Application.Application;
import Application.ApplicationSystem.ApplicationSystem;
import Application.ApplicationSystem.Debug;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

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
    public Vector2f GetMousePosition()
    {
        DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(m_WindowLong, xBuffer, yBuffer);
        return new Vector2f((float) xBuffer.get(0), (float) yBuffer.get(0));
    }
    public void SetCursorInputMode(int mode)
    {
        glfwSetInputMode(m_WindowLong, GLFW_CURSOR, mode);
    }
    public void SetCursorPosition(float x, float y)
    {
        glfwSetCursorPos(m_WindowLong, x, y);
    }
    public void SetCursorPositionOnWindow(float x, float y)
    {
        Vector2i windowPos = Application.Window().GetPosition();
        System.out.println(windowPos.x);
        SetCursorPosition(windowPos.x + x, windowPos.y + y);
    }
}
