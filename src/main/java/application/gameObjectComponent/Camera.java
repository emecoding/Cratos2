package application.gameObjectComponent;

import application.Application;
import org.joml.Matrix4f;

public class Camera extends GameObjectComponent
{
    private Matrix4f m_view;
    private Matrix4f m_ortho_projection;

    @Override
    public void initialize()
    {
        Application.application_renderer.add_camera(this);
        m_view = new Matrix4f();
        m_ortho_projection = new Matrix4f().ortho(0.0f, Application.application_window.get_width(), Application.application_window.get_height(), 0.0f, -1.0f, 1.0f);
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
        m_view.translate(parent.transform.position);
        m_view = new Matrix4f().ortho(-1.0f, 1.0f, -1.0f, 1.0f, 0.1f, 100.0f).mul(m_view);
        return m_view;
    }
    public Matrix4f get_orthographic_projection()
    {
        return m_ortho_projection;
    }
}
