package Application.Resource.Shader;

import Application.ApplicationSystem.ResourceManager.ResourceManager;
import Application.GameObjectComponent.Model.Material;
import Application.Resource.Resource;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

import static Application.Resource.Shader.UniformConstants.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader extends Resource
{
    private int m_ProgramID;
    private int m_VertexShaderID, m_FragmentShaderID;
    private Map<String, Integer> m_Uniforms;

    public Shader(String name, String vertex_path, String fragment_path)
    {
        SetName(name);

        m_ProgramID = glCreateProgram();
        if(m_ProgramID == 0)
            throw new RuntimeException("Failed to create shader...");

        m_Uniforms = new HashMap<>();

        try
        {
            CreateVertexShader(vertex_path);
            CreateFragmentShader(fragment_path);
            Link();
            glUseProgram(0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void CreateVertexShader(String vertex_path) throws Exception
    {
        m_VertexShaderID = CreateShader(ResourceManager.ReadFile(vertex_path), GL_VERTEX_SHADER);
    }
    private void CreateFragmentShader(String fragment_path) throws Exception
    {
        m_FragmentShaderID = CreateShader(ResourceManager.ReadFile(fragment_path), GL_FRAGMENT_SHADER);
    }
    private int CreateShader(String shader_code, int shader_type) throws Exception
    {
        int shader_id = glCreateShader(shader_type);
        if(shader_id == 0)
            throw new Exception("Error creating shader " + GetName() + ". Shader code: " + shader_code);

        glShaderSource(shader_id, shader_code);
        glCompileShader(shader_id);

        if(glGetShaderi(shader_id, GL_COMPILE_STATUS) == 0)
            throw new Exception("Error compiling shader " + GetName() + ". Code: " + (shader_type == GL_VERTEX_SHADER ? "VERTEX" : "FRAGMENT") + ", Info: " + glGetShaderInfoLog(shader_id, 1024));

        glAttachShader(m_ProgramID, shader_id);
        return shader_id;
    }
    private void Link() throws Exception
    {
        glLinkProgram(m_ProgramID);

        if(glGetProgrami(m_ProgramID, GL_LINK_STATUS) == 0)
            throw new Exception("Error linking shader " + GetName() + ": " + glGetProgramInfoLog(m_ProgramID, 1024));

        glDetachShader(m_ProgramID, m_VertexShaderID);
        glDetachShader(m_ProgramID, m_FragmentShaderID);

        glValidateProgram(m_ProgramID);
        if(glGetProgrami(m_ProgramID, GL_VALIDATE_STATUS) == 0)
            throw new Exception("Error validating shader" + GetName() + ": " + glGetProgramInfoLog(m_ProgramID, 1024));
    }

    public void CreateUniform(String uniform)
    {
        int uniform_location = glGetUniformLocation(m_ProgramID, uniform);
        m_Uniforms.put(uniform, uniform_location);
    }
    public void SetUniform(String uniform, Matrix4f value)
    {
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            glUniformMatrix4fv(m_Uniforms.get(uniform), false, value.get(stack.mallocFloat(16)));
        }
    }
    public void SetUniform(String uniform, Vector4f value)
    {
        glUniform4f(m_Uniforms.get(uniform), value.x(), value.y(), value.z(), value.w());
    }
    public void SetUniform(String uniform, int value)
    {
        glUniform1i(m_Uniforms.get(uniform), value);
    }
    public void CreateDefault2DUniforms()
    {
        CreateUniform(GAME_OBJECT_2D_TRANSFORM);
        CreateUniform(CAMERA_VIEW);
        CreateUniform(ORTHOGRAPHIC_PROJECTION);
    }
    public void CreateDefault3DUniforms()
    {
        CreateUniform(GAME_OBJECT_3D_TRANSFORM);
        CreateUniform(CAMERA_VIEW);
        CreateUniform(PERSPECTIVE_PROJECTION);
        Material.CREATE_DEFAULT_UNIFORMS(this);
    }
    public Map<String, Integer> GetSetUniforms() { return m_Uniforms; }
    public void Bind()
    {
        glUseProgram(m_ProgramID);
    }
    @Override
    public void Destroy()
    {
        glUseProgram(0);
        glDeleteProgram(m_ProgramID);
    }
}
