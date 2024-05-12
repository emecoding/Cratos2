package application.resource;

public class Resource
{
    String m_name = null;

    protected void set_name(String name) { m_name = name; }
    public String get_name() { return m_name; }
    public void destroy() {}

}
