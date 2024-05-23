package Application.GameObjectComponent.Collider;


import Application.Application;
import org.joml.Vector4f;

public class RectangleCollider extends Collider //RectangleCollider is 2D collider, where BoxCollider will be 3D collider
{
    private Vector4f GetDebugRect()
    {
        return new Vector4f(m_Parent.m_Transform.Position.x - m_PositionOffset.x, m_Parent.m_Transform.Position.y - m_PositionOffset.y, m_Size.x, m_Size.y);
    }

    @Override
    protected void RenderDebugRect()
    {
        Application.Renderer().RenderDebugRectangle(GetDebugRect());
    }
}
