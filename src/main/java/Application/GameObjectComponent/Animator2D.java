package Application.GameObjectComponent;

import Application.Application;
import Application.ApplicationSystem.Debug;
import Application.Resource.SpriteSheet;
import Application.Resource.SpriteSheetTexture;
import Application.Utils.Time;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.glBindTexture;

public class Animator2D extends RenderComponent
{
    private SpriteSheet m_SpriteSheet;
    private Sprite m_Sprite = null;

    private Map<String, List<SpriteSheetTexture>> m_Animations;

    private String m_CurrentAnimationName;

    private int m_CurrentFrameIndex = 0;

    private boolean m_IsPlaying = true;

    private double m_FrameTime;
    private double m_FrameRate;
    private double m_CurrentFrameTime;

    public Animator2D(String shaderName, SpriteSheet spriteSheet)
    {
        SetShaderName(shaderName);
        m_SpriteSheet = spriteSheet;

        m_Animations = new HashMap<>();
    }

    public void SetAnimation(String name, int firstFrameIndex, int lastFrameIndex)
    {
        m_Animations.put(name, m_SpriteSheet.GetSpritesFromIndexAToIndexB(firstFrameIndex, lastFrameIndex));
    }

    public void SetCurrentAnimation(String name, boolean reset)
    {
        m_CurrentAnimationName = name;
        if(reset)
            m_CurrentFrameIndex = 0;
    }

    public void SetCurrentAnimation(String name)
    {
        SetCurrentAnimation(name, true);
    }

    public void Stop() { m_IsPlaying = false; }
    public void Play(boolean reset)
    {
        m_IsPlaying = true;
        if(reset)
            m_CurrentFrameIndex = 0;
    }

    public void SetFrameTime(double frameTime) { m_FrameTime = frameTime; }
    public void SetFrameRate(double frameRate) { m_FrameRate = frameRate; }

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


        if(m_IsPlaying)
        {
            m_CurrentFrameTime += m_FrameRate * Time.DeltaTime();
            if(m_CurrentFrameTime >= m_FrameTime)
            {
                m_CurrentFrameTime = 0;
                m_CurrentFrameIndex++;
                if(m_CurrentFrameIndex >= m_Animations.get(m_CurrentAnimationName).size())
                    m_CurrentFrameIndex = 0;
            }
        }

        m_Sprite.SetTexture(m_Animations.get(m_CurrentAnimationName).get(m_CurrentFrameIndex).GetID());
    }
}
