package Application.GameObjectComponent;

import Application.Application;
import Application.ApplicationSystem.Debug;
import Application.Utils.Time;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

//https://learnopengl.com/Getting-started/Camera

public class CameraMovement extends GameObjectComponent
{
    private float m_Speed;
    private float m_Sensitivity;

    private Vector2f m_LastMousePosition;

    private Camera m_Camera;

    public static final float DEFAULT_SPEED = 1000.0f;
    public static final float DEFAULT_SENSITIVITY = 10000.0f;

    public CameraMovement()
    {
        m_LastMousePosition = Application.Input().GetMousePosition();

        SetSpeed(DEFAULT_SPEED);
        SetSensitivity(DEFAULT_SENSITIVITY);
    }

    private void Turn()
    {
        Vector2f currentMousePosition = Application.Input().GetMousePosition();

        float xOffset = currentMousePosition.x - m_LastMousePosition.x;
        float yOffset = currentMousePosition.y - m_LastMousePosition.y;

        m_LastMousePosition.x = currentMousePosition.x;
        m_LastMousePosition.y = currentMousePosition.y;

        xOffset *= m_Sensitivity * Time.DeltaTime();
        yOffset *= m_Sensitivity * Time.DeltaTime();

        m_Camera.SetYaw(m_Camera.Yaw() + xOffset);
        m_Camera.SetPitch(m_Camera.Pitch() - yOffset);
    }

    private void Move()
    {
        float speed = m_Speed * Time.DeltaTime();
        if(Application.Input().KeyboardKeyPressed(GLFW_KEY_W))
            m_Parent.m_Transform.Position.add(m_Camera.Front().mul(speed));
        if(Application.Input().KeyboardKeyPressed(GLFW_KEY_S))
            m_Parent.m_Transform.Position.sub(m_Camera.Front().mul(speed));
        if(Application.Input().KeyboardKeyPressed(GLFW_KEY_A))
            m_Parent.m_Transform.Position.sub(m_Camera.Front().cross(m_Camera.Up()).normalize().mul(speed));
        if(Application.Input().KeyboardKeyPressed(GLFW_KEY_D))
            m_Parent.m_Transform.Position.add(m_Camera.Front().cross(m_Camera.Up()).normalize().mul(speed));
    }

    @Override
    public void Update()
    {

        if(m_Camera == null)
            m_Camera = (Camera) m_Parent.GetComponent(Camera.class);

        if(m_Camera == null)
        {
            Debug.Error("Failed to find camera component from the GameObject");
            return;
        }

        Turn();
        Move();
    }

    public void SetSpeed(float speed) { m_Speed = speed; }
    public void SetSensitivity(float sensitivity) { m_Sensitivity = sensitivity; }
}
