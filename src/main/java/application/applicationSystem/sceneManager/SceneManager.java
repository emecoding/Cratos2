package application.applicationSystem.sceneManager;

import application.applicationSystem.ApplicationSystem;
import application.gameObject.GameObject;
import application.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class SceneManager implements ApplicationSystem
{
    private int m_current_scene_index = 0;
    private List<Scene> m_scenes = new ArrayList<Scene>();

    @Override
    public void initialize()
    {

    }
    @Override
    public void destroy()
    {
        for (int i = 0; i < m_scenes.size(); i++) {
            m_scenes.get(i).destroy();
        }
    }

    public void update_current_scene_gameObjects()
    {
        m_scenes.get(m_current_scene_index).update_gameObjects();
    }
    public void initialize_current_scene_gameObjects()
    {
        m_scenes.get(m_current_scene_index).initialize_gameObjects();
    }

    public Scene create_scene(String name)
    {
        Scene new_scene = new Scene(name);
        add_scene(new_scene);
        return new_scene;
    }
    public void add_scene(Scene new_scene)
    {
        m_scenes.add(new_scene);
    }
    public Scene get_scene(String name)
    {
        for(int i = 0; i < m_scenes.size(); i++)
        {
            if(m_scenes.get(i).get_name().equals(name))
                return m_scenes.get(i);
        }

        return null;
    }

    public List<GameObject> get_every_gameObject_from_scene() { return m_scenes.get(m_current_scene_index).get_gameObjects(); }
}
