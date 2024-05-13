package application.resource.shader;

import application.applicationSystem.resourceManager.ResourceManager;
import application.gameObjectComponent.Model.Material;
import application.resource.Resource;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

import static application.resource.shader.UniformConstants.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader extends Resource
{
    private int m_program_id;
    private int m_vertex_shader_id, m_fragment_shader_id;
    private Map<String, Integer> m_uniforms;

    public Shader(String name, String vertex_path, String fragment_path)
    {
        set_name(name);

        m_program_id = glCreateProgram();
        if(m_program_id == 0)
            throw new RuntimeException("Failed to create shader...");

        m_uniforms = new HashMap<>();

        try
        {
            create_vertex_shader(vertex_path);
            create_fragment_shader(fragment_path);
            link();
            glUseProgram(0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void create_vertex_shader(String vertex_path) throws Exception
    {
        m_vertex_shader_id = create_shader(ResourceManager.load_resource_from_path(vertex_path), GL_VERTEX_SHADER);
    }
    private void create_fragment_shader(String fragment_path) throws Exception
    {
        m_fragment_shader_id = create_shader(ResourceManager.load_resource_from_path(fragment_path), GL_FRAGMENT_SHADER);
    }
    private int create_shader(String shader_code, int shader_type) throws Exception
    {
        int shader_id = glCreateShader(shader_type);
        if(shader_id == 0)
            throw new Exception("Error creating shader " + get_name() + ". Shader code: " + shader_code);

        glShaderSource(shader_id, shader_code);
        glCompileShader(shader_id);

        if(glGetShaderi(shader_id, GL_COMPILE_STATUS) == 0)
            throw new Exception("Error compiling shader " + get_name() + ". Code: " + (shader_type == GL_VERTEX_SHADER ? "VERTEX" : "FRAGMENT") + ", Info: " + glGetShaderInfoLog(shader_id, 1024));

        glAttachShader(m_program_id, shader_id);
        return shader_id;
    }
    private void link() throws Exception
    {
        glLinkProgram(m_program_id);

        if(glGetProgrami(m_program_id, GL_LINK_STATUS) == 0)
            throw new Exception("Error linking shader " + get_name() + ": " + glGetProgramInfoLog(m_program_id, 1024));

        glDetachShader(m_program_id, m_vertex_shader_id);
        glDetachShader(m_program_id, m_fragment_shader_id);

        glValidateProgram(m_program_id);
        if(glGetProgrami(m_program_id, GL_VALIDATE_STATUS) == 0)
            throw new Exception("Error validating shader" + get_name() + ": " + glGetProgramInfoLog(m_program_id, 1024));
    }

    public void create_uniform(String uniform)
    {
        int uniform_location = glGetUniformLocation(m_program_id, uniform);
        m_uniforms.put(uniform, uniform_location);
    }
    public void set_uniform(String uniform, Matrix4f value)
    {
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            glUniformMatrix4fv(m_uniforms.get(uniform), false, value.get(stack.mallocFloat(16)));
        }
    }
    public void set_uniform(String uniform, Vector4f value)
    {
        glUniform4f(m_uniforms.get(uniform), value.x(), value.y(), value.z(), value.w());
    }
    public void set_uniform(String uniform, int value)
    {
        glUniform1i(m_uniforms.get(uniform), value);
    }
    public void create_default_2D_uniforms()
    {
        create_uniform(GAME_OBJECT_2D_TRANSFORM);
        create_uniform(CAMERA_VIEW);
        create_uniform(ORTHOGRAPHIC_PROJECTION);
    }
    public void create_default_3D_uniforms()
    {
        create_uniform(GAME_OBJECT_3D_TRANSFORM);
        create_uniform(CAMERA_VIEW);
        create_uniform(PERSPECTIVE_PROJECTION);
        Material.CREATE_DEFAULT_UNIFORMS(this);
    }
    public Map<String, Integer> get_set_uniforms() { return m_uniforms; }
    public void bind()
    {
        glUseProgram(m_program_id);
    }
    @Override
    public void destroy()
    {
        glUseProgram(0);
        glDeleteProgram(m_program_id);
    }
}
