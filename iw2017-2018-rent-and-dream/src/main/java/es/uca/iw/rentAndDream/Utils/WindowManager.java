package es.uca.iw.rentAndDream.Utils;

import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class WindowManager extends VerticalLayout{
	
	public WindowManager(String caption, Component component)
	{
		Window window = new Window(caption);
		//window.setCaption(caption);
	    VerticalLayout subContent = new VerticalLayout();
	    window.setModal(true);
	    window.center();
	    window.setResizable(false);
	    window.setSizeFull();
	    window.setWidth(95, Unit.PERCENTAGE);
	    window.setHeight(85, Unit.PERCENTAGE);
	    
	    subContent.addComponent(component);
	    window.setContent(subContent);
	    UI.getCurrent().addWindow(window);
	}
}