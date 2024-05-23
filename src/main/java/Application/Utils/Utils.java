package Application.Utils;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL15C.glBufferData;

public class Utils
{
    public static FloatBuffer StoreDataInFloatBuffer(float[] data)
    {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    public static IntBuffer StoreDataInIntBuffer(int[] data)
    {
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
    public static int CreateVAO() { return glGenVertexArrays(); }
    public static int CreateBuffer(int buffer_type, FloatBuffer data, int draw_type)
    {
        int buffer = glGenBuffers();
        glBindBuffer(buffer_type, buffer);
        glBufferData(buffer_type, data, draw_type);
        return buffer;
    }
    public static int CreateBuffer(int buffer_type, IntBuffer data, int draw_type)
    {
        int buffer = glGenBuffers();
        glBindBuffer(buffer_type, buffer);
        glBufferData(buffer_type, data, draw_type);
        return buffer;
    }
}
