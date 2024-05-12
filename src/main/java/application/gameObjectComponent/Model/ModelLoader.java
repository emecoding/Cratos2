package application.gameObjectComponent.Model;

import application.applicationSystem.Debug;
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
    public static Model load_model(String path, String shader_name, int assimp_flags)
    {
        File file = new File(path);
        if(!file.exists())
        {
            Debug.error("Model " + path + " does not exist.");
            return null;
        }

        String model_dir = file.getParent();

        AIScene ai_scene = aiImportFile(path, assimp_flags);
        if(ai_scene == null)
        {
            Debug.error("Error loading model " + path);
            return null;
        }

        int num_materials = ai_scene.mNumMaterials();
        List<Material> material_list = new ArrayList<>();
        for(int i = 0; i < num_materials; i++)
        {
            AIMaterial ai_material = AIMaterial.create(ai_scene.mMaterials().get(i));
            material_list.add(process_material(ai_material, model_dir));
        }

        Model model = new Model(shader_name);

        int num_meshes = ai_scene.mNumMeshes();
        PointerBuffer ai_meshes = ai_scene.mMeshes();
        for(int i = 0; i < num_meshes; i++)
        {
            AIMesh ai_mesh = AIMesh.create(ai_meshes.get(i));
            Mesh mesh = process_mesh(ai_mesh);

            int material_index = ai_mesh.mMaterialIndex();
            Material material = null;
            if(material_index >= 0 && material_index < material_list.size())
                material = material_list.get(material_index);
            /*else
                material = default_material;*/

            mesh.set_material(material);

            model.add_mesh(mesh);
        }


        return model;

    }

    private static Mesh process_mesh(AIMesh ai_mesh)
    {
        float[] vertices = process_vertices(ai_mesh);
        float[] tex_coords = process_tex_coords(ai_mesh);
        int[] indices = process_indices(ai_mesh);

        if(tex_coords.length == 0)
        {
            int num_elements = (vertices.length/3)*2;
            tex_coords = new float[num_elements];
        }

        return new Mesh(vertices, tex_coords, indices);
    }

    private static int[] process_indices(AIMesh ai_mesh)
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

    private static float[] process_tex_coords(AIMesh ai_mesh)
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

    private static float[] process_vertices(AIMesh ai_mesh)
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
    private static Material process_material(AIMaterial ai_material, String model_dir)
    {
        Material material = new Material();
        try(MemoryStack stack = MemoryStack.stackPush())
        {
            AIColor4D color = AIColor4D.create();
            int result = aiGetMaterialColor(ai_material, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, color);

            if(result == aiReturn_SUCCESS)
                material.set_diffuse_color(color.r(), color.g(), color.b(), color.a());
            else
            {
                Debug.error("Failed to get material's diffuse color");
                material.set_diffuse_color(0.0f, 0.0f, 0.0f, 0.0f);
            }

            AIString aiTexturePath = AIString.calloc(stack);
            aiGetMaterialTexture(ai_material, aiTextureType_DIFFUSE, 0, aiTexturePath,
                    (IntBuffer) null, null, null, null, null, null);
            String texture_path = aiTexturePath.dataString();
            if(texture_path != null && texture_path.length() > 0)
            {
                material.set_texture_path(texture_path);
            }
        }

        return material;
    }

}