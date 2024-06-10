package Application.GameObjectComponent.Animator2D;

import Application.Resource.SpriteSheetTexture;
import Application.Utils.Time;

import java.util.List;

public class Animation2D
{
    private List<SpriteSheetTexture> m_Frames;

    private String m_Name;

    private int m_CurrentFrameIndex = 0;

    private boolean m_IsPlaying = true;

    private double m_FrameTime;
    private double m_FrameRate;
    private double m_CurrentFrameTime;

    public Animation2D(String name, List<SpriteSheetTexture> frames)
    {
        m_Name = name;
        m_Frames = frames;
    }

    public void SetFrameTime(double frameTime)
    {
        m_FrameTime = frameTime;
    }

    public void SetFrameRate(double frameRate)
    {
        m_FrameRate = frameRate;
    }

    public void Stop()
    {
        m_IsPlaying = false;
    }

    public void Play(boolean reset)
    {
        m_IsPlaying = true;
        if(reset)
            Reset();
    }

    public void Update()
    {
        m_CurrentFrameTime += m_FrameRate * Time.DeltaTime();
        if(m_CurrentFrameTime >= m_FrameTime)
        {
            m_CurrentFrameTime = 0;
            m_CurrentFrameIndex++;
            if(m_CurrentFrameIndex >= m_Frames.size())
                m_CurrentFrameIndex = 0;
        }
    }

    public void Reset() { m_CurrentFrameIndex = 0; }

    public String GetName() { return m_Name; }

    public boolean IsPlaying() { return m_IsPlaying; }

    public SpriteSheetTexture CurrentFrame() { return m_Frames.get(m_CurrentFrameIndex); }
}
