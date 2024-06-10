package Application.GameObject;

import Application.GameObjectComponent.GameObjectComponent;
import Application.GameObjectComponent.RenderComponent;
import Application.GameObjectComponent.RenderObject;
import Application.GameObjectComponent.Transform;
import org.joml.Vector3f;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GameObject
{
    public Transform m_Transform;
    public List<GameObjectComponent> m_Components = new ArrayList<GameObjectComponent>();
    public String m_Tag = "";

    public GameObject()
    {
        m_Transform = new Transform(new Vector3f(0.0f));
    }

    public void Initialize()
    {
        for(int i = 0; i < m_Components.size(); i++)
        {
            m_Components.get(i).Initialize();
        }
    }

    public void Update()
    {
        for(int i = 0; i < m_Components.size(); i++)
        {
            if(!m_Components.get(i).Active)
                continue;
            m_Components.get(i).Update();
        }
    }

    public void Destroy()
    {
        for(int i = 0; i < m_Components.size(); i++)
        {
            m_Components.get(i).Destroy();
        }
    }



    public GameObjectComponent AddComponent(GameObjectComponent component)
    {
        if(IsRenderComponent(component.getClass().getAnnotatedSuperclass().toString()))
        {
            RenderObject m_render_object = (RenderObject) GetComponent(RenderObject.class);
            if(m_render_object == null)
                m_render_object = (RenderObject) AddComponent(new RenderObject());

            m_render_object.AddComponentToRender((RenderComponent) component);
        }

        component.m_Parent = this;
        component.OnAdd();
        m_Components.add(component);
        return component;
    }

    public GameObjectComponent GetComponent(Type component_type)
    {
        for(int i = 0; i < m_Components.size(); i++)
        {
            if(m_Components.get(i).getClass() == component_type)
                return m_Components.get(i);
        }

        return null;
    }

    private boolean IsRenderComponent(String component)
    {
        return component.contains("RenderComponent");
    }
}
