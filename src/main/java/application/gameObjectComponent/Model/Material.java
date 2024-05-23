package application.gameObjectComponent.Model;


import application.Application;
import application.applicationSystem.Debug;
import application.resource.Texture;
import application.resource.shader.Shader;
import org.joml.Vector4f;

import java.io.File;

import static application.resource.shader.UniformConstants.DIFFUSE_COLOR;
import static application.resource.shader.UniformConstants.HAS_TEXTURE;

public class Material
{
    private Vector4f m_diffuse_color = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
    private String m_texture_path;
    private Texture m_texture;
    private Shader m_shader = null;

    public Material()
    {

    }

    public void set_diffuse_color(float x, float y, float z, float w)
    {
        m_diffuse_color.x = x;
        m_diffuse_color.y = y;
        m_diffuse_color.z = z;
        m_diffuse_color.w = w;
    }

    public void set_texture_path(String path)
    {
        m_texture_path = path;
        String texture_name = generate_name_for_texture();
        m_texture = Application.ResourceManager().get_texture(texture_name);
        if(m_texture == null)
            create_texture_for_material(texture_name);
    }

    public void use(String shader_name)
    {
        if(m_shader == null)
            m_shader = Application.ResourceManager().get_shader(shader_name);
        m_shader.set_uniform(DIFFUSE_COLOR, m_diffuse_color);
        if(m_texture != null)
        {
            m_shader.set_uniform(HAS_TEXTURE, 1);
            m_texture.use();
        }

    }

    private String generate_name_for_texture()
    {
        String file_name = new File(m_texture_path).getName();
        file_name = file_name.split("\\.")[0];
        file_name = file_name.toUpperCase();
        return file_name;
    }
    private void create_texture_for_material(String texture_name)
    {
        Debug.log("Didn't find texture '" + texture_name + "'. Creating one.");
        Texture texture = Texture.create_texture_3D(texture_name, m_texture_path);
        Application.ResourceManager().add_texture(texture);
        m_texture = texture;
    }

    public static void CREATE_DEFAULT_UNIFORMS(Shader shader)
    {
        shader.create_uniform(DIFFUSE_COLOR);
        shader.create_uniform(HAS_TEXTURE);
    }


}
