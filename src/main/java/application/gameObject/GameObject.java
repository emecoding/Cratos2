package application.gameObject;

import application.applicationSystem.Debug;
import application.gameObjectComponent.GameObjectComponent;
import application.gameObjectComponent.RenderComponent;
import application.gameObjectComponent.RenderObject;
import application.gameObjectComponent.Transform;
import org.joml.Vector3f;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GameObject
{
    public Transform transform;
    public List<GameObjectComponent> m_components = new ArrayList<GameObjectComponent>();

    public GameObject()
    {
        transform = new Transform(new Vector3f(0.0f));
    }

    public void initialize()
    {
        for(int i = 0; i < m_components.size(); i++)
        {
            m_components.get(i).initialize();
        }
    }

    public void update()
    {
        for(int i = 0; i < m_components.size(); i++)
        {
            if(!m_components.get(i).active)
                continue;
            m_components.get(i).update();
        }
    }

    public void destroy()
    {
        for(int i = 0; i < m_components.size(); i++)
        {
            m_components.get(i).destroy();
        }
    }



    public GameObjectComponent add_component(GameObjectComponent component)
    {
        if(is_render_component(component.getClass().getAnnotatedSuperclass().toString()))
        {
            RenderObject m_render_object = (RenderObject) get_component(RenderObject.class);
            if(m_render_object == null)
                m_render_object = (RenderObject) add_component(new RenderObject());

            m_render_object.add_component_to_render((RenderComponent) component);
        }

        component.parent = this;
        component.on_add();
        m_components.add(component);
        return component;
    }

    public GameObjectComponent get_component(Type component_type)
    {
        for(int i = 0; i < m_components.size(); i++)
        {
            if(m_components.get(i).getClass() == component_type)
                return m_components.get(i);
        }

        return null;
    }

    private boolean is_render_component(String component)
    {
        return component.contains("RenderComponent");
    }
}
