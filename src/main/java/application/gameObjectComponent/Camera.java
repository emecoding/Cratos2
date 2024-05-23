package application.gameObjectComponent;

import application.Application;
import application.Utils.Time;
import org.joml.Matrix4f;

public class Camera extends GameObjectComponent
{
    private Matrix4f m_view;
    private Matrix4f m_ortho_projection;
    public static final float DEFAULT_CAMERA_Z = -2.0f;

    @Override
    public void initialize()
    {
        Application.Renderer().add_camera(this);
        m_view = new Matrix4f();
        m_ortho_projection = new Matrix4f().ortho(0.0f, Application.Window().get_width(), Application.Window().get_height(), 0.0f, -1.0f, 1.0f);
    }

    @Override
    public void update()
    {
    }

    @Override
    public void destroy()
    {

    }

    public Matrix4f get_view()
    {
        m_view = new Matrix4f();
        m_view = m_view.translate(parent.transform.position);
        m_view = new Matrix4f().ortho(-1.0f, 1.0f, -1.0f, 1.0f, 0.1f, 100.0f).mul(m_view);
        return m_view;
    }
    public Matrix4f get_orthographic_projection()
    {
        return m_ortho_projection;
    }
    public Matrix4f get_perspective_projection()
    {
        return new Matrix4f().perspective((float) Math.toRadians(90.0f), (float) Application.Window().get_width() /Application.Window().get_height(), 0.1f, 100.0f);
    }
}
