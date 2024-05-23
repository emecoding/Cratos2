package Application.ApplicationSystem;

public class Debug
{
    public static void Error(String message)
    {
        System.err.println("[CRATOS DEBUG]: " + message.toUpperCase());
    }

    public static void Log(String message)
    {
        System.out.println("[CRATOS LOG]: " + message);
    }
}
