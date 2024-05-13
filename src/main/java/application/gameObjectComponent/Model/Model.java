package application.gameObjectComponent.Model;

import application.gameObjectComponent.RenderComponent;

import java.util.ArrayList;
import java.util.List;

public class Model extends RenderComponent
{
    private List<Mesh> m_meshes = new ArrayList<>();

    public Model(String shader)
    {
        set_shader_name(shader);
        //TODO Model animations - dumb every .obj into one file with java and loop over them frame by frame.
    }

    public void render()
    {
        for(int i = 0; i < m_meshes.size(); i++)
        {
            m_meshes.get(i).render(get_shader_name());
        }
    }

    public void add_mesh(Mesh mesh) { m_meshes.add(mesh); }
}
