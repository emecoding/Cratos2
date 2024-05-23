package Application.Resource;

public class Resource
{
    String m_Name = null;

    protected void SetName(String name) { m_Name = name; }
    public String GetName() { return m_Name; }
    public void Destroy() {}

}
