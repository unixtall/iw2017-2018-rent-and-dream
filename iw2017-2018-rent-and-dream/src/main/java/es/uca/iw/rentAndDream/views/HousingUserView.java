package es.uca.iw.rentAndDream.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.uca.iw.rentAndDream.Utils.HorizontalItemLayout;
import es.uca.iw.rentAndDream.Utils.HousingPreview;
import es.uca.iw.rentAndDream.Utils.WindowManager;
import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.services.CityService;
import es.uca.iw.rentAndDream.services.HousingService;
import es.uca.iw.rentAndDream.services.ReserveService;
import es.uca.iw.rentAndDream.services.UserService;

@SpringView(name = HousingUserView.VIEW_NAME)
public class HousingUserView extends CssLayout implements View {
	public static final String VIEW_NAME = "housingUserView";
	
	private HorizontalItemLayout horizontalItemLayout;
	
	private final ReserveService reserveService;
	private final UserService userService;
	private final HousingService housingService;
	private final CityService cityService;
	Button addNew = new Button("Add new housing");

	@Autowired
	public HousingUserView(ReserveService reserveService, UserService userService, HousingService housingService
			, CityService cityService) {
		this.reserveService = reserveService;
		this.userService = userService;
		this.horizontalItemLayout = new HorizontalItemLayout();
		this.housingService = housingService;
		this.cityService = cityService;
	}
	
	@PostConstruct
	void init() {
		
		// build layout		
		addComponent(horizontalItemLayout);
		
		horizontalItemLayout.addComponent(addNew);
		
		
		//listado inicial de casas housing preview de casas del usuario

		housingService.findByUser((User)VaadinService.getCurrentRequest()
				.getWrappedSession().getAttribute(User.class.getName()))
				.forEach(e -> {
					HousingPreview housingPreview = new HousingPreview(e, housingService);
					horizontalItemLayout.addComponent(housingPreview);
				});

		//Boton para añadir nuevo
		addNew.addClickListener(e -> {
			
			VerticalLayout editForm = housingService.getEditForm(new Housing("", "", 0f, "", 0, 0, false
					,(User)VaadinService.getCurrentRequest().getWrappedSession().getAttribute(User.class.getName()), null));
			
			Window window = new WindowManager("Housing management",editForm).getWindow();
			
			window.addCloseListener(event -> reloadOffers());
		});

		
	}

	private void reloadOffers()
	{
		horizontalItemLayout.removeAllComponents();
		horizontalItemLayout.addComponent(addNew);
		housingService.findByUser((User)VaadinService.getCurrentRequest()
				.getWrappedSession().getAttribute(User.class.getName()))
				.forEach(e -> {
					HousingPreview housingPreview = new HousingPreview(e, housingService);
					horizontalItemLayout.addComponent(housingPreview);
				});
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
