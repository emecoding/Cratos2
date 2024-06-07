package Application.Resource;

public class SpriteSheetTexture
{
    private int m_ID;
    private int m_XOnSpriteSheet;
    private int m_YOnSpriteSheet;


    public SpriteSheetTexture(int ID, int x, int y)
    {
        this.m_ID = ID;
        this.m_XOnSpriteSheet = x;
        this.m_YOnSpriteSheet = y;
    }

    public int GetID() { return this.m_ID; }
    public int GetXOnSpriteSheet() { return this.m_XOnSpriteSheet; }
    public int GetYOnSpriteSheet() { return this.m_YOnSpriteSheet; }
}
