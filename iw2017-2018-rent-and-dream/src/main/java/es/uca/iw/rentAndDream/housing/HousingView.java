package es.uca.iw.rentAndDream.housing;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.rentAndDream.availabilities.Availability;

@SpringView(name = HousingView.VIEW_NAME)
public class HousingView extends VerticalLayout implements View{
	
	public static final String VIEW_NAME = "housingView";
	
	private Housing housing;

	@Autowired
	public HousingView(Housing housing) {
		this.housing = housing;
	
		HorizontalLayout features = new HorizontalLayout();
		Label name = new Label(housing.getName() + " Rating: " + housing.getAssessment());
		Label city = new Label(housing.getCity().getName());
		Label beds = new Label(housing.getBeds().toString() + " Beds");
		Label bedrooms = new Label(housing.getBedrooms().toString() + " Bedrooms");
		Label airAconditioner = new Label();
		airAconditioner.setCaption(" Air Aconditioner");
		airAconditioner.setIcon(housing.getAirConditioner()? VaadinIcons.CHECK_CIRCLE_O : VaadinIcons.CLOSE_CIRCLE_O);
		airAconditioner.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
			
		//housing.getReserve().forEach(e -> System.out.println(e.toString()));
		Float lowPrice = Float.POSITIVE_INFINITY;
		for(Availability a : housing.getAvailability())
			if(a.getPrice() < lowPrice)
				lowPrice = a.getPrice();	
		
		Label fromPrice = new Label("From " + lowPrice + "€");
		
		Label description = new Label(housing.getDescription());
		
        name.setStyleName(ValoTheme.LABEL_H3);
        city.setStyleName(ValoTheme.LABEL_H4);
		
		features.addComponents(beds, bedrooms, airAconditioner, fromPrice);

		features.setWidth("50%");
		
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		addComponents(name, city, features, description);
		setSizeFull();
	}
	
	@PostConstruct
	void init()
	{

		//this.setContent(layout);
	}
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}
	
}
