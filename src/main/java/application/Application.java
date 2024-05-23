package application;


import application.Utils.Time;
import application.applicationSystem.CollisionManager.CollisionManager;
import application.applicationSystem.Input.Input;
import application.applicationSystem.renderer.Renderer;
import application.applicationSystem.resourceManager.ResourceManager;
import application.applicationSystem.sceneManager.SceneManager;
import application.applicationSystem.window.Window;
import application.gameObject.GameObject;
import application.scene.Scene;

import java.util.List;


public class Application
{

    private static Window ApplicationWindow = null;
    private static Renderer ApplicationRenderer = new Renderer();
    private static final ResourceManager ApplicationResourceManager = new ResourceManager();
    private static final Input ApplicationInput = new Input();
    private static final SceneManager ApplicationSceneManager = new SceneManager();
    private static final CollisionManager ApplicationCollisionManager = new CollisionManager();


    public static void initialize()
    {
        ApplicationWindow.initialize();
        ApplicationResourceManager.initialize();
        ApplicationRenderer.initialize();
        ApplicationInput.initialize();
    }

    public static void launch()
    {
        ApplicationSceneManager.initialize_current_scene_gameObjects();

        while(!ApplicationWindow.window_should_close())
        {
            Time.update_time();
            ApplicationWindow.clear_window();
            ApplicationCollisionManager.manage_collisions();
            ApplicationSceneManager.update_current_scene_gameObjects();
            ApplicationWindow.swap_buffers();
            ApplicationWindow.poll_events();
        }
    }

    public static void destroy()
    {
        ApplicationSceneManager.destroy();
        ApplicationRenderer.destroy();
        ApplicationResourceManager.destroy();
        ApplicationWindow.destroy();
    }

    public static Window Window() { return ApplicationWindow; }
    public static Renderer Renderer() { return ApplicationRenderer; }
    public static ResourceManager ResourceManager() { return ApplicationResourceManager; }
    public static Input Input() { return ApplicationInput; }
    public static SceneManager SceneManager() { return ApplicationSceneManager; }
    public static CollisionManager CollisionManager() { return ApplicationCollisionManager; }

    public static void CreateWindow(String title, int width, int height)
    {
        ApplicationWindow = new Window(title, width, height);
    }

    public static void terminate_safely()
    {
        ApplicationWindow.set_window_should_close(true);
    }

    public static Scene create_scene(String name)
    {
        return ApplicationSceneManager.create_scene(name);
    }
    public static Scene get_scene(String name) { return ApplicationSceneManager.get_scene(name); }
    public static void add_scene(Scene new_scene)
    {
        ApplicationSceneManager.add_scene(new_scene);
    }
    public static List<GameObject> get_every_gameObject_from_scene() { return ApplicationSceneManager.get_every_gameObject_from_scene(); }

}
