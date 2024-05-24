package Application.GameObjectComponent;

import Application.Application;
import Application.ApplicationSystem.Debug;
import Application.Utils.Time;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.lwjgl.glfw.GLFW.*;

//https://learnopengl.com/Getting-started/Camera
// Camera.m_Up = cameraUp


public class Camera extends GameObjectComponent
{
    private Matrix4f m_3D_View;
    private Matrix4f m_2D_View;
    private Matrix4f m_OrthoProjection;

    private float m_2D_Z;
    private float m_Yaw = 0.0f;
    private float m_Pitch = 0.0f;
    private float m_FOV = 0.0f;

    private Vector3f m_Target;
    private Vector3f m_ReverseDirection;
    private Vector3f m_Direction;
    private Vector3f m_Up;
    private Vector3f m_Right;
    private Vector3f m_Front;

    public static final float DEFAULT_CAMERA_3D_Z = -3.0f;
    public static final float DEFAULT_CAMERA_2D_Z = -2.0f;
    public static final float DEFAULT_YAW = 90.0f;
    public static final float DEFAULT_PITCH = 0.0f;
    public static final float DEFAULT_FOV = 90.0f;

    public Camera()
    {
        m_2D_Z = DEFAULT_CAMERA_2D_Z;
        m_Yaw = DEFAULT_YAW;
        m_Pitch = DEFAULT_PITCH;
        m_FOV = DEFAULT_FOV;
    }

    @Override
    public void Initialize()
    {
        Application.Renderer().AddCamera(this);
        m_3D_View = new Matrix4f();
        m_2D_View = new Matrix4f();
        m_OrthoProjection = new Matrix4f().ortho(0.0f, Application.Window().GetWidth(), Application.Window().GetHeight(), 0.0f, -1.0f, 1.0f);

        m_Target = new Vector3f(0.0f);
        m_ReverseDirection = new Vector3f(m_Parent.m_Transform.Position).sub(m_Target).normalize();

        m_Direction = new Vector3f(-m_ReverseDirection.x, -m_ReverseDirection.y, -m_ReverseDirection.z);
        m_Right = new Vector3f(0.0f, 1.0f, 0.0f).cross(new Vector3f(m_Direction)).normalize();
        m_Up = new Vector3f(m_Direction).cross(new Vector3f(m_Right));
        m_Front = new Vector3f(0.0f, 0.0f, -1.0f);
    }

    @Override
    public void Update()
    {

    }

    @Override
    public void Destroy()
    {

    }

    public Matrix4f Get3DView()
    {
        m_3D_View = new Matrix4f();

        Vector3f direction = new Vector3f(m_Direction);
        direction.x = (float) (cos(Math.toRadians(m_Yaw)) * cos(Math.toRadians(m_Pitch)));
        direction.y = (float) sin(Math.toRadians(m_Pitch));
        direction.z = (float) (sin(Math.toRadians(m_Yaw)) * cos(Math.toRadians(m_Pitch)));

        m_Direction = direction;

        m_Front = direction.normalize();

        m_3D_View = m_3D_View.lookAt(this.m_Parent.m_Transform.Position, new Vector3f(this.m_Parent.m_Transform.Position).add(m_Front), new Vector3f(0.0f, 1.0f, 0.0f));

        return m_3D_View;
    }

    public Matrix4f Get2DView()
    {
        m_2D_View = new Matrix4f();
        m_2D_View = m_2D_View.translate(new Vector3f(this.m_Parent.m_Transform.Position.x, this.m_Parent.m_Transform.Position.y, m_2D_Z));
        m_2D_View = new Matrix4f().ortho(-1.0f, 1.0f, -1.0f, 1.0f, 0.1f, 100.0f).mul(m_2D_View);
        return m_2D_View;
    }

    public Matrix4f GetOrthographicProjection()
    {
        return m_OrthoProjection;
    }
    public Matrix4f GetPerspectiveProjection()
    {
        return new Matrix4f().perspective((float) Math.toRadians(this.m_FOV), (float) Application.Window().GetWidth() /Application.Window().GetHeight(), 0.1f, 100.0f);
    }

    public void Set2DZ(float z)
    {
        m_2D_Z = z;
    }
    public void SetYaw(float yaw)
    {
        m_Yaw = yaw;
    }
    public void SetPitch(float pitch) { m_Pitch = pitch; }
    public void SetFOV(float fov) { m_FOV = fov; }

    public float Yaw() { return m_Yaw; }
    public float Pitch() { return m_Pitch; }
    public float FOV() { return m_FOV; }

    public Vector3f Direction() { return m_Direction; }
    public Vector3f Front() { return new Vector3f(m_Front); }
    public Vector3f Up() { return new Vector3f(m_Up); }
}
