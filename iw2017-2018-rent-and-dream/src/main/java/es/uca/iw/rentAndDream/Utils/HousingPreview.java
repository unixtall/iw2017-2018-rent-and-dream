package es.uca.iw.rentAndDream.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.core.io.ClassPathResource;

import com.vaadin.server.FileResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.uca.iw.rentAndDream.availabilities.Availability;
import es.uca.iw.rentAndDream.housing.Housing;
import es.uca.iw.rentAndDream.housing.HousingView;

public class HousingPreview extends VerticalLayout {
	
	Housing housing;
	
	public HousingPreview(Housing housing)
	{
		this.housing =housing;
		
		// Image as a file resource

		FileResource resource = null;
		ClassPathResource file = new ClassPathResource("images/foto1.jpg");
		try {
			resource = new FileResource(file.getFile());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		// Show the image in the application
		Image image = new Image(null, resource);
		image.setWidth("240px");
		setSpacing(true);
		//hay que añadir el atributo tipo
		addComponent(image);
		addComponent(new Label("Name: " + housing.getName()));
		addComponent(new Label("Number of reserves: " + housing.getReserve().size()));
		
		setSizeFull();
		
		//popup with a  resume of offer
		//this.addLayoutClickListener(event -> getHousingPreviewDescription(housing));

	}
	
}