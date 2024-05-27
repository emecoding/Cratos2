package Application.GameObjectComponent;

import Application.Resource.Model;

public class ModelRenderer extends RenderComponent
{
    private Model m_Model;

    public ModelRenderer(Model model)
    {
        m_Model = model;
        SetShaderName(m_Model.GetShaderName());
    }

    @Override
    public void Render()
    {
        m_Model.Render();
    }

}
