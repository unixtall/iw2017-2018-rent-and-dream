package es.uca.iw.rentAndDream.housing;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.uca.iw.rentAndDream.Utils.Offer;

@SpringView(name = HousingSearchView.VIEW_NAME)
public class HousingSearchView extends CssLayout implements View{
	
	public static final String VIEW_NAME = "housingSearchView";
	
	//Reservamos memoria para la dependencia housingSearch
	private HousingSearch housingSearch;

	//Inyectamos la dependencia housingSearch
	@Autowired
	public HousingSearchView(HousingSearch housingSearch) {
		this.housingSearch = housingSearch;
	}
	
	@PostConstruct
	void init(){
		
		addComponent(housingSearch);
		
		//Cargamos la busqueda inicial
		housingSearch.getResults().forEach(e -> 
			addComponent(new Offer(e))
		);	
		
		//cada vez que el componente housingSearch se cambia
		housingSearch.setChangeHandler(() -> {
			this.removeAllComponents();
			addComponent(housingSearch);
			housingSearch.getResults().forEach(e -> 
				addComponent(new Offer(e))
			);	
		});
	
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}
}