package Application.GameObjectComponent.Model;

import Application.Utils.Utils;
import Application.ApplicationSystem.Debug;

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
    private float[] m_Vertices, m_TexCoords;
    private int[] m_Indices;
    private int m_NumVertices;
    private int m_VertexLength = 5;//position + texture coords

    private int VAO, VBO, EBO;

    private Material m_material;

    public Mesh(float[] vertices, float[] tex_coords, int[] indices)
    {
        m_Vertices = vertices;
        m_TexCoords = tex_coords;
        m_Indices = indices;

        InitializeMesh();
    }

    public void Render(String shder_name)
    {
        glBindVertexArray(VAO);
        m_material.Use(shder_name);
        glDrawElements(GL_TRIANGLES, m_NumVertices, GL_UNSIGNED_INT, 0);
    }

    public void SetMaterial(Material material) { m_material = material; }

    private void InitializeMesh()
    {
        float[] VBO_data = CombineVBOData(); //pos has to be 3, tex_coord 2
        if(VBO_data == null)
            return;

        m_NumVertices = VBO_data.length;

        FloatBuffer VBO_data_buffer = Utils.StoreDataInFloatBuffer(VBO_data);
        VAO = Utils.CreateVAO();
        VBO = Utils.CreateBuffer(GL_ARRAY_BUFFER, VBO_data_buffer, GL_STATIC_DRAW);

        glBindVertexArray(VAO);

        int stride = m_VertexLength * Float.BYTES; //apos(3) + tex_coor(2) * Float.BYTES

        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        IntBuffer indices_buffer = Utils.StoreDataInIntBuffer(m_Indices);
        EBO = Utils.CreateBuffer(GL_ELEMENT_ARRAY_BUFFER, indices_buffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private float[] CombineVBOData()
    {

        if(m_Vertices.length/3 != m_TexCoords.length/2)
        {
            Debug.Error("VBO data is not possible to combine - vertex pos and tex coord data is different length.");
            return null;
        }

        float[] VBO_data = new float[m_Vertices.length/3*m_VertexLength];

        int i = 0;
        int a_pos = 0;
        int tex_coord = 0;
        while(i < VBO_data.length)
        {

            for(int j = 0; j < 3; j++)
            {
                VBO_data[i] = m_Vertices[a_pos];
                i++;
                a_pos++;
            }

            for(int j = 0; j < 2; j++)
            {
                VBO_data[i] = m_TexCoords[tex_coord];
                i++;
                tex_coord++;
            }
        }

        return VBO_data;
    }


}
