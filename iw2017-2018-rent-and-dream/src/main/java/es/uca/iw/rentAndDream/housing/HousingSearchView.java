package es.uca.iw.rentAndDream.housing;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;

import es.uca.iw.rentAndDream.Utils.HorizontalItemLayout;
import es.uca.iw.rentAndDream.Utils.Offer;
import es.uca.iw.rentAndDream.cities.CityService;
import es.uca.iw.rentAndDream.countries.CountryService;
import es.uca.iw.rentAndDream.regions.RegionService;

@SpringView(name = HousingSearchView.VIEW_NAME)
public class HousingSearchView extends CssLayout implements View{
	
	public static final String VIEW_NAME = "housingSearchView";
	
	//Reservamos memoria para la dependencia housingSearch
	private HousingSearch housingSearch;
	
	@Autowired
	private CityService cityService;
	@Autowired
	private RegionService regionService;
	@Autowired
	private CountryService countryService;
	@Autowired
	private HousingService housingService;
	
	private HorizontalItemLayout horizontalItemLayout;

	//Inyectamos la dependencia housingSearch
	
	public HousingSearchView(CityService cityService, RegionService regionService, CountryService countryService,
			HousingService housingService) {
		super();
		this.cityService = cityService;
		this.regionService = regionService;
		this.countryService = countryService;
		this.housingService = housingService;
		this.housingSearch = new HousingSearch(cityService, regionService, countryService, housingService);
		this.horizontalItemLayout = new HorizontalItemLayout();
		horizontalItemLayout.addComponent(housingSearch);
		
		addComponent(horizontalItemLayout);
		
		//Cargamos la busqueda inicial
		housingSearch.getResults().forEach(e -> 
			horizontalItemLayout.addComponent(new Offer(e))
		);	
	}

	@PostConstruct
	void init(){
		
		//cada vez que el componente housingSearch se cambia
		housingSearch.setChangeHandler(() -> {
			horizontalItemLayout.removeAllComponents();
			horizontalItemLayout.addComponent(housingSearch);
			housingSearch.getResults().forEach(e -> 
				horizontalItemLayout.addComponent(new Offer(e))
			);	
		});
	
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}
}