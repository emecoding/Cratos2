package Application;


import Application.Utils.Time;
import Application.ApplicationSystem.CollisionManager.CollisionManager;
import Application.ApplicationSystem.Input.Input;
import Application.ApplicationSystem.Renderer.Renderer;
import Application.ApplicationSystem.ResourceManager.ResourceManager;
import Application.ApplicationSystem.SceneManager.SceneManager;
import Application.ApplicationSystem.Window.Window;


public class Application
{

    private static Window ApplicationWindow = null;
    private static Renderer ApplicationRenderer = new Renderer();
    private static final ResourceManager ApplicationResourceManager = new ResourceManager();
    private static final Input ApplicationInput = new Input();
    private static final SceneManager ApplicationSceneManager = new SceneManager();
    private static final CollisionManager ApplicationCollisionManager = new CollisionManager();


    public static void Initialize()
    {
        ApplicationWindow.Initialize();
        ApplicationResourceManager.Initialize();
        ApplicationRenderer.Initialize();
        ApplicationInput.Initialize();
    }

    public static void Launch()
    {
        ApplicationSceneManager.InitializeCurrentSceneGameObjects();

        while(!ApplicationWindow.WindowShouldClose())
        {
            Time.update_time();
            ApplicationWindow.ClearWindow();
            ApplicationCollisionManager.ManageCollisions();
            ApplicationSceneManager.UpdateCurrentSceneGameObjects();
            ApplicationWindow.SwapBuffers();
            ApplicationWindow.PollEvents();
        }
    }

    public static void Destroy()
    {
        ApplicationSceneManager.Destroy();
        ApplicationRenderer.Destroy();
        ApplicationResourceManager.Destroy();
        ApplicationWindow.Destroy();
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

    public static void Terminate()
    {
        ApplicationWindow.SetWindowShouldClose(true);
    }

}
