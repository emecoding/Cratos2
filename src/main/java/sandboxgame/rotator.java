package sandboxgame;

import application.gameObjectComponent.GameObjectComponent;

public class rotator extends GameObjectComponent
{
    @Override
    public void update()
    {
        this.parent.transform.rotation.y += 1.0f;
    }
}
