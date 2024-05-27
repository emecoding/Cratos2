package Application.GameObjectComponent.Model;

import Application.Application;
import Application.ApplicationSystem.Debug;
import Application.Resource.Material;
import Application.Resource.Model;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

//https://ahbejarano.gitbook.io/lwjglgamedev/chapter-09

public class ModelLoader
{
    public static Model LoadModel(String name, String path, String shaderName, int assimpFlags)
    {
        File file = new File(path);
        if(!file.exists())
        {
            Debug.Error("Model " + path + " does not exist.");
            return null;
        }

        String model_dir = file.getParent();

        AIScene ai_scene = aiImportFile(path, assimpFlags);
        if(ai_scene == null)
        {
            Debug.Error("Error loading model " + path);
            return null;
        }

        int num_materials = ai_scene.mNumMaterials();
        List<Material> material_list = new ArrayList<>();
        for(int i = 0; i < num_materials; i++)
        {
            AIMaterial ai_material = AIMaterial.create(ai_scene.mMaterials().get(i));
            material_list.add(ProcessMaterial(ai_material, model_dir));
        }

        Model model = new Model(name, shaderName);

        int num_meshes = ai_scene.mNumMeshes();
        PointerBuffer ai_meshes = ai_scene.mMeshes();
        for(int i = 0; i < num_meshes; i++)
        {
            AIMesh ai_mesh = AIMesh.create(ai_meshes.get(i));
            Mesh mesh = ProcessMesh(ai_mesh);

            int material_index = ai_mesh.mMaterialIndex();
            Material material = null;
            if(material_index >= 0 && material_index < material_list.size())
                material = material_list.get(material_index);
            /*else
                material = default_material;*/

            mesh.SetMaterial(material);

            model.AddMesh(mesh);
        }


        return model;

    }

    private static Mesh ProcessMesh(AIMesh ai_mesh)
    {
        float[] vertices = ProcessVertices(ai_mesh);
        float[] tex_coords = ProcessTexCoords(ai_mesh);
        int[] indices = ProcessIndices(ai_mesh);

        if(tex_coords.length == 0)
        {
            int num_elements = (vertices.length/3)*2;
            tex_coords = new float[num_elements];
        }

        return new Mesh(vertices, tex_coords, indices);
    }

    private static int[] ProcessIndices(AIMesh ai_mesh)
    {
        List<Integer> indices = new ArrayList<>();
        int num_faces = ai_mesh.mNumFaces();
        AIFace.Buffer ai_faces = ai_mesh.mFaces();
        for(int i = 0; i < num_faces; i++)
        {
            AIFace ai_face = ai_faces.get(i);
            IntBuffer buffer = ai_face.mIndices();
            while(buffer.remaining() > 0)
            {
                indices.add(buffer.get());
            }
        }

        return indices.stream().mapToInt(Integer::intValue).toArray();
    }

    private static float[] ProcessTexCoords(AIMesh ai_mesh)
    {
        AIVector3D.Buffer buffer = ai_mesh.mTextureCoords(0);
        if(buffer == null)
            return new float[]{};

        float[] data = new float[buffer.remaining() * 2];
        int pos = 0;
        while(buffer.remaining() > 0)
        {
            AIVector3D tex_coord = buffer.get();
            data[pos++] = tex_coord.x();
            data[pos++] = 1 - tex_coord.y();
        }

        return data;
    }

    private static float[] ProcessVertices(AIMesh ai_mesh)
    {
        AIVector3D.Buffer buffer = ai_mesh.mVertices();
        float[] data = new float[buffer.remaining() * 3];
        int pos = 0;
        while(buffer.remaining() > 0)
        {
            AIVector3D vertex = buffer.get();
            data[pos++] = vertex.x();
            data[pos++] = vertex.y();
            data[pos++] = vertex.z();
        }

        return data;
    }
    private static Material ProcessMaterial(AIMaterial ai_material, String model_dir)
    {

        Material material = new Material();
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            AIString name = AIString.create();
            int result = aiGetMaterialString(ai_material, AI_MATKEY_NAME, aiTextureType_NONE, 0, name);
            if(result == aiReturn_SUCCESS)
                material.SetMaterialName(name.dataString());
            else
            {
                Debug.Error("Failed to get material's name");
                material.SetMaterialName("Material" + Application.ResourceManager().GetAmountOfMaterials());
            }


            AIColor4D color = AIColor4D.create();
            result = aiGetMaterialColor(ai_material, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, color);

            if(result == aiReturn_SUCCESS)
                material.SetDiffuseColor(color.r(), color.g(), color.b(), color.a());
            else
            {
                Debug.Error("Failed to get material's diffuse color");
                material.SetDiffuseColor(0.0f, 0.0f, 0.0f, 0.0f);
            }

            AIString aiTexturePath = AIString.calloc(stack);
            aiGetMaterialTexture(ai_material, aiTextureType_DIFFUSE, 0, aiTexturePath,
                    (IntBuffer) null, null, null, null, null, null);
            String texture_path = aiTexturePath.dataString();
            if(texture_path != null && texture_path.length() > 0)
            {
                material.SetTexturePath(texture_path);
            }
        }

        Application.ResourceManager().AddMaterial(material);
        return material;
    }

}