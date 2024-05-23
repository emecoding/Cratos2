package Application.ApplicationSystem.Render;

import Application.Application;
import Application.Utils.Utils;
import Application.ApplicationSystem.ApplicationSystem;
import Application.ApplicationSystem.Debug;
import Application.GameObject.GameObject;
import Application.GameObjectComponent.Camera;
import Application.GameObjectComponent.RenderObject;
import Application.Resource.Shader.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Application.Resource.Shader.UniformConstants.*;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;

public class Renderer implements ApplicationSystem
{

    private List<Camera> m_Cameras = new ArrayList<>();
    private Camera m_CurrentCamera = null;

    private List<Vector4f> m_DebugRectangles = new ArrayList<>();

    private int m_DebugVAO, m_DebugVBO;
    private int m_RectangleVerticesStride = 4 * Float.BYTES;
    private final float[] RECTANGLE_VERTICES = new float[]
            {
                    0.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 0.0f,

                    0.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 0.0f
            };

    private int m_SpriteVAO, m_SpriteVBO;

    @Override
    public void Initialize()
    {
        glEnable(GL_DEPTH_TEST);

        SetUpDebugRendering();
        SetUpSpriteRendering();
        SetUpModelRendering();
    }

    @Override
    public void Destroy()
    {

    }

    private void UpdateCurrentCamera()
    {
        if(m_Cameras.size() == 0)
            return;
        m_CurrentCamera = m_Cameras.get(0);
    }
    public Camera GetCurrentCamera() { return m_CurrentCamera; }
    public void AddCamera(Camera camera_component)
    {
        m_Cameras.add(camera_component);
    }
    public void RenderGameObject(GameObject gameObject)
    {
        glCullFace(GL_BACK);
        RenderObject render_object_component = (RenderObject) gameObject.GetComponent(RenderObject.class);
        if(render_object_component == null)
            return;


        while(render_object_component.HasComponentsToRender())
        {
            StopRenderingCurrentShader();
            SetUpCurrentShaderForRendering(gameObject, render_object_component.GetCurrentShaderName());
            render_object_component.RenderCurrentComponent();
        }
    }
    public void RenderRectangle()
    {
        glBindVertexArray(m_SpriteVAO);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);
    }
    public void RenderDebugRectangle(Vector4f rectangle)
    {
        m_DebugRectangles.add(rectangle);
    }
    public void RenderDebugToScreen()
    {
        glBindVertexArray(m_DebugVAO);
        for(int i = 0; i < m_DebugRectangles.size(); i++)
        {
            Vector4f debug_rectangle = m_DebugRectangles.get(i);

            Matrix4f transform_2d = new Matrix4f();
            transform_2d = transform_2d.translate(new Vector3f(debug_rectangle.x(), debug_rectangle.y(), 0.0f));
            transform_2d = transform_2d.scale(new Vector3f(debug_rectangle.z(), debug_rectangle.w(), 1.0f));

            SetUpCurrentShaderForRendering("default_debug_shader", transform_2d, null);
            glDrawArrays(GL_LINES, 0, 6);
        }

        m_DebugRectangles.clear();

    }
    private void SetUpDebugRendering()
    {
        FloatBuffer debug_vbo_data = Utils.StoreDataInFloatBuffer(RECTANGLE_VERTICES);
        m_DebugVAO = Utils.CreateVAO();
        m_DebugVBO = Utils.CreateBuffer(GL_ARRAY_BUFFER, debug_vbo_data, GL_STATIC_DRAW);

        glBindVertexArray(m_DebugVAO);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, m_RectangleVerticesStride, 0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(0);

        Shader debug_shader = Application.ResourceManager().GetShader("default_debug_shader");
        debug_shader.CreateDefault2DUniforms();
    }
    private void SetUpSpriteRendering()
    {
        FloatBuffer sprite_vbo_data = Utils.StoreDataInFloatBuffer(RECTANGLE_VERTICES);
        m_SpriteVAO = Utils.CreateVAO();
        m_SpriteVBO = Utils.CreateBuffer(GL_ARRAY_BUFFER, sprite_vbo_data, GL_STATIC_DRAW);

        glBindVertexArray(m_SpriteVAO);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, m_RectangleVerticesStride, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, m_RectangleVerticesStride, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);

        Shader sprite_shader = Application.ResourceManager().GetShader("default_sprite_shader");
        sprite_shader.CreateDefault2DUniforms();
    }
    private void SetUpModelRendering()
    {
        Shader default_model_shader = Application.ResourceManager().GetShader("default_model_shader");
        default_model_shader.CreateDefault3DUniforms();
    }
    private void SetUpCurrentShaderForRendering(String shader_name, Matrix4f transform_2D_matrix, Matrix4f transform_3D_matrix)
    {
        UpdateCurrentCamera();

        Shader current_shader = Application.ResourceManager().GetShader(shader_name);
        if(current_shader == null)
        {
            Debug.Error("No such shader found as '" + shader_name + "'.");
            return;
        }
        current_shader.Bind();

        Map<String, Integer> set_shader_uniforms = current_shader.GetSetUniforms();
        for(String uniform_name : set_shader_uniforms.keySet())
        {
            switch(uniform_name)
            {
                case GAME_OBJECT_2D_TRANSFORM ->
                {
                    current_shader.SetUniform(GAME_OBJECT_2D_TRANSFORM, transform_2D_matrix);
                }

                case GAME_OBJECT_3D_TRANSFORM ->
                {
                    current_shader.SetUniform(GAME_OBJECT_3D_TRANSFORM, transform_3D_matrix);
                }

                case CAMERA_VIEW ->
                {
                    current_shader.SetUniform(CAMERA_VIEW, m_CurrentCamera.GetView());
                }

                case ORTHOGRAPHIC_PROJECTION ->
                {
                    current_shader.SetUniform(ORTHOGRAPHIC_PROJECTION, m_CurrentCamera.GetOrthographicProjection());
                }

                case PERSPECTIVE_PROJECTION ->
                {
                    current_shader.SetUniform(PERSPECTIVE_PROJECTION, m_CurrentCamera.GetPerspectiveProjection());
                }
            }
        }

    }
    private void SetUpCurrentShaderForRendering(GameObject gameObject, String shader_name)
    {
        SetUpCurrentShaderForRendering(shader_name, gameObject.m_Transform.get_transform_2D_matrix(), gameObject.m_Transform.get_transform_3D_matrix());
    }
    private void StopRenderingCurrentShader()
    {
        glUseProgram(0);
    }


}
