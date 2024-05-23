package SandboxGame;

import Application.GameObjectComponent.GameObjectComponent;

public class rotator extends GameObjectComponent
{
    @Override
    public void Update()
    {
        this.m_Parent.m_Transform.Rotation.y += 1.0f;
    }
}
