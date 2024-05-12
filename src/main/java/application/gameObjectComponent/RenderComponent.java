package application.gameObjectComponent;

public class RenderComponent extends GameObjectComponent
{
    protected String m_shader_name;

    protected void set_shader_name(String name)
    {
        m_shader_name = name;
    }
    public void render()
    {

    }
    public String get_shader_name() { return m_shader_name; }
}
