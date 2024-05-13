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
Currently you do not need to touch the renderer in order for the program to execute. Later on the renderer class will be made so that the user can create their own renderer expanding on the Renderer.java class.


- application_renderer (no need to care about that for the user probably)
- application_resource_manager
- application_input
- application_collision_manager (no need to care about that for the user)

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

# Scene

# GameObject
    Code can be added like this.

# GameObjectComponent


# Coming Up

- No need to create the main renderer by hand - will be created automatically.
- The resource manager will automatically load every shader and texture from the resources folder.
- Config file where the user can declare variables that the program will use (for example: window size)