package es.uca.iw.rentAndDream.views;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.tuningdatefield.TuningDateField;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.rentAndDream.Utils.MultiSelectDateField;
import es.uca.iw.rentAndDream.entities.Availability;
import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.services.ReserveService;

@SpringView(name = HousingInfoView.VIEW_NAME)
public class HousingInfoView extends VerticalLayout implements View{
	
	public static final String VIEW_NAME = "housingInfo";
	
	private Housing housing;
	
	@Autowired
	private ReserveService reserveService;

	final private InlineDateField reserveDatePicker = new InlineDateField();
	final private TuningDateField tuningDateField = new TuningDateField("Availability Calendar");

	@Autowired
	public HousingInfoView(Housing housing) {
		this.housing = housing;
		this.reserveService = reserveService;
	    this.reserveDatePicker.setResolution(DateResolution.DAY);
	
		HorizontalLayout features = new HorizontalLayout();
		Label name = new Label(housing.getName() + " Rating: " + housing.getAssessment());
		Label city = new Label(housing.getCity().getName());
		Label beds = new Label(housing.getBeds().toString() + " Beds");
		Label bedrooms = new Label(housing.getBedrooms().toString() + " Bedrooms");
		Label airAconditioner = new Label();
		airAconditioner.setCaption(" Air Aconditioner");
		airAconditioner.setIcon(housing.getAirConditioner()? VaadinIcons.CHECK_CIRCLE_O : VaadinIcons.CLOSE_CIRCLE_O);
		airAconditioner.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
			
		//Calculamos el precio mas bajo de entre las disponibilidades
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
		
		//reserveDatePicker.setRangeStart(startDate);
		//tuningDateField.
		MultiSelectDateField multiSelectDateField = new MultiSelectDateField();
		addComponents(name, city, features, description, tuningDateField, multiSelectDateField);
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
