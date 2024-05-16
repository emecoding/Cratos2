# Cratos2

Cratos2 is a game engine library written for java. It is also built on top of LWJGL 3 and meant to make creating games easier and faster!

Currently the only documentation for the project is this fine file you are reading right now. The docs will (hopefully :) be updated as new features are developed.

Please use the version you can find from the Release-V0.1, since there will be the latest well working version of the game engine.

# Download And Install

# Sandbox Game
To see an example game/program, please head to src/main/java/sandboxgame/Launcher.java.

# Application

Any of the public variables of the Application.java class can be accessed from anywhere since the whole Application.java class is static.

<br>

    Application.application_window
is the main instance of the window for the program. To see more about the Window class, check out #Window.

<br>

    Application.application_renderer
is the main instance of the renderer for the program. Currently you have to create the renderer by yourself:

    Application.application_renderer = new Renderer()
    This is fixed in task 2!
Currently you do not need to touch the renderer in order for the program to execute. Later on the renderer class will be made so that the user can create their own renderer expanding on the Renderer.java class.

<br>

    Application.resource_manager
is the main instance of the resource manager for the program. To see more about the Resource Manager class, check out #ResourceManager.

<br>

    Application.application_input
is the main instance of the input class for the program. To see more about the Input class, check out #Input

<br>

    public static void initialize()
Initialize the application. This function also initializes GLFW and OpenGL, so be sure to call this function before doing anything else. Application.application_window and Application.application_renderer has to be created before the initialize function is called.

<br>

    public static void launch()
Launch the application after the set up has been done. This starts the main loop for the application. Check out Application.terminate_safely() for safe termination of the program.

<br>

    public static void terminate_safely()
This function terminates the program safely. The function just calls the Application.application_window.set_window_should_close(true) function, which closes the main loop of the application.

<br>

    public static void destroy()
Destroys the application and closes all the resources safely.



- application_collision_manager (no need to care about that for the user)

# Debug

Make your code more debuggable with the Debug class.

<br>

    Debug.error(String message)
Print out an error to the console.

<br>

    Debug.log(String message)
Print out an message to the console.



# Window

    public void set_window_hint(int hint, int value)
Add a window hint before the intialization of the main window. Check out the window hints from [here.](https://www.glfw.org/docs/3.3/window_guide.html#window_hints). Window resizing is not supported currently.

<br>

    public void set_monitor(long monitor)
Set the current monitor for the window.

<br>

    public void center()
Centers the window to the set monitor.

<br>

    public void set_window_should_close(boolean value)
The whole application main loop is being controlled by this argument. If this is set to true, the main loop stops and closes the program.

<br>

    public void set_buffer_bits_to_clear(int buffer_bits_to_clear)
Add an buffer bit to clear with the glClear() command. The default buffer bits set are GL_COLOR_BUFFER_BIT and GL_DEPTH_BUFFER_BIT. Check out the the buffer bits from [here.](https://registry.khronos.org/OpenGL-Refpages/gl4/html/glClear.xhtml)

<br>

    public void set_background_color(float r, float g, float b, float a)
Set the window background color. Skyboxes are not supported right now.

<br>

    public int get_width()
    public int get_height()
Get the size of the window.

<br>

    public long get_window_long()
Get the LWJGL window id.

# ResourceManager

Resource manager holds the shaders and the textures of the program. Every resource will have a name, and can be accessed with that.
Currently you have to add all the resources you want to use manually, but later on the resource manager will load them automatically!

<br>

    public void add_shader(String name, String vertex_path, String fragment_path)
Add a shader to the resource manager.

<br>

    public void add_texture(String name, String path)
Add a texture with a name and a path to the resource manager.

<br>

    public void add_texture(Texture texture)
Add an already created texture to the resource manager.
Textures can be created with 

    Texture.create_texture_3D(String name, String path)
    or
    Texture.create_texture_2D(String name, String path)


<br>

    public Shader get_shader(String name)
Get a shader from the resource manager.

<br>

    public Texture get_texture(String texture)
Get a texture from the resource manager.

# Input

Check out all the keys from [here.](https://www.glfw.org/docs/3.3.2/group__keys.html)

<br>

    public boolean keyboard_key_pressed(int key)
Checks if a key from keyboard is pressed. Returns true if so.

# Shader

    public Shader(String name, String vertex_path, String fragment_path)
Create a shader from a vertex file and a fragment path.

<br>

    public void create_uniform(String uniform)
Create a uniform for the shader. You can find all the uniforms from Shader/UniformConstants.java. If you do this, some of the shader uniforms will be automatically loaded to the shader.

<br>

    public void set_uniform(String uniform, Matrix4f value)
    public void set_uniform(String uniform, Vector4f value)
    public void set_uniform(String uniform, int value)
Set value of a uniform. The uniform has to be created before calling this function.

<br>

    public void create_default_2D_uniforms()
Creates the default uniforms for the default 2D shader.

<br>

    public void create_default_3D_uniforms()
Creates the default uniforms for the default 3D shader.

<br>

    public Map<String, Integer> get_set_uniforms()
Get all the created and set uniforms in a map.

<br>

    public void bind()
Use the shader.

    
# Texture

# Scene

# GameObject
    public GameObject()
Create an empty gameObject. This method is not recommended, but use the way described in #Scene.

<br>

    public Transform transform;
Access the transform component of the gameObject.

<br>

    public List<GameObjectComponent> m_components;
Access all the components of the gameObject. The transform component of the gameObject is not in this list, but use method described above.
    
# GameObjectComponent


# Coming Up

- No need to create the main renderer by hand - will be created automatically.
- The resource manager will automatically load every shader and texture from the resources folder.
- Config file where the user can declare variables that the program will use (for example: window size)
- More error messages!
- Add material to resource manager.
- Application.application_scene_manager will be public, which clears the Application class.
