package application.Utils;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time
{
    private static float last_frame = 0.0f;
    private static float delta_time = 0.0f;
    private static int frames_done = 0;
    private static float fps = 0;
    private static float last_time = (float) glfwGetTime();

    public static void update_time()
    {
        float t = (float) glfwGetTime();

        if(t - last_time > 0.25 && frames_done > 10)
        {
            fps = (float) frames_done / (t - last_time);
            last_time = t;
            frames_done = 0;
        }

        delta_time = (t - last_frame) / 1000.0f;
        last_frame = t;

        frames_done++;
    }

    public static float delta_time() { return delta_time; }
    public static float fps() { return fps; }
}
