package Application.Resource;


import Application.Application;
import Application.ApplicationSystem.Debug;
import Application.Resource.Shader.Shader;
import org.joml.Vector4f;

import java.io.File;

import static Application.Resource.Shader.UniformConstants.DIFFUSE_COLOR;
import static Application.Resource.Shader.UniformConstants.HAS_TEXTURE;

public class Material extends Resource
{
    private Vector4f m_DiffuseColor = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
    private String m_TexturePath;
    private Texture m_Texture;
    private Shader m_Shader = null;

    public Material()
    {

    }

    public void SetMaterialName(String name)
    {
        SetName(name);
    }

    public void SetDiffuseColor(float x, float y, float z, float w)
    {
        m_DiffuseColor.x = x;
        m_DiffuseColor.y = y;
        m_DiffuseColor.z = z;
        m_DiffuseColor.w = w;
    }

    public void SetTexturePath(String path)
    {
        m_TexturePath = path;
        String texture_name = GenerateNameForTexture();
        m_Texture = Application.ResourceManager().GetTexture(texture_name);
        if(m_Texture == null)
            CreateTextureForMaterial(texture_name);
    }

    public void Use(String shader_name)
    {
        if(m_Shader == null)
            m_Shader = Application.ResourceManager().GetShader(shader_name);
        m_Shader.SetUniform(DIFFUSE_COLOR, m_DiffuseColor);

        if(m_Texture != null)
        {
            m_Shader.SetUniform(HAS_TEXTURE, 1);
            m_Texture.Use();
        }
        else
        {
            m_Shader.SetUniform(HAS_TEXTURE, 0);
        }

    }

    private String GenerateNameForTexture()
    {
        String file_name = new File(m_TexturePath).getName();
        file_name = file_name.split("\\.")[0];
        file_name = file_name.toUpperCase();
        return file_name;
    }
    private void CreateTextureForMaterial(String texture_name)
    {
        Debug.Log("Didn't find texture '" + texture_name + "'. Creating one.");
        Texture texture = Texture.CreateTexture3D(texture_name, m_TexturePath);
        Application.ResourceManager().AddTexture(texture);
        m_Texture = texture;
    }

    public static void CREATE_DEFAULT_UNIFORMS(Shader shader)
    {
        shader.CreateUniform(DIFFUSE_COLOR);
        shader.CreateUniform(HAS_TEXTURE);
    }


}
