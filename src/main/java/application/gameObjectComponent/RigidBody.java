package application.gameObjectComponent;

import application.Utils.Time;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class RigidBody extends GameObjectComponent
{
    public float mass = 1.0f;
    public float gravity = 9.81f;
    public Vector3f velocity = new Vector3f(0.0f, 0.0f, 0.0f);
    public Vector3f m_last_frame_forces = new Vector3f(0.0f);
    private final List<Vector3f> m_forces_affecting = new ArrayList<>();

    @Override
    public void update()
    {
        add_force(new Vector3f(0.0f, mass*gravity, 0.0f)); //Gravity

        Vector3f sum_forces = calculate_forces();
        Vector3f sum_acceleration = calculate_sum_acceleration(sum_forces);

        m_last_frame_forces = sum_forces;
        m_forces_affecting.clear();

        velocity.x += sum_acceleration.x() * Time.delta_time();
        velocity.y += sum_acceleration.y() * Time.delta_time();
        velocity.z += sum_acceleration.z() * Time.delta_time();

        apply();
    }

    public void add_force(Vector3f force)
    {
        m_forces_affecting.add(force);
    }

    public Vector3f calculate_forces()
    {
        Vector3f sum_forces = new Vector3f(0.0f);
        for(int i = 0; i < m_forces_affecting.size(); i++)
        {
            sum_forces.x += m_forces_affecting.get(i).x();
            sum_forces.y += m_forces_affecting.get(i).y();
            sum_forces.z += m_forces_affecting.get(i).z();
        }

        return sum_forces;
    }

    private Vector3f calculate_sum_acceleration(Vector3f sum_forces)
    {
        return new Vector3f(sum_forces.x()/mass, sum_forces.y()/mass, sum_forces.z()/mass);
    }

    private void apply()
    {
        parent.transform.position.x += velocity.x;
        parent.transform.position.y += velocity.y;
        parent.transform.position.z += velocity.z;
    }

}
