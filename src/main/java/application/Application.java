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

    public static Window application_window = null;
    public static Renderer application_renderer = null;
    public static final ResourceManager application_resource_manager = new ResourceManager();
    public static final Input application_input = new Input();
    private static final SceneManager application_scene_manager = new SceneManager();
    public static final CollisionManager application_collision_manager = new CollisionManager();


    public static void initialize()
    {
        application_window.initialize();
        application_renderer.initialize();
        application_input.initialize();
    }

    public static void launch()
    {
        application_scene_manager.initialize_current_scene_gameObjects();

        while(!application_window.window_should_close())
        {
            Time.update_time();
            application_window.clear_window();
            application_collision_manager.manage_collisions();
            application_scene_manager.update_current_scene_gameObjects();
            application_window.swap_buffers();
            application_window.poll_events();
        }
    }

    public static void destroy()
    {
        application_scene_manager.destroy();
        application_renderer.destroy();
        application_resource_manager.destroy();
        application_window.destroy();
    }

    public static void terminate_safely()
    {
        application_window.set_window_should_close(true);
    }

    public static Scene create_scene(String name)
    {
        return application_scene_manager.create_scene(name);
    }
    public static Scene get_scene(String name) { return application_scene_manager.get_scene(name); }
    public static void add_scene(Scene new_scene)
    {
        application_scene_manager.add_scene(new_scene);
    }
    public static List<GameObject> get_every_gameObject_from_scene() { return application_scene_manager.get_every_gameObject_from_scene(); }
}
