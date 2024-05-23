package Application.ApplicationSystem.ResourceManager;

import Application.ApplicationSystem.ApplicationSystem;
import Application.ApplicationSystem.Debug;
import Application.Resource.Material;
import Application.Resource.Texture;
import Application.Resource.Shader.Shader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResourceManager implements ApplicationSystem
{

    private List<Shader> m_Shaders = new ArrayList<>();
    private List<Texture> m_Textures = new ArrayList<>();
    private List<Material> m_Materials = new ArrayList<>();
    private List<String> m_ResourceFoldersToLoad = new ArrayList<>();

    @Override
    public void Initialize()
    {
        //TODO Load every texture on initialize
        LoadEveryResourceFolder();
    }
    @Override
    public void Destroy()
    {

    }

    public void AddResourceFolderToLoad(String folder)
    {
        m_ResourceFoldersToLoad.add(folder);
    }
    public Shader GetShader(String name)
    {
        for(int i = 0; i < m_Shaders.size(); i++)
        {
            if(m_Shaders.get(i).GetName().equals(name))
            {
                return m_Shaders.get(i);
            }

        }
        return null;
    }
    public Shader AddShader(String name, String vertex_path, String fragment_path)
    {
        Shader new_shader = new Shader(name, vertex_path, fragment_path);
        m_Shaders.add(new_shader);
        return new_shader;
    }

    public Texture GetTexture(String name)
    {
        for(int i = 0; i < m_Textures.size(); i++)
        {
            if(m_Textures.get(i).GetName().equals(name))
                return m_Textures.get(i);
        }
        return null;
    }
    public Texture AddTexture(String name, String path)
    {
        Texture texture = Texture.CreateTexture2D(name, path);
        m_Textures.add(texture);
        return texture;
    }
    public void AddTexture(Texture texture) { m_Textures.add(texture); }
    public Material AddMaterial(Material material)
    {
        m_Materials.add(material);
        return material;
    }
    public Material GetMaterial(String name)
    {
        for(int i = 0; i < m_Materials.size(); i++)
        {
            if(m_Materials.get(i).GetName().equals(name))
                return m_Materials.get(i);
        }
        return null;
    }
    public int GetAmountOfMaterials() { return m_Materials.size(); }
    public boolean HasTexture(String name)
    {
        for(int i = 0; i < m_Textures.size(); i++)
        {
            if(m_Textures.get(i).GetName().equals(name))
                return true;
        }
        return false;
    }

    public static String LoadResourceFromPath(String path) throws Exception
    {
        String result = "";
        try(InputStream in = ResourceManager.class.getResourceAsStream(path);
            Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        }
        catch (Exception e)
        {
            Debug.Error("Failed to read file '" + path + "'");
            e.printStackTrace();
        }
        return result;
    }
    public static String ReadFile(String path)
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
            Debug.Error("Failed to find file '" + path + "'");
            e.printStackTrace();
        }

        return data;

    }
    public static List<String> LoadResourceAsLinesFromPath(String path)
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

    private void LoadEveryResourceFolder()
    {
        for(int i = 0; i < m_ResourceFoldersToLoad.size(); i++)
        {
            /*URL url = ResourceManager.class.getResource(m_ResourceFoldersToLoad.get(i));
            if(url == null)
            {
                Debug.Error("No such path as " + m_ResourceFoldersToLoad.get(i));
                continue;
            }
            Debug.Log(url.getPath());
            File[] files_in_folder = new File(url.getPath()).listFiles();
            */

            File[] files_in_folder = new File(m_ResourceFoldersToLoad.get(i)).listFiles();
            if(files_in_folder == null)
            {
                Debug.Error("Didn't find resource folder '" + m_ResourceFoldersToLoad.get(i) + "' -> skipping");
                continue;
            }

            for(File file : files_in_folder)
            {
                //Load shaders
                if(file.isDirectory() && file.getName().toUpperCase().contains("SHADER"))//shader folder has to contain the word 'shader'
                {
                    LoadShaderFromFolder(file);
                    continue;
                }

                //Load single files
                String[] splitted_file = file.getName().split(System.getProperty("file.separator"));
                String[] splitted_file_name = splitted_file[splitted_file.length-1].split("\\.");
                String file_type = splitted_file_name[splitted_file_name.length-1];
                switch(file_type)
                {
                    case "png":
                        AddTexture(splitted_file_name[0], file.getPath());
                        break;

                    default:
                        Debug.Error("File type '." + file_type + "' is not currently supported. Check out the supported file types from the documentation.");
                        break;
                }

            }
        }
    }

    private void LoadShaderFromFolder(File folder)
    {
        File[] files_in_folder = folder.listFiles();
        String shader_name = folder.getName();
        String vertex_shader_path = "";
        String fragment_shader_path = "";
        for(int i = 0; i < files_in_folder.length; i++)
        {
            String path = files_in_folder[i].getPath();//.replace("resources"+System.getProperty("file.separator")+"main", "main"+System.getProperty("file.separator")+"resources");
            if(files_in_folder[i].getName().contains("vs"))//vertex shader
                vertex_shader_path = path;

            if(files_in_folder[i].getName().contains("fs"))//fragment shader
                fragment_shader_path = path;
        }

        if(vertex_shader_path.equals(""))
        {
            Debug.Error("Didn't find the vertex shader file for the shader '" + folder.getName() + "'. Make sure you have named them correctly. Check out how to name them correctly from the documentation.");
        }

        if(fragment_shader_path.equals(""))
        {
            Debug.Error("Didn't find the fragment shader file for the shader '" + folder.getName() + "'. Make sure you have named them correctly. Check out how to name them correctly from the documentation.");
        }

        AddShader(shader_name, vertex_shader_path, fragment_shader_path);
    }
}
