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
import es.uca.iw.rentAndDream.housing.HousingInfo;

public class Offer extends VerticalLayout {
	
	Housing housing;
	
	public Offer(Housing housing)
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
		addComponent(new Label("Type: Private BedRoom · " + housing.getBeds() + " Beds"));
		addComponent(new Label(housing.getName()));
		
		//Hay que calcular desde que precio se puede alquilar
		
		Float fromPrice = Float.POSITIVE_INFINITY;
		for(Availability a : housing.getAvailability())
			if(a.getPrice() < fromPrice)
				fromPrice = a.getPrice();	

		addComponent(new Label("From " + fromPrice + "€ per Night, Rating: " + housing.getAssessment()));
		setSizeFull();
		
		//popup with a  resume of offer
		this.addLayoutClickListener(event -> new WindowManager("Offer Description", new HousingInfo(housing)));

	}
	
	public void getOfferDescription(Housing housing)
	{	
        Window window = new Window(housing.getName() + " Description page");
        VerticalLayout subContent = new VerticalLayout();
        window.setModal(true);
        window.center();
        window.setResizable(false);
        window.setSizeFull();
        window.setWidth(95, Unit.PERCENTAGE);
        window.setHeight(85, Unit.PERCENTAGE);
        
        subContent.addComponent(new HousingInfo(housing));
        window.setContent(subContent);

        getUI().getUI().addWindow(window);
	}
}