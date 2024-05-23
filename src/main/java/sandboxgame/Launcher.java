package sandboxgame;

import application.Application;
import application.applicationSystem.window.Window;
import application.gameObject.GameObject;
import application.gameObjectComponent.Camera;
import application.gameObjectComponent.Collider.BoxCollider;
import application.gameObjectComponent.Collider.RectangleCollider;
import application.gameObjectComponent.Model.ModelLoader;
import application.gameObjectComponent.RenderObject;
import application.gameObjectComponent.RigidBody;
import application.gameObjectComponent.Sprite;
import application.scene.Scene;
import org.joml.Vector3f;

import static application.gameObjectComponent.Camera.DEFAULT_CAMERA_Z;
import static org.lwjgl.assimp.Assimp.aiProcess_JoinIdenticalVertices;
import static org.lwjgl.assimp.Assimp.aiProcess_Triangulate;
import static org.lwjgl.glfw.GLFW.*;

public class Launcher
{
    public static void main(String[] args)
    {
        Application.application_window = new Window("Sandbox game window", 800, 600);
        Application.application_window.set_window_hint(GLFW_RESIZABLE, GLFW_FALSE);
        Application.application_window.set_background_color(0.2f, 0.3f, 0.3f, 1.0f);

        Application.application_resource_manager.add_resource_folder_to_load("src/main/resources/textures");
        Application.application_resource_manager.add_resource_folder_to_load("src/main/resources/shaders"); //Shaders has to be stored like in this project

        Application.initialize();

        Application.application_window.center();

        Application.create_scene("Sandbox Scene");
        Scene sandbox_scene = Application.get_scene("Sandbox Scene");

        GameObject sandbox_camera = sandbox_scene.create_gameObject();
        sandbox_camera.transform.position.z = DEFAULT_CAMERA_Z;
        sandbox_camera.add_component(new Camera());

        GameObject sandbox_gameObject = sandbox_scene.create_gameObject();
        sandbox_gameObject.transform.position = new Vector3f(0.0f, 0.0f, 0.0f);
        sandbox_gameObject.transform.rotation = new Vector3f(180.0f, 0.0f, 180.0f);
        sandbox_gameObject.transform.scale = new Vector3f(0.5f, 0.5f, 0.5f);

        sandbox_gameObject.add_component(ModelLoader.load_model("src/main/resources/models/frank.obj", "default_model_shader", aiProcess_Triangulate | aiProcess_JoinIdenticalVertices));
        sandbox_gameObject.add_component(new rotator());
        RigidBody rb1 = (RigidBody) sandbox_gameObject.add_component(new RigidBody());
        rb1.gravity = 1000.0f;
        rb1.active = false;


        BoxCollider bxc = (BoxCollider) sandbox_gameObject.add_component(new BoxCollider());
        bxc.set_size(200.0f, 200.0f);
        bxc.render_debug_rect(true);


        GameObject sandbox_gameObject2 = sandbox_scene.create_gameObject();
        sandbox_gameObject2.transform.position = new Vector3f(100.0f, 500.0f, 1.0f);
        sandbox_gameObject2.transform.scale = new Vector3f(700.0f, 40.0f, 1.0f);
        sandbox_gameObject2.add_component(new RenderObject());

        sandbox_gameObject2.add_component(new Sprite("default_sprite_shader", "testTexture02"));
        RigidBody rb2 = (RigidBody) sandbox_gameObject2.add_component(new RigidBody());
        rb2.gravity = 0.0f;
        RectangleCollider rect_collider2 = (RectangleCollider) sandbox_gameObject2.add_component(new RectangleCollider());
        rect_collider2.render_debug_rect(true);


        Application.launch();
        Application.destroy();
    }
}
