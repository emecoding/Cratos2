package application.gameObjectComponent;

import application.Application;
import application.applicationSystem.Debug;
import application.resource.Texture;

public class Sprite extends RenderComponent
{
    private Texture m_texture;
    public Sprite(String shader_name, String texture_name)
    {
        set_shader_name(shader_name);

        m_texture = Application.ResourceManager().get_texture(texture_name);
        if(m_texture == null)
        {
            Debug.error("No such texture found as '" + texture_name + "'.");
        }
    }

    @Override
    public void render()
    {
        if(m_texture != null)
        {
            m_texture.use();
        }

        Application.Renderer().render_rectangle();
    }

}
