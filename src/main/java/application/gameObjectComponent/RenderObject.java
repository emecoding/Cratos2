package application.gameObjectComponent;

import java.util.ArrayList;
import java.util.List;

public class RenderObject extends GameObjectComponent
{

    private List<RenderComponent> m_components_to_render = new ArrayList<>();
    private int m_current_component_to_render = 0;

    public RenderObject()
    {

    }
    @Override
    public void initialize()
    {

    }
    @Override
    public void update()
    {

    }
    @Override
    public void destroy()
    {

    }

    public void add_component_to_render(RenderComponent render_component)
    {
        m_components_to_render.add(render_component);
    }

    public void render_current_component()
    {
        if(m_components_to_render.get(m_current_component_to_render).active)
            m_components_to_render.get(m_current_component_to_render).render();
        m_current_component_to_render++;
    }

    public boolean has_components_to_render()
    {
        if(m_current_component_to_render >= m_components_to_render.size())
        {
            m_current_component_to_render = 0;
            return false;
        }
        return true;
    }

    public String get_current_shader_name() { return m_components_to_render.get(m_current_component_to_render).get_shader_name(); }


}
