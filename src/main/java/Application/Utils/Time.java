package Application.Utils;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time
{
    private static float LastFrame = 0.0f;
    private static float DeltaTime = 0.0f;
    private static int FramesDone = 0;
    private static float FPS = 0;
    private static float LastTime = (float) glfwGetTime();

    public static void update_time()
    {
        float t = (float) glfwGetTime();

        if(t - LastTime > 0.25 && FramesDone > 10)
        {
            FPS = (float) FramesDone / (t - LastTime);
            LastTime = t;
            FramesDone = 0;
        }

        DeltaTime = (t - LastFrame) / 1000.0f;
        LastFrame = t;

        FramesDone++;
    }

    public static float DeltaTime() { return DeltaTime; }
    public static float FPS() { return FPS; }
}
