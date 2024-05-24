package Application.GameObjectComponent.Collider;

import Application.Application;
import org.joml.Matrix4f;

public class BoxCollider extends Collider
{
    @Override
    protected void RenderDebugRect()
    {
        Application.Renderer().RenderDebugBox(this);
    }

    public Matrix4f GetTransformMatrix()
    {
        return m_Parent.m_Transform.GetTransform3DMatrix();
    }

}
