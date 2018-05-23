package es.uca.iw.rentAndDream.Utils;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;

public class HorizontalItemLayout extends CustomComponent{
	
	private CssLayout cssLayout = new CssLayout();
	
	public HorizontalItemLayout()
	{
		this.setCompositionRoot(cssLayout);
	}
	
	public void addComponent(Component component)
	{
		cssLayout.addComponent(new CssLayout(component));
	}
	
	public void addComponent(Component... component)
	{
		cssLayout.addComponents(component);
	}
	
	public void removeAllComponents()
	{
		cssLayout.removeAllComponents();
	}
}
