package application.gameObjectComponent;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform extends GameObjectComponent
{
    public Vector3f position;
    public Vector3f rotation;
    public Vector3f scale;

    public Transform(Vector3f position)
    {
        this.position = position;
        this.rotation = new Vector3f(0.0f, 0.0f, 0.0f);
        this.scale = new Vector3f(1.0f, 1.0f, 1.0f);
    }

    @Override
    public void initialize()
    {

    }

    @Override
    public void update()
    {

    }

    @Override
    public void destroy()
    {

    }

    public Matrix4f get_transform_2D_matrix()
    {
        Matrix4f mat4 = new Matrix4f();
        mat4 = mat4.translate(new Vector3f(position.x, position.y, 0.0f));
        mat4 = mat4.scale(this.scale);
        return mat4;
    }

    public Matrix4f get_transform_3D_matrix()
    {
        Matrix4f mat4 = new Matrix4f();
        mat4 = mat4.translate(new Vector3f(position.x, position.y, 0.0f));

        mat4 = mat4.rotate((float)Math.toRadians(this.rotation.x()), new Vector3f(1.0f, 0.0f, 0.0f));
        mat4 = mat4.rotate((float)Math.toRadians(this.rotation.y()), new Vector3f(0.0f, 1.0f, 0.0f));
        mat4 = mat4.rotate((float)Math.toRadians(this.rotation.z()), new Vector3f(0.0f, 0.0f, 1.0f));

        mat4 = mat4.scale(this.scale);

        return mat4;
    }
}
