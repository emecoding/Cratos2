package application.applicationSystem.CollisionManager;

import application.Application;
import application.applicationSystem.ApplicationSystem;
import application.gameObject.GameObject;
import application.gameObjectComponent.Collider.RectangleCollider;
import application.gameObjectComponent.RigidBody;

import java.util.List;

public class CollisionManager implements ApplicationSystem
{
    private List<GameObject> m_every_gameObject = null;
    @Override
    public void initialize()
    {

    }

    public void manage_collisions()
    {
        m_every_gameObject = Application.get_every_gameObject_from_scene();
    }

    public void check_for_collision_with_gameObject(GameObject gameObject)
    {
        RectangleCollider rect_collider = (RectangleCollider) gameObject.get_component(RectangleCollider.class);
        //When implementing BoxCollider, just repeat everything with RectCollider
        if(rect_collider == null || !rect_collider.active)
            return;

        RigidBody rb = (RigidBody) gameObject.get_component(RigidBody.class);
        if(rb == null || !rb.active)
            return;

        for(int i = 0; i < m_every_gameObject.size(); i++)
        {
            GameObject other = m_every_gameObject.get(i);
            if(other == gameObject)
                continue;

            RectangleCollider other_rect_collider = (RectangleCollider) other.get_component(RectangleCollider.class);
            if(other_rect_collider == null || !other_rect_collider.active)
                continue;

            RigidBody other_rb = (RigidBody) other.get_component(RigidBody.class);
            if(other_rb == null || !other_rb.active)
                continue;

            check_for_rect_collision(rect_collider, other_rect_collider, rb, other_rb);
        }

    }

    private void check_for_rect_collision(RectangleCollider rect_collider, RectangleCollider other_rect_collider, RigidBody rb, RigidBody other_rb)
    {
        if(AABB_collision(rect_collider, other_rect_collider))
        {
            //TODO Figure out the proper math for collisions

            rect_collider.add_collision(other_rect_collider.parent);
        }
        else
        {
            rect_collider.remove_collision(other_rect_collider.parent);
        }
    }

    private boolean AABB_collision(RectangleCollider rect_collider, RectangleCollider other_rect_collider)
    {
        float x0 = rect_collider.get_position_2D().x();
        float y0 = rect_collider.get_position_2D().y();
        float w0 = rect_collider.get_size_2D().x();
        float h0 = rect_collider.get_size_2D().y();

        float x1 = other_rect_collider.get_position_2D().x();
        float y1 = other_rect_collider.get_position_2D().y();
        float w1 = other_rect_collider.get_size_2D().x();
        float h1 = other_rect_collider.get_size_2D().y();

        return (x0 < x1 + w1 &&
                x0 + w0 > x1 &&
                y0 < y1 + h1 &&
                y0 + h0 > y1);

    }

    @Override
    public void destroy()
    {

    }
}
