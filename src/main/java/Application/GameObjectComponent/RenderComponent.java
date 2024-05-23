package Application.GameObjectComponent;

public class RenderComponent extends GameObjectComponent
{
    protected String m_ShaderName;

    protected void SetShaderName(String name)
    {
        m_ShaderName = name;
    }
    public void Render()
    {

    }
    public String GetShaderName() { return m_ShaderName; }
}
