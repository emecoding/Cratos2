package application.gameObjectComponent.Model;

import application.Utils.Utils;
import application.applicationSystem.Debug;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL15C.glBufferData;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Mesh
{
    private float[] m_vertices, m_tex_coords;
    private int[] m_indices;
    private int m_num_vertices;
    private int m_vertex_length = 5;

    private int VAO, VBO, EBO;

    private Material m_material;

    public Mesh(float[] vertices, float[] tex_coords, int[] indices)
    {
        m_vertices = vertices;
        m_tex_coords = tex_coords;
        m_indices = indices;

        initialize_mesh();
    }

    public void render(String shder_name)
    {
        glBindVertexArray(VAO);
        m_material.use(shder_name);
        glDrawElements(GL_TRIANGLES, m_num_vertices, GL_UNSIGNED_INT, 0);
    }

    public void set_material(Material material) { m_material = material; }

    private void initialize_mesh()
    {
        float[] VBO_data = combine_VBO_data(); //pos has to be 3, tex_coord 2
        if(VBO_data == null)
            return;

        m_num_vertices = VBO_data.length;

        FloatBuffer VBO_data_buffer = Utils.store_data_in_float_buffer(VBO_data);
        VAO = Utils.create_VAO();
        VBO = Utils.create_buffer(GL_ARRAY_BUFFER, VBO_data_buffer, GL_STATIC_DRAW);

        glBindVertexArray(VAO);

        int stride = m_vertex_length * Float.BYTES; //apos(3) + tex_coor(2) * Float.BYTES

        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        IntBuffer indices_buffer = Utils.store_data_in_int_buffer(m_indices);
        EBO = Utils.create_buffer(GL_ELEMENT_ARRAY_BUFFER, indices_buffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private float[] combine_VBO_data()
    {

        if(m_vertices.length/3 != m_tex_coords.length/2)
        {
            Debug.error("VBO data is not possible to combine - vertex pos and tex coord data is different length.");
            return null;
        }

        float[] VBO_data = new float[m_vertices.length/3*m_vertex_length];

        int i = 0;
        int a_pos = 0;
        int tex_coord = 0;
        while(i < VBO_data.length)
        {

            for(int j = 0; j < 3; j++)
            {
                VBO_data[i] = m_vertices[a_pos];
                i++;
                a_pos++;
            }

            for(int j = 0; j < 2; j++)
            {
                VBO_data[i] = m_tex_coords[tex_coord];
                i++;
                tex_coord++;
            }
        }

        return VBO_data;
    }


}
