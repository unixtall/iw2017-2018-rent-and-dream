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

public class Offer extends CssLayout {
	
	CssLayout offer;
	Housing housing;
	
	public Offer(Housing housing)
	{
		offer = new CssLayout();
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
		
		VerticalLayout verticalLayout = new VerticalLayout();
		// Show the image in the application
		Image image = new Image(null, resource);
		image.setWidth("240px");
		verticalLayout.setSpacing(true);
		//hay que añadir el atributo tipo
		verticalLayout.addComponent(image);
		verticalLayout.addComponent(new Label("Type: Private BedRoom · " + housing.getBeds() + " Beds"));
		verticalLayout.addComponent(new Label(housing.getName()));
		
		//Hay que calcular desde que precio se puede alquilar
		
		Float fromPrice = Float.POSITIVE_INFINITY;
		for(Availability a : housing.getAvailability())
			if(a.getPrice() < fromPrice)
				fromPrice = a.getPrice();	

		verticalLayout.addComponent(new Label("From " + fromPrice + "€ per Night, Rating: " + housing.getAssessment()));
		verticalLayout.setSizeFull();
		
		//popup with a  resume of offer
		this.addLayoutClickListener(event -> getOfferDescription(housing));

		offer = new CssLayout(verticalLayout);
		addComponent(offer);
	}

	public CssLayout getCssLayout() {
		return offer;
	}

	public void setCssLayout(CssLayout cssLayout) {
		this.offer = cssLayout;
	}
	
	public void getOfferDescription(Housing housing)
	{
		/*
		VerticalLayout subContent = new VerticalLayout();
        final HousingView housingView = new HousingView(housing);
        subContent.addComponent(new HousingView(housing));
        addComponent(subContent);
		*/
		
        final Window window = new Window(housing.getName() + " Description page");
        VerticalLayout subContent = new VerticalLayout();
        final HousingView housingView = new HousingView(housing);
        window.setModal(true);
        window.center();
        window.setResizable(false);
        window.setSizeFull();
        window.setWidth(95, Unit.PERCENTAGE);
        window.setHeight(85, Unit.PERCENTAGE);
        
        subContent.addComponent(new HousingView(housing));
        window.setContent(subContent);

        getUI().getUI().addWindow(window);
	}
}
