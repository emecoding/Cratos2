package Application.GameObjectComponent;

import Application.Application;
import Application.ApplicationSystem.Debug;
import Application.Resource.Texture;

public class Sprite extends RenderComponent
{
    private Texture m_Texture;
    public Sprite(String shader_name, String texture_name)
    {
        SetShaderName(shader_name);

        m_Texture = Application.ResourceManager().GetTexture(texture_name);
        if(m_Texture == null)
        {
            Debug.Error("No such texture found as '" + texture_name + "'.");
        }
    }

    @Override
    public void Render()
    {
        if(m_Texture != null)
        {
            m_Texture.Use();
        }

        Application.Renderer().RenderRectangle();
    }

}
