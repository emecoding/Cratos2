package application.gameObjectComponent.Collider;

import application.Application;
import application.gameObject.GameObject;
import application.gameObjectComponent.GameObjectComponent;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class Collider extends GameObjectComponent
{
    protected Vector3f m_size = new Vector3f(1.0f);
    protected Vector3f m_position_offset = new Vector3f(0.0f);
    protected boolean render_debug_rect = false;
    protected List<GameObject> m_collisions = new ArrayList<>();


    public void set_size(float w, float h, float d) { m_size = new Vector3f(w, h, d); }
    public void set_size(float w, float h) { m_size = new Vector3f(w, h, 1.0f); }
    public void set_position_offset(float x, float y, float z) { m_position_offset = new Vector3f(x, y, z); }
    public void set_position_offset(float x, float y) { m_position_offset = new Vector3f(x, y, 0.0f); }
    public void render_debug_rect(boolean render) { render_debug_rect = render; }


    @Override
    public void on_add()
    {
        m_size.x = parent.transform.scale.x;
        m_size.y = parent.transform.scale.y;
    }

    @Override
    public void update()
    {
        if(render_debug_rect)
        {
            render_debug_rect();
        }
    }

    protected void render_debug_rect()
    {

    }

    public void add_collision(GameObject other)
    {
        if(!m_collisions.contains(other))
            m_collisions.add(other);
    }
    public void remove_collision(GameObject other)
    {
        if(m_collisions.contains(other))
            m_collisions.remove(other);
    }

    public Vector2f get_size_2D() { return new Vector2f(m_size.x, m_size.y); }
    public Vector3f get_size_3D() { return m_size; }

    public Vector2f get_position_2D() {return new Vector2f(parent.transform.position.x - m_position_offset.x, parent.transform.position.y - m_position_offset.y);}
    public Vector3f get_position_3D() { return new Vector3f(parent.transform.position.x - m_position_offset.x, parent.transform.position.y - m_position_offset.y, parent.transform.position.z - m_position_offset.z); }

    public List<GameObject> collisions() { return m_collisions; }


}
