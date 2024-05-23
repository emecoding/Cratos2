package Application.ApplicationSystem.SceneManager;

import Application.ApplicationSystem.ApplicationSystem;
import Application.ApplicationSystem.Debug;
import Application.GameObject.GameObject;
import Application.Scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class SceneManager implements ApplicationSystem
{
    private int m_CurrentSceneIndex = 0;
    private List<Scene> m_Scenes = new ArrayList<Scene>();

    @Override
    public void Initialize()
    {

    }
    @Override
    public void Destroy()
    {
        for (int i = 0; i < m_Scenes.size(); i++) {
            m_Scenes.get(i).Destroy();
        }
    }

    public void UpdateCurrentSceneGameObjects()
    {
        m_Scenes.get(m_CurrentSceneIndex).UpdateGameObjects();
    }
    public void InitializeCurrentSceneGameObjects()
    {
        if(m_Scenes.size() == 0)
        {
            Debug.Error("No scenes created!");
            return;
        }
        m_Scenes.get(m_CurrentSceneIndex).Initialize_gameObjects();
    }

    public Scene CreateScene(String name)
    {
        Scene new_scene = new Scene(name);
        AddScene(new_scene);
        return new_scene;
    }
    public void AddScene(Scene new_scene)
    {
        m_Scenes.add(new_scene);
    }
    public Scene GetScene(String name)
    {
        for(int i = 0; i < m_Scenes.size(); i++)
        {
            if(m_Scenes.get(i).GetName().equals(name))
                return m_Scenes.get(i);
        }

        return null;
    }

    public List<GameObject> GetEveryGameObjectFromScene() { return m_Scenes.get(m_CurrentSceneIndex).GetGameObjects(); }
}
