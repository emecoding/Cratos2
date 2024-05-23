package Application.Resource;

import Application.ApplicationSystem.Debug;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL30C.glGenerateMipmap;

public class Texture extends Resource
{
    private String m_Path;
    private int m_ID;
    private int m_Width, m_Height;
    public Texture(String name, String path)
    {
        SetName(name);

        m_Path = path;
    }

    public void Initialize(int id, int width, int height)
    {
        m_ID = id;
        m_Width = width;
        m_Height = height;
    }

    public void Use()
    {
        glBindTexture(GL_TEXTURE_2D, m_ID);
    }

    public static Texture CreateTexture3D(String name, String path)
    {
        Texture texture = new Texture(name, path);

        int width, height, color_channels;
        ByteBuffer buffer;


        try(MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(path, w, h, c, 4);
            if(buffer == null)
                Debug.Error("Failed to load texture '" + path + "': " + STBImage.stbi_failure_reason());

            width = w.get();
            height = h.get();
            color_channels = c.get();

            int ID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, ID);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            int color_type = (color_channels == 4 ? GL_RGBA : GL_RGB);
            glTexImage2D(GL_TEXTURE_2D, 0, color_type, width, height, 0, color_type, GL_UNSIGNED_BYTE, buffer);
            glGenerateMipmap(GL_TEXTURE_2D);

            STBImage.stbi_image_free(buffer);

            glBindTexture(GL_TEXTURE_2D,0);

            texture.Initialize(ID, width, height);
        }
        catch(Exception e)
        {
            Debug.Error("Failed to load texture '" + path + "'");
            e.printStackTrace();
        }

        return texture;
    }

    public static Texture CreateTexture2D(String name, String path)
    {
        Texture texture = new Texture(name, path);

        int width, height, color_channels;
        ByteBuffer buffer;

        try(MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(path, w, h, c, 4);
            if(buffer == null)
                Debug.Error("Failed to load texture '" + path + "': " + STBImage.stbi_failure_reason());

            width = w.get();
            height = h.get();

            int ID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, ID);


            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            glGenerateMipmap(GL_TEXTURE_2D);

            STBImage.stbi_image_free(buffer);

            glBindTexture(GL_TEXTURE_2D,0);

            texture.Initialize(ID, width, height);
        }
        catch(Exception e)
        {
            Debug.Error("Failed to load texture '" + path + "'");
            e.printStackTrace();
        }

        return texture;
    }
}
