package SandboxGame;

import Application.Application;
import Application.GameObject.GameObject;
import Application.GameObjectComponent.*;
import Application.GameObjectComponent.Collider.BoxCollider;
import Application.GameObjectComponent.Collider.RectangleCollider;
import Application.Resource.SpriteSheet;
import Application.Scene.Scene;
import org.joml.Vector3f;

import static Application.GameObjectComponent.Camera.DEFAULT_CAMERA_3D_Z;
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
        Application.ResourceManager().AddResourceFolderToLoad("src/main/resources/shaders");//Shaders has to be stored like in this project
        Application.ResourceManager().AddResourceFolderToLoad("src/main/resources/models");
        Application.ResourceManager().SetAssimpFlags(aiProcess_Triangulate | aiProcess_JoinIdenticalVertices);


        Application.Initialize();

        Application.Window().Center();

        Application.Input().SetCursorPositionOnWindow(400, 300);
        Application.Input().SetCursorInputMode(GLFW_CURSOR_DISABLED);

        Application.SceneManager().CreateScene("Sandbox Scene");
        Scene sandbox_scene = Application.SceneManager().GetScene("Sandbox Scene");

        GameObject sandbox_camera = sandbox_scene.CreateGameObject();
        sandbox_camera.m_Transform.Position.z = DEFAULT_CAMERA_3D_Z;
        sandbox_camera.AddComponent(new Camera());
        CameraMovement camMovement = (CameraMovement) sandbox_camera.AddComponent(new CameraMovement());
        camMovement.SetSpeed(1000.0f);

        GameObject sandbox_gameObject = sandbox_scene.CreateGameObject();
        sandbox_gameObject.m_Transform.Position = new Vector3f(0.0f, 0.0f, 0.0f);
        sandbox_gameObject.m_Transform.Rotation = new Vector3f(180.0f, 0.0f, 180.0f);
        sandbox_gameObject.m_Transform.Scale = new Vector3f(0.5f, 0.5f, 0.5f);

        sandbox_gameObject.AddComponent(new ModelRenderer(Application.ResourceManager().GetModel("frank")));
        sandbox_gameObject.AddComponent(new rotator());
        RigidBody rb1 = (RigidBody) sandbox_gameObject.AddComponent(new RigidBody());
        rb1.Gravity = 1000.0f;
        rb1.Active = false;

        BoxCollider bxc = (BoxCollider) sandbox_gameObject.AddComponent(new BoxCollider());
        bxc.SetSize(200.0f, 200.0f);
        bxc.RenderDebugRect(true);

        GameObject sandbox_gameObject2 = sandbox_scene.CreateGameObject();
        sandbox_gameObject2.m_Transform.Position = new Vector3f(100.0f, 500.0f, 1.0f);
        sandbox_gameObject2.m_Transform.Scale = new Vector3f(32, 32, 1.0f);
        sandbox_gameObject2.AddComponent(new RenderObject());
        sandbox_gameObject2.AddComponent(new Sprite("default_sprite_shader"));

        SpriteSheet testSpriteSheet = SpriteSheet.LoadSpriteSheet("src/main/resources/textures/testTexture04.png", 32, 32);

        Animator2D animator2D = (Animator2D) sandbox_gameObject2.AddComponent(new Animator2D("default_sprite_shader", testSpriteSheet));
        animator2D.SetAnimation("test", 0, 22);
        animator2D.SetCurrentAnimation("test");
        animator2D.SetFrameTime(0.1);
        animator2D.SetFrameRate(700.0);

        RigidBody rb2 = (RigidBody) sandbox_gameObject2.AddComponent(new RigidBody());
        rb2.Gravity = 0.0f;
        RectangleCollider rect_collider2 = (RectangleCollider) sandbox_gameObject2.AddComponent(new RectangleCollider());
        rect_collider2.RenderDebugRect(true);

        Application.Launch();
        Application.Destroy();
    }
}
