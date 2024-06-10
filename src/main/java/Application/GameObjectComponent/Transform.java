package Application.GameObjectComponent;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform extends GameObjectComponent
{
    public Vector3f Position;
    public Vector3f Rotation;
    public Vector3f Scale;

    public Transform(Vector3f position)
    {
        this.Position = position;
        this.Rotation = new Vector3f(0.0f, 0.0f, 0.0f);
        this.Scale = new Vector3f(1.0f, 1.0f, 1.0f);
    }

    @Override
    public void Initialize()
    {

    }

    @Override
    public void Update()
    {

    }

    @Override
    public void Destroy()
    {

    }

    public Matrix4f GetTransform2DMatrix()
    {
        Matrix4f mat4 = new Matrix4f();
        mat4 = mat4.translate(new Vector3f(Position.x, Position.y, 0.0f));

        mat4 = mat4.translate(new Vector3f(0.5f * Scale.x, 0.5f * Scale.y, 0.0f));

        mat4 = mat4.rotate(Rotation.x, new Vector3f(1.0f, 0.0f, 0.0f));
        mat4 = mat4.rotate(Rotation.y, new Vector3f(0.0f, 1.0f, 0.0f));
        mat4 = mat4.rotate(Rotation.z, new Vector3f(0.0f, 0.0f, 1.0f));

        mat4 = mat4.translate(new Vector3f(-0.5f * Scale.x, -0.5f * Scale.y, 0.0f));

        mat4 = mat4.scale(this.Scale);
        return mat4;
    }

    public Matrix4f GetTransform3DMatrix()
    {
        Matrix4f mat4 = new Matrix4f();
        mat4 = mat4.translate(new Vector3f(Position.x, Position.y, 0.0f));

        mat4 = mat4.rotate((float)Math.toRadians(this.Rotation.x()), new Vector3f(1.0f, 0.0f, 0.0f));
        mat4 = mat4.rotate((float)Math.toRadians(this.Rotation.y()), new Vector3f(0.0f, 1.0f, 0.0f));
        mat4 = mat4.rotate((float)Math.toRadians(this.Rotation.z()), new Vector3f(0.0f, 0.0f, 1.0f));

        mat4 = mat4.scale(this.Scale);

        return mat4;
    }
}
