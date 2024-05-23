package SandboxGame;

import Application.Application;
import Application.GameObject.GameObject;
import Application.GameObjectComponent.Camera;
import Application.GameObjectComponent.Collider.BoxCollider;
import Application.GameObjectComponent.Collider.RectangleCollider;
import Application.GameObjectComponent.Model.ModelLoader;
import Application.GameObjectComponent.RenderObject;
import Application.GameObjectComponent.RigidBody;
import Application.GameObjectComponent.Sprite;
import Application.Scene.Scene;
import org.joml.Vector3f;

import static Application.GameObjectComponent.Camera.DEFAULT_CAMERA_Z;
import static org.lwjgl.assimp.Assimp.aiProcess_JoinIdenticalVertices;
import static org.lwjgl.assimp.Assimp.aiProcess_Triangulate;
import static org.lwjgl.glfw.GLFW.*;

public class Launcher
{
    public static void main(String[] args)
    {
        Application.CreateWindow("Sandbox game", 800, 600);
        Application.Window().SetWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        Application.Window().SetBackgroundColor(0.2f, 0.3f, 0.3f, 1.0f);

        Application.ResourceManager().AddResourceFolderToLoad("src/main/resources/textures");
        Application.ResourceManager().AddResourceFolderToLoad("src/main/resources/shaders"); //Shaders has to be stored like in this project

        Application.Initialize();

        Application.Window().Center();

        Application.SceneManager().CreateScene("Sandbox Scene");
        Scene sandbox_scene = Application.SceneManager().GetScene("Sandbox Scene");

        GameObject sandbox_camera = sandbox_scene.CreateGameObject();
        sandbox_camera.m_Transform.Position.z = DEFAULT_CAMERA_Z;
        sandbox_camera.AddComponent(new Camera());

        GameObject sandbox_gameObject = sandbox_scene.CreateGameObject();
        sandbox_gameObject.m_Transform.Position = new Vector3f(0.0f, 0.0f, 0.0f);
        sandbox_gameObject.m_Transform.Rotation = new Vector3f(180.0f, 0.0f, 180.0f);
        sandbox_gameObject.m_Transform.Scale = new Vector3f(0.5f, 0.5f, 0.5f);

        sandbox_gameObject.AddComponent(ModelLoader.LoadModel("src/main/resources/models/frank.obj", "default_model_shader", aiProcess_Triangulate | aiProcess_JoinIdenticalVertices));
        sandbox_gameObject.AddComponent(new rotator());
        RigidBody rb1 = (RigidBody) sandbox_gameObject.AddComponent(new RigidBody());
        rb1.Gravity = 1000.0f;
        rb1.Active = false;


        BoxCollider bxc = (BoxCollider) sandbox_gameObject.AddComponent(new BoxCollider());
        bxc.SetSize(200.0f, 200.0f);
        bxc.RenderDebugRect(true);


        GameObject sandbox_gameObject2 = sandbox_scene.CreateGameObject();
        sandbox_gameObject2.m_Transform.Position = new Vector3f(100.0f, 500.0f, 1.0f);
        sandbox_gameObject2.m_Transform.Scale = new Vector3f(700.0f, 40.0f, 1.0f);
        sandbox_gameObject2.AddComponent(new RenderObject());

        sandbox_gameObject2.AddComponent(new Sprite("default_sprite_shader", "testTexture02"));
        RigidBody rb2 = (RigidBody) sandbox_gameObject2.AddComponent(new RigidBody());
        rb2.Gravity = 0.0f;
        RectangleCollider rect_collider2 = (RectangleCollider) sandbox_gameObject2.AddComponent(new RectangleCollider());
        rect_collider2.RenderDebugRect(true);


        Application.Launch();
        Application.Destroy();
    }
}
