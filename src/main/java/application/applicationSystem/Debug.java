package application.applicationSystem;

public class Debug
{
    public static void error(String message)
    {
        System.err.println("[CRATOS DEBUG]: " + message.toUpperCase());
    }

    public static void log(String message)
    {
        System.out.println("[CRATOS LOG]: " + message);
    }
}
