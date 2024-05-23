package Application.GameObjectComponent.Collider;

import Application.GameObject.GameObject;
import Application.GameObjectComponent.GameObjectComponent;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Collider extends GameObjectComponent
{
    protected Vector3f m_Size = new Vector3f(1.0f);
    protected Vector3f m_PositionOffset = new Vector3f(0.0f);
    protected boolean m_RenderDebugRect = false;
    protected List<GameObject> m_Collisions = new ArrayList<>();


    public void SetSize(float w, float h, float d) { m_Size = new Vector3f(w, h, d); }
    public void SetSize(float w, float h) { m_Size = new Vector3f(w, h, 1.0f); }
    public void SetPositionOffset(float x, float y, float z) { m_PositionOffset = new Vector3f(x, y, z); }
    public void SetPositionOffset(float x, float y) { m_PositionOffset = new Vector3f(x, y, 0.0f); }
    public void RenderDebugRect(boolean render) { m_RenderDebugRect = render; }


    @Override
    public void OnAdd()
    {
        m_Size.x = m_Parent.m_Transform.Scale.x;
        m_Size.y = m_Parent.m_Transform.Scale.y;
    }

    @Override
    public void Update()
    {
        if(m_RenderDebugRect)
        {
            RenderDebugRect();
        }
    }

    protected void RenderDebugRect()
    {

    }

    public void AddCollision(GameObject other)
    {
        if(!m_Collisions.contains(other))
            m_Collisions.add(other);
    }
    public void RemoveCollision(GameObject other)
    {
        m_Collisions.remove(other);
    }

    public Vector2f GetSize2D() { return new Vector2f(m_Size.x, m_Size.y); }
    public Vector3f GetSize3D() { return m_Size; }

    public Vector2f GetPosition2D() {return new Vector2f(m_Parent.m_Transform.Position.x - m_PositionOffset.x, m_Parent.m_Transform.Position.y - m_PositionOffset.y);}
    public Vector3f GetPosition3D() { return new Vector3f(m_Parent.m_Transform.Position.x - m_PositionOffset.x, m_Parent.m_Transform.Position.y - m_PositionOffset.y, m_Parent.m_Transform.Position.z - m_PositionOffset.z); }

    public List<GameObject> Collisions() { return m_Collisions; }


}
