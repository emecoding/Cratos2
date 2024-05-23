package application.applicationSystem.resourceManager;

import application.applicationSystem.ApplicationSystem;
import application.applicationSystem.Debug;
import application.resource.Texture;
import application.resource.shader.Shader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResourceManager implements ApplicationSystem
{

    private List<Shader> m_shaders = new ArrayList<>();
    private List<Texture> m_textures = new ArrayList<>();
    private List<String> m_resource_folders_to_load = new ArrayList<>();

    @Override
    public void initialize()
    {
        //TODO Load every texture on initialize
        load_every_resource_folder();
    }
    @Override
    public void destroy()
    {

    }

    public void add_resource_folder_to_load(String folder)
    {
        m_resource_folders_to_load.add(folder);
    }
    public Shader get_shader(String name)
    {
        for(int i = 0; i < m_shaders.size(); i++)
        {
            if(m_shaders.get(i).get_name().equals(name))
            {
                return m_shaders.get(i);
            }

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
    public static String read_file(String path)
    {
        String data = "";

        try
        {
            File file = new File(path);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine())
            {
                data += scanner.nextLine() + System.lineSeparator();
            }
        }
        catch (FileNotFoundException e)
        {
            Debug.error("Failed to find file '" + path + "'");
            e.printStackTrace();
        }

        return data;

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

    private void load_every_resource_folder()
    {
        for(int i = 0; i < m_resource_folders_to_load.size(); i++)
        {
            File[] files_in_folder = new File(m_resource_folders_to_load.get(i)).listFiles();
            if(files_in_folder == null)
            {
                Debug.error("Didn't find resource folder '" + m_resource_folders_to_load.get(i) + "' -> skipping");
                continue;
            }
            for(File file : files_in_folder)
            {
                //Load shaders
                if(file.isDirectory())//shader folder has to contain the word 'shader'
                {
                    if(file.getName().toUpperCase().contains("SHADER"))
                    {
                        load_shader_from_path(file);
                        continue;
                    }
                    else
                    {
                        add_resource_folder_to_load(file.getPath());
                    }
                }

                //Load single files
                String[] splitted_file = file.getName().split(System.getProperty("file.separator"));
                String[] splitted_file_name = splitted_file[splitted_file.length-1].split("\\.");
                String file_type = splitted_file_name[splitted_file_name.length-1];
                switch(file_type)
                {
                    case "png":
                        add_texture(splitted_file_name[0], file.getPath());
                        break;

                    default:
                        Debug.error("File type '." + file_type + "' is not currently supported. Check out the supported file types from the documentation.");
                        break;
                }

            }
        }
    }

    private void load_shader_from_path(File folder)
    {
        File[] files_in_folder = folder.listFiles();
        String shader_name = folder.getName();
        String vertex_shader_path = "";
        String fragment_shader_path = "";
        for(int i = 0; i < files_in_folder.length; i++)
        {
            if(files_in_folder[i].getName().contains("vs"))//vertex shader
                vertex_shader_path = files_in_folder[i].getPath();

            if(files_in_folder[i].getName().contains("fs"))//fragment shader
                fragment_shader_path = files_in_folder[i].getPath();
        }

        if(vertex_shader_path.equals(""))
        {
            Debug.error("Didn't find the vertex shader file for the shader '" + folder.getName() + "'. Make sure you have named them correctly. Check out how to name them correctly from the documentation.");
        }

        if(fragment_shader_path.equals(""))
        {
            Debug.error("Didn't find the fragment shader file for the shader '" + folder.getName() + "'. Make sure you have named them correctly. Check out how to name them correctly from the documentation.");
        }

        add_shader(shader_name, vertex_shader_path, fragment_shader_path);
    }
}
