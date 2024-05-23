package Application.GameObjectComponent;

import Application.Utils.Time;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class RigidBody extends GameObjectComponent
{
    public float Mass = 1.0f;
    public float Gravity = 9.81f;
    public Vector3f Velocity = new Vector3f(0.0f, 0.0f, 0.0f);
    public Vector3f m_LastFrameForces = new Vector3f(0.0f);
    private final List<Vector3f> m_ForcesAffecting = new ArrayList<>();

    @Override
    public void Update()
    {
        AddForce(new Vector3f(0.0f, Mass*Gravity, 0.0f)); //Gravity

        Vector3f sum_forces = CalculateForces();
        Vector3f sum_acceleration = CalculateSumAcceleration(sum_forces);

        m_LastFrameForces = sum_forces;
        m_ForcesAffecting.clear();

        Velocity.x += sum_acceleration.x() * Time.DeltaTime();
        Velocity.y += sum_acceleration.y() * Time.DeltaTime();
        Velocity.z += sum_acceleration.z() * Time.DeltaTime();

        Apply();
    }

    public void AddForce(Vector3f force)
    {
        m_ForcesAffecting.add(force);
    }

    public Vector3f CalculateForces()
    {
        Vector3f sum_forces = new Vector3f(0.0f);
        for(int i = 0; i < m_ForcesAffecting.size(); i++)
        {
            sum_forces.x += m_ForcesAffecting.get(i).x();
            sum_forces.y += m_ForcesAffecting.get(i).y();
            sum_forces.z += m_ForcesAffecting.get(i).z();
        }

        return sum_forces;
    }

    private Vector3f CalculateSumAcceleration(Vector3f sum_forces)
    {
        return new Vector3f(sum_forces.x()/Mass, sum_forces.y()/Mass, sum_forces.z()/Mass);
    }

    private void Apply()
    {
        m_Parent.m_Transform.Position.x += Velocity.x;
        m_Parent.m_Transform.Position.y += Velocity.y;
        m_Parent.m_Transform.Position.z += Velocity.z;
    }

}
