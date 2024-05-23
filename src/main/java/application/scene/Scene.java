package application.scene;

import application.Application;
import application.gameObject.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Scene
{
    private String m_name;
    private List<GameObject> m_gameObjects = new ArrayList<GameObject>();

    public Scene(String name)
    {
        m_name = name;
    }

    public void update_gameObjects()
    {
        Application.Renderer().render_debug_to_screen();
        for(int i = 0; i < m_gameObjects.size(); i++)
        {
            GameObject gameObject = m_gameObjects.get(i);
            gameObject.update();
            Application.CollisionManager().check_for_collision_with_gameObject(gameObject);
            Application.Renderer().render_gameObject(gameObject);
        }

    }
    public void initialize_gameObjects()
    {
        for(int i = 0; i < m_gameObjects.size(); i++)
        {
            m_gameObjects.get(i).initialize();
        }
    }

    public void destroy()
    {
        for(int i = 0; i < m_gameObjects.size(); i++)
        {
            m_gameObjects.get(i).destroy();
        }
    }

    public GameObject create_gameObject()
    {
        GameObject new_gameObject = new GameObject();
        add_gameObject(new_gameObject);
        return new_gameObject;
    }
    public void add_gameObject(GameObject new_gameObject)
    {
        m_gameObjects.add(new_gameObject);
    }
    public void remove_gameObject(GameObject gameObject) { if(m_gameObjects.contains(gameObject)) {m_gameObjects.remove(gameObject); } }

    public String get_name() { return m_name; }
    public List<GameObject> get_gameObjects() { return m_gameObjects; }
}
