package application.applicationSystem.renderer;

import application.Application;
import application.Utils.Utils;
import application.applicationSystem.ApplicationSystem;
import application.applicationSystem.Debug;
import application.gameObject.GameObject;
import application.gameObjectComponent.Camera;
import application.gameObjectComponent.RenderObject;
import application.resource.shader.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static application.resource.shader.UniformConstants.*;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;

public class Renderer implements ApplicationSystem
{

    private List<Camera> m_cameras = new ArrayList<>();
    private Camera m_current_camera = null;

    private List<Vector4f> m_debug_rectangles = new ArrayList<>();

    private int m_debug_VAO, m_debug_VBO;
    private int m_rectangle_vertices_stride = 4 * Float.BYTES;
    private final float[] RECTANGLE_VERTICES = new float[]
            {
                    0.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 0.0f,

                    0.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 0.0f
            };

    private int m_sprite_VAO, m_sprite_VBO;

    @Override
    public void initialize()
    {
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        set_up_debug_rendering();
        set_up_sprite_rendering();
        set_up_model_rendering();

        //TODO Text rendering - possibly to another file
    }

    @Override
    public void destroy()
    {

    }

    private void update_current_camera()
    {
        if(m_cameras.size() == 0)
            return;
        m_current_camera = m_cameras.get(0); //later on check which cameras are on and somehow sort them into tärkeysjärjestys.
    }
    public void add_camera(Camera camera_component)
    {
        m_cameras.add(camera_component);
    }
    public void render_gameObject(GameObject gameObject)
    {
        glCullFace(GL_BACK);
        RenderObject render_object_component = (RenderObject) gameObject.get_component(RenderObject.class);
        if(render_object_component == null)
            return;


        while(render_object_component.has_components_to_render())
        {
            stop_rendering_current_shader();
            set_up_current_shader_for_rendering(gameObject, render_object_component.get_current_shader_name());
            render_object_component.render_current_component();
        }
    }
    public void render_rectangle()
    {
        glBindVertexArray(m_sprite_VAO);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);
    }
    public void render_debug_rectangle(Vector4f rectangle)
    {
        m_debug_rectangles.add(rectangle);
    }
    public void render_debug_to_screen()
    {
        glBindVertexArray(m_debug_VAO);
        for(int i = 0; i < m_debug_rectangles.size(); i++)
        {
            Vector4f debug_rectangle = m_debug_rectangles.get(i);

            Matrix4f transform_2d = new Matrix4f();
            transform_2d = transform_2d.translate(new Vector3f(debug_rectangle.x(), debug_rectangle.y(), 0.0f));
            transform_2d = transform_2d.scale(new Vector3f(debug_rectangle.z(), debug_rectangle.w(), 1.0f));

            set_up_current_shader_for_rendering("DEBUG", transform_2d, null);
            glDrawArrays(GL_LINES, 0, 6);
        }

        m_debug_rectangles.clear();

    }
    private void set_up_debug_rendering()
    {
        FloatBuffer debug_vbo_data = Utils.store_data_in_float_buffer(RECTANGLE_VERTICES);
        m_debug_VAO = Utils.create_VAO();
        m_debug_VBO = Utils.create_buffer(GL_ARRAY_BUFFER, debug_vbo_data, GL_STATIC_DRAW);

        glBindVertexArray(m_debug_VAO);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, m_rectangle_vertices_stride, 0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(0);

        Shader debug_shader = Application.application_resource_manager.add_shader("DEBUG", "/shaders/default_debug_shader/default_debug_shader.vs", "/shaders/default_debug_shader/default_debug_shader.fs");
        debug_shader.create_default_2D_uniforms();
    }
    private void set_up_sprite_rendering()
    {
        FloatBuffer sprite_vbo_data = Utils.store_data_in_float_buffer(RECTANGLE_VERTICES);
        m_sprite_VAO = Utils.create_VAO();
        m_sprite_VBO = Utils.create_buffer(GL_ARRAY_BUFFER, sprite_vbo_data, GL_STATIC_DRAW);

        glBindVertexArray(m_sprite_VAO);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, m_rectangle_vertices_stride, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, m_rectangle_vertices_stride, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);

        Shader sprite_shader = Application.application_resource_manager.add_shader("DEFAULT_SPRITE", "/shaders/default_sprite_shader/default_sprite_shader.vs", "/shaders/default_sprite_shader/default_sprite_shader.fs");
        sprite_shader.create_default_2D_uniforms();
    }
    private void set_up_model_rendering()
    {
        Shader default_model_shader = Application.application_resource_manager.add_shader("DEFAULT_MODEL", "/shaders/default_model_shader/default_model_shader.vs", "/shaders/default_model_shader/default_model_shader.fs");
        default_model_shader.create_default_3D_uniforms();
    }
    private void set_up_current_shader_for_rendering(String shader_name, Matrix4f transform_2D_matrix, Matrix4f transform_3D_matrix)
    {
        update_current_camera();

        Shader current_shader = Application.application_resource_manager.get_shader(shader_name);
        if(current_shader == null)
        {
            Debug.error("No such shader found as '" + shader_name + "'.");
            return;
        }
        current_shader.bind();

        Map<String, Integer> set_shader_uniforms = current_shader.get_set_uniforms();
        for(String uniform_name : set_shader_uniforms.keySet())
        {
            switch(uniform_name)
            {
                case GAME_OBJECT_2D_TRANSFORM ->
                {
                    current_shader.set_uniform(GAME_OBJECT_2D_TRANSFORM, transform_2D_matrix);
                }

                case GAME_OBJECT_3D_TRANSFORM ->
                {
                    current_shader.set_uniform(GAME_OBJECT_3D_TRANSFORM, transform_3D_matrix);
                }

                case CAMERA_VIEW ->
                {
                    current_shader.set_uniform(CAMERA_VIEW, m_current_camera.get_view());
                }

                case ORTHOGRAPHIC_PROJECTION ->
                {
                    current_shader.set_uniform(ORTHOGRAPHIC_PROJECTION, m_current_camera.get_orthographic_projection());
                }
            }
        }

    }
    private void set_up_current_shader_for_rendering(GameObject gameObject, String shader_name)
    {
        set_up_current_shader_for_rendering(shader_name, gameObject.transform.get_transform_2D_matrix(), gameObject.transform.get_transform_3D_matrix());
    }
    private void stop_rendering_current_shader()
    {
        glUseProgram(0);
    }


}
