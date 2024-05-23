package Application.GameObjectComponent;

import java.util.ArrayList;
import java.util.List;

public class RenderObject extends GameObjectComponent
{

    private List<RenderComponent> m_ComponentsToRender = new ArrayList<>();
    private int m_CurrentComponentToRender = 0;

    public RenderObject()
    {

    }
    @Override
    public void Initialize()
    {

    }
    @Override
    public void Update()
    {

    }
    @Override
    public void Destroy()
    {

    }

    public void AddComponentToRender(RenderComponent render_component)
    {
        m_ComponentsToRender.add(render_component);
    }

    public void RenderCurrentComponent()
    {
        if(m_ComponentsToRender.get(m_CurrentComponentToRender).Active)
            m_ComponentsToRender.get(m_CurrentComponentToRender).Render();
        m_CurrentComponentToRender++;
    }

    public boolean HasComponentsToRender()
    {
        if(m_CurrentComponentToRender >= m_ComponentsToRender.size())
        {
            m_CurrentComponentToRender = 0;
            return false;
        }
        return true;
    }

    public String GetCurrentShaderName() { return m_ComponentsToRender.get(m_CurrentComponentToRender).GetShaderName(); }


}
