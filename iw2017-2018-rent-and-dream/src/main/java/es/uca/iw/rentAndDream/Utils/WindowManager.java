package es.uca.iw.rentAndDream.Utils;

import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class WindowManager extends VerticalLayout{
	
	Window window;
	
	public WindowManager(String caption, Component component)
	{
		window = new Window(caption);
		window.setVisible(true);
	    VerticalLayout subContent = new VerticalLayout();
	    window.setModal(true);
	    window.center();
	    window.setResizable(false);
	    window.setDraggable(false);


	    window.setSizeUndefined();
	    //window.setWidth(95, Unit.PERCENTAGE);
	    //window.setHeight(85, Unit.PERCENTAGE);
	    
	    
	    subContent.addComponent(component);
	    
	    window.setContent(subContent);
	    UI.getCurrent().addWindow(window);
	}

	public Window getWindow() {
		return window;
	}

	public void setWindow(Window window) {
		this.window = window;
	}
	
}