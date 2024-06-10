package Application.Scene;

import Application.Application;
import Application.ApplicationSystem.Debug;
import Application.GameObject.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Scene
{
    private String m_Name;
    private List<GameObject> m_GameObjects = new ArrayList<GameObject>();

    public Scene(String name)
    {
        m_Name = name;
    }

    public void UpdateGameObjects()
    {
        Application.Renderer().RenderDebugToScreen();
        for(int i = 0; i < m_GameObjects.size(); i++)
        {
            GameObject gameObject = m_GameObjects.get(i);
            gameObject.Update();
            Application.CollisionManager().CheckForCollisionWithGameObject(gameObject);
            Application.Renderer().RenderGameObject(gameObject);
        }

    }
    public void Initialize_gameObjects()
    {
        for(int i = 0; i < m_GameObjects.size(); i++)
        {
            m_GameObjects.get(i).Initialize();
        }
    }

    public void Destroy()
    {
        for(int i = 0; i < m_GameObjects.size(); i++)
        {
            m_GameObjects.get(i).Destroy();
        }
    }

    public GameObject CreateGameObject()
    {
        GameObject newGameObject = new GameObject();
        AddGameObject(newGameObject);
        return newGameObject;
    }
    public void AddGameObject(GameObject new_gameObject)
    {
        m_GameObjects.add(new_gameObject);
    }
    public void RemoveGameObject(GameObject gameObject) { if(m_GameObjects.contains(gameObject)) {m_GameObjects.remove(gameObject); } }

    public String GetName() { return m_Name; }
    public List<GameObject> GetGameObjects() { return m_GameObjects; }
    public GameObject FindGameObjectWithTag(String tag)
    {
        for(GameObject gameObject : m_GameObjects)
        {
            if(gameObject.m_Tag.equals(tag))
                return gameObject;
        }

        Debug.Error("No gameObject found with tag '" + tag + "'");
        return null;
    }
}
