package Application.GameObjectComponent.Model;

import Application.GameObjectComponent.RenderComponent;

import java.util.ArrayList;
import java.util.List;

public class Model extends RenderComponent
{
    private List<Mesh> m_Meshes = new ArrayList<>();

    public Model(String shader)
    {
        SetShaderName(shader);
        //TODO Model animations - dumb every .obj into one file with java and loop over them frame by frame.
    }

    @Override
    public void Render()
    {
        for(int i = 0; i < m_Meshes.size(); i++)
        {
            m_Meshes.get(i).Render(GetShaderName());
        }
    }

    public void AddMesh(Mesh mesh) { m_Meshes.add(mesh); }
}
