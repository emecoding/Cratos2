package Application.GameObjectComponent.Animator2D;

import Application.ApplicationSystem.Debug;
import Application.GameObjectComponent.RenderComponent;
import Application.GameObjectComponent.Sprite;
import Application.Resource.SpriteSheet;
import Application.Resource.SpriteSheetTexture;
import Application.Utils.Time;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Animator2D extends RenderComponent
{
    private Sprite m_Sprite = null;

    private List<Animation2D> m_Animations;

    private String m_CurrentAnimationName = "";

    private double m_DefaultFrameTime;
    private double m_DefaultFrameRate;

    public Animator2D(String shaderName)
    {
        SetShaderName(shaderName);

        m_Animations = new ArrayList<>();
    }

    public void AddAnimation(String name, List<SpriteSheetTexture> frames)
    {
        Animation2D animation2D = new Animation2D(name, frames);
        animation2D.SetFrameRate(m_DefaultFrameRate);
        animation2D.SetFrameTime(m_DefaultFrameTime);
        m_Animations.add(animation2D);
    }

    public void AddAnimation(Animation2D animation)
    {
        m_Animations.add(animation);
    }

    public void SetCurrentAnimation(String name, boolean reset)
    {
        m_CurrentAnimationName = name;
        if(reset)
            CurrentAnimation().Reset();
    }

    public void SetCurrentAnimation(String name)
    {
        SetCurrentAnimation(name, true);
    }

    public void SetDefaultFrameTime(double defaultFrameTime) { m_DefaultFrameTime = defaultFrameTime; }
    public void SetDefaultFrameRate(double defaultFrameRate) { m_DefaultFrameRate = defaultFrameRate; }

    public void Stop() { CurrentAnimation().Stop(); }
    public void Play(boolean reset) { CurrentAnimation().Play(reset); }


    @Override
    public void Render()
    {
        if(m_Sprite == null)
        {
            m_Sprite = (Sprite) m_Parent.GetComponent(Sprite.class);
            if(m_Sprite == null)
            {
                Debug.Error("No sprite component found from gameObject with Animator2D. Please add one.");
                return;
            }
        }

        if(m_Animations.size() <= 0)
        {
            Debug.Error("No animations set.");
        }

        Animation2D currentAnimation = CurrentAnimation();

        if(currentAnimation.IsPlaying())
        {
            currentAnimation.Update();
        }

        m_Sprite.SetTexture(currentAnimation.CurrentFrame().GetID());
    }

    public Animation2D CurrentAnimation()
    {
        if(m_CurrentAnimationName.equals(""))
        {
            Debug.Error("No current animation set for animator2D");
            return null;
        }

        for(Animation2D animation2D : m_Animations)
        {
            if(animation2D.GetName().equals(m_CurrentAnimationName))
                return animation2D;
        }

        Debug.Error("No such animation set '" + m_CurrentAnimationName + "'");
        return null;
    }
}
