package application.gameObjectComponent.Collider;


import application.Application;
import org.joml.Vector4f;

public class RectangleCollider extends Collider //RectangleCollider is 2D collider, where BoxCollider will be 3D collider
{
    private Vector4f get_debug_rect()
    {
        return new Vector4f(parent.transform.position.x - m_position_offset.x, parent.transform.position.y - m_position_offset.y, m_size.x, m_size.y);
    }

    @Override
    protected void render_debug_rect()
    {
        Application.Renderer().render_debug_rectangle(get_debug_rect());
    }
}
