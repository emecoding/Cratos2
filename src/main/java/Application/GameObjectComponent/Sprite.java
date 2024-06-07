package Application.GameObjectComponent;

import Application.Application;
import Application.ApplicationSystem.Debug;
import Application.Resource.Texture;

import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.glBindTexture;

public class Sprite extends RenderComponent
{
    private int m_Texture = -1;
    public Sprite(String shader_name, String texture_name)
    {
        SetShaderName(shader_name);

        if(Application.ResourceManager().HasTexture(texture_name))
        {
            m_Texture = Application.ResourceManager().GetTexture(texture_name).GetID();
        }
        else
        {
            Debug.Error("No such texture found as '" + texture_name + "'.");
        }

    }

    public Sprite(String shaderName)
    {
        SetShaderName(shaderName);
    }

    public void SetTexture(Texture texture)
    {
        m_Texture = texture.GetID();
    }

    public void SetTexture(int ID)
    {
        m_Texture = ID;
    }

    @Override
    public void Render()
    {
        glBindTexture(GL_TEXTURE_2D, 0);

        if(m_Texture != -1)
        {
            glBindTexture(GL_TEXTURE_2D, m_Texture);
        }

        Application.Renderer().RenderRectangle();
        glBindTexture(GL_TEXTURE_2D, 0);
    }

}
