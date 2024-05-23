package Application.GameObjectComponent;

import Application.Application;
import org.joml.Matrix4f;

public class Camera extends GameObjectComponent
{
    private Matrix4f m_View;
    private Matrix4f m_OrthoProjection;
    public static final float DEFAULT_CAMERA_Z = -2.0f;

    @Override
    public void Initialize()
    {
        Application.Renderer().AddCamera(this);
        m_View = new Matrix4f();
        m_OrthoProjection = new Matrix4f().ortho(0.0f, Application.Window().GetWidth(), Application.Window().GetHeight(), 0.0f, -1.0f, 1.0f);
    }

    @Override
    public void Update()
    {
    }

    @Override
    public void Destroy()
    {

    }

    public Matrix4f GetView()
    {
        m_View = new Matrix4f();
        m_View = m_View.translate(m_Parent.m_Transform.Position);
        m_View = new Matrix4f().ortho(-1.0f, 1.0f, -1.0f, 1.0f, 0.1f, 100.0f).mul(m_View);
        return m_View;
    }
    public Matrix4f GetOrthographicProjection()
    {
        return m_OrthoProjection;
    }
    public Matrix4f GetPerspectiveProjection()
    {
        return new Matrix4f().perspective((float) Math.toRadians(90.0f), (float) Application.Window().GetWidth() /Application.Window().GetHeight(), 0.1f, 100.0f);
    }
}
