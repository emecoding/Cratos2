package Application.GameObjectComponent;

import Application.Application;
import Application.ApplicationSystem.Debug;
import Application.Resource.Shader.Shader;
import Application.Resource.Texture;

import static Application.Resource.Shader.UniformConstants.FLIP_HORIZONTALLY;
import static Application.Resource.Shader.UniformConstants.FLIP_VERTICALLY;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.glBindTexture;

public class Sprite extends RenderComponent
{
    private int m_Texture = -1;

    private boolean m_FlipHorizontally = false;
    private boolean m_FlipVertically = false;

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

    public void FlipHorizontally() { m_FlipHorizontally = !m_FlipHorizontally; }
    public void FlipVertically() { m_FlipVertically = !m_FlipVertically; }

    public boolean IsFlippedHorizontally() { return m_FlipHorizontally; }
    public boolean IsFlippedVertically() { return m_FlipVertically; }


    @Override
    public void Render()
    {
        glBindTexture(GL_TEXTURE_2D, 0);

        if(m_Texture != -1)
        {
            glBindTexture(GL_TEXTURE_2D, m_Texture);
        }

        Shader shader = Application.ResourceManager().GetShader(m_ShaderName);

        if(shader.HasUniform(FLIP_HORIZONTALLY))
        {
            shader.SetUniform(FLIP_HORIZONTALLY, m_FlipHorizontally);
        }

        if(shader.HasUniform(FLIP_VERTICALLY))
        {
            shader.SetUniform(FLIP_VERTICALLY, m_FlipVertically);
        }

        Application.Renderer().RenderRectangle();
        glBindTexture(GL_TEXTURE_2D, 0);
    }

}
