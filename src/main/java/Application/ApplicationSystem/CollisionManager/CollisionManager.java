package Application.ApplicationSystem.CollisionManager;

import Application.Application;
import Application.ApplicationSystem.ApplicationSystem;
import Application.GameObject.GameObject;
import Application.GameObjectComponent.Collider.RectangleCollider;
import Application.GameObjectComponent.RigidBody;

import java.util.List;

public class CollisionManager implements ApplicationSystem
{
    private List<GameObject> m_EveryGameObject = null;
    @Override
    public void Initialize()
    {

    }

    public void ManageCollisions()
    {
        m_EveryGameObject = Application.SceneManager().GetEveryGameObjectFromScene();
    }

    public void CheckForCollisionWithGameObject(GameObject gameObject)
    {
        RectangleCollider rect_collider = (RectangleCollider) gameObject.GetComponent(RectangleCollider.class);
        //When implementing BoxCollider, just repeat everything with RectCollider
        if(rect_collider == null || !rect_collider.Active)
            return;

        RigidBody rb = (RigidBody) gameObject.GetComponent(RigidBody.class);
        if(rb == null || !rb.Active)
            return;

        for(int i = 0; i < m_EveryGameObject.size(); i++)
        {
            GameObject other = m_EveryGameObject.get(i);
            if(other == gameObject)
                continue;

            RectangleCollider other_rect_collider = (RectangleCollider) other.GetComponent(RectangleCollider.class);
            if(other_rect_collider == null || !other_rect_collider.Active)
                continue;

            RigidBody other_rb = (RigidBody) other.GetComponent(RigidBody.class);
            if(other_rb == null || !other_rb.Active)
                continue;

            CheckForRectCollision(rect_collider, other_rect_collider, rb, other_rb);
        }

    }

    private void CheckForRectCollision(RectangleCollider rect_collider, RectangleCollider other_rect_collider, RigidBody rb, RigidBody other_rb)
    {
        if(AABBCollision(rect_collider, other_rect_collider))
        {
            //TODO Figure out the proper math for collisions

            rect_collider.AddCollision(other_rect_collider.m_Parent);
        }
        else
        {
            rect_collider.RemoveCollision(other_rect_collider.m_Parent);
        }
    }

    private boolean AABBCollision(RectangleCollider rect_collider, RectangleCollider other_rect_collider)
    {
        float x0 = rect_collider.GetPosition2D().x();
        float y0 = rect_collider.GetPosition2D().y();
        float w0 = rect_collider.GetSize2D().x();
        float h0 = rect_collider.GetSize2D().y();

        float x1 = other_rect_collider.GetPosition2D().x();
        float y1 = other_rect_collider.GetPosition2D().y();
        float w1 = other_rect_collider.GetSize2D().x();
        float h1 = other_rect_collider.GetSize2D().y();

        return (x0 < x1 + w1 &&
                x0 + w0 > x1 &&
                y0 < y1 + h1 &&
                y0 + h0 > y1);

    }

    @Override
    public void Destroy()
    {

    }
}
