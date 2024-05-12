package application.applicationSystem.resourceManager;

import application.applicationSystem.ApplicationSystem;
import application.applicationSystem.Debug;
import application.resource.Texture;
import application.resource.shader.Shader;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResourceManager implements ApplicationSystem
{

    private List<Shader> m_shaders = new ArrayList<>();
    private List<Texture> m_textures = new ArrayList<>();

    @Override
    public void initialize()
    {
        //TODO Load every texture on initialize
    }
    @Override
    public void destroy()
    {

    }

    public Shader get_shader(String name)
    {
        for(int i = 0; i < m_shaders.size(); i++)
        {
            if(m_shaders.get(i).get_name().equals(name))
                return m_shaders.get(i);
        }
        return null;
    }
    public Shader add_shader(String name, String vertex_path, String fragment_path)
    {
        Shader new_shader = new Shader(name, vertex_path, fragment_path);
        m_shaders.add(new_shader);
        return new_shader;
    }

    public Texture get_texture(String name)
    {
        for(int i = 0; i < m_textures.size(); i++)
        {
            if(m_textures.get(i).get_name().equals(name))
                return m_textures.get(i);
        }
        return null;
    }
    public Texture add_texture(String name, String path)
    {
        Texture texture = Texture.create_texture_2D(name, path);
        m_textures.add(texture);
        return texture;
    }
    public void add_texture(Texture texture) { m_textures.add(texture); }
    public boolean has_texture(String name)
    {
        for(int i = 0; i < m_textures.size(); i++)
        {
            if(m_textures.get(i).get_name().equals(name))
                return true;
        }
        return false;
    }

    public static String load_resource_from_path(String path) throws Exception
    {
        String result = "";
        try(InputStream in = ResourceManager.class.getResourceAsStream(path);
            Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        }
        catch (Exception e)
        {
            Debug.error("Failed to read file '" + path + "'");
            e.printStackTrace();
        }
        return result;
    }

    public static List<String> load_resource_as_lines_from_path(String path)
    {
        List<String> lines = new ArrayList<>();
        try(InputStream in = ResourceManager.class.getResourceAsStream(path);
            Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
            while(scanner.hasNextLine())
            {
                lines.add(scanner.nextLine());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return lines;
    }
}
