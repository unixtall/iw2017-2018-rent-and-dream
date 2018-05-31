package es.uca.iw.rentAndDream.components;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.vaadin.teemu.ratingstars.RatingStars;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.server.FileResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.Utils.HorizontalItemLayout;
import es.uca.iw.rentAndDream.entities.Availability;
import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.services.HousingService;

@SpringComponent
@UIScope
public class HousingSearch extends HorizontalItemLayout{

	private HousingService housingService;
	private CitySearchForm citySearchForm;
	
	private Binder<HousingSearch> binder = new Binder<HousingSearch>(HousingSearch.class);
	
	private LocalDate _startDate;
	private LocalDate _endDate;
	private Integer _guests;
	private List<Housing> _results;	
	
	private DateField startDate;
	private DateField endDate;
	private ComboBox<Integer> guests;
	public Button searchButton;
	
	//Para mostrar el estado de validacion
	Label validationStatus = new Label();
	
	private VerticalLayout form;

	//Inyectamos la dependencia housingSearch
	@Autowired
	public HousingSearch(HousingService housingService, CitySearchForm citySearchForm) {
		super();
		this.housingService = housingService;
		this.citySearchForm = citySearchForm;
		
		this.startDate = new DateField();
		this.endDate = new DateField();
		this.guests = new ComboBox<>(null, IntStream.range(1, 31).mapToObj(i -> i).collect(Collectors.toList()));
		this.searchButton = new Button("Search");
		this._results = new ArrayList<Housing>();
		this.form = new VerticalLayout();
	}

	@PostConstruct
	void init(){

		citySearchForm.setMargin(false);
		startDate.setValue(LocalDate.now());
		startDate.setPlaceholder("Entry Date");
		startDate.setRangeStart(LocalDate.now());
		endDate.setValue(LocalDate.now().plusDays(1));
		endDate.setPlaceholder("End date");
		endDate.setRangeStart(LocalDate.now().plusDays(1));
		guests.setPlaceholder("Number of guests");
        searchButton.setEnabled(false);
		
        form.addComponents(citySearchForm, startDate, endDate, guests, searchButton);
		
	    binder.forField(startDate)
	    	.withValidator(new DateRangeValidator("The entry date is previous today", LocalDate.now(), null))
	    	.asRequired("Is Required")
	    	.bind(s -> this._startDate, (s, v) -> this._startDate = v);
	    binder.forField(endDate)
	    	.withValidator(new DateRangeValidator("The end date is the same day or previous at entry Date", 
	    			startDate.getValue().plusDays(1), null))
	    	.asRequired("Is Required")
	    	.bind(s -> this._endDate, (s, v) -> this._endDate = v);
	    binder.forField(guests)
	    	.asRequired("Is Required")
	    	.bind(s -> this._guests, (s, v) -> this._guests = v);
		
        binder.setStatusLabel(validationStatus);
        binder.bindInstanceFields(this);
        
        binder.addStatusChangeListener(
        		event -> searchButton.setEnabled(citySearchForm.getBinder().isValid() && binder.isValid())
        );
        		
        citySearchForm.getBinder().addStatusChangeListener(
        		event -> searchButton.setEnabled(citySearchForm.getBinder().isValid() && binder.isValid())
        );

        
		this.searchButton.addClickListener(event -> 
		{
			removeAllComponents();
			addComponent(form);
			List<Housing> results = housingService.findByCityidAndAvailabilityAndGuest
					(citySearchForm.getCity().getValue(), startDate.getValue(), endDate.getValue(), guests.getValue());
			results.forEach(e -> 
				addComponent(new HousingSearchResults(e))
			);
			
			if(results.size() == 0)
			{
				addComponent(new Label("we dont find nothing with this filter parameters =("));
			}
		});
		
		addComponent(form);
	}
	
	public interface ChangeHandler {

		void onChange();
	}
	
	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		this.searchButton.addClickListener(e -> h.onChange());
	}
}

final class HousingSearchResults extends VerticalLayout {
	
	Housing housing;
	
	public HousingSearchResults(Housing housing)
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

		addComponent(new Label("From " + fromPrice + "€ per Night"));
		
		RatingStars calification = new RatingStars();
		calification.setValue(housing.getAssessment());
		calification.setReadOnly(true);
		
		addComponent(calification);
		
		setSizeFull();
		
		//popup with a  resume of offer
		
		this.addLayoutClickListener(event -> UI.getCurrent().getNavigator().navigateTo("housingInfoView/" + String.valueOf(housing.getId())));
	
	}

	private void addComponent(Object setValue) {
		// TODO Auto-generated method stub
		
	}
}