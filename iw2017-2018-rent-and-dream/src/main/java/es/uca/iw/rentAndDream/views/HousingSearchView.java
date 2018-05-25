package es.uca.iw.rentAndDream.views;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.CssLayout;

import es.uca.iw.rentAndDream.components.HousingSearch;

@SpringView(name = HousingSearchView.VIEW_NAME)
public class HousingSearchView extends CssLayout implements View{
	
	public static final String VIEW_NAME = "housingSearchView";
	
	private HousingSearch housingSearch;
	
	@Autowired
	public HousingSearchView(HousingSearch housingSearch) {
		super();
		this.housingSearch = housingSearch;
	}

	@PostConstruct
	void init(){
		addComponent(housingSearch);
		
		housingSearch.setChangeHandler(()->{

		addComponent(housingSearch);
		});
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
	}
}