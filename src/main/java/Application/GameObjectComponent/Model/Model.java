package Application.GameObjectComponent.Model;

import Application.GameObjectComponent.RenderComponent;
import Application.Resource.Material;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;

public class Model extends RenderComponent
{
    private List<Mesh> m_Meshes = new ArrayList<>();
    private int m_RenderMode;
    public Model(String shader)
    {
        SetShaderName(shader);

        m_RenderMode = GL_TRIANGLES;
        //TODO Model animations - dumb every .obj into one file with java and loop over them frame by frame.
    }

    @Override
    public void Render()
    {
        for(int i = 0; i < m_Meshes.size(); i++)
        {
            m_Meshes.get(i).Render(GetShaderName(), m_RenderMode);
        }
    }

    public void AddMesh(Mesh mesh) { m_Meshes.add(mesh); }
    public void SetRenderMode(int mode) { m_RenderMode = mode; }
    public void SetDiffuseColorForEveryMesh(float r, float g, float b, float a)
    {
        for(int i = 0; i < m_Meshes.size(); i++)
        {
            m_Meshes.get(i).GetMaterial().SetDiffuseColor(r, g, b, a);
        }
    }
}
