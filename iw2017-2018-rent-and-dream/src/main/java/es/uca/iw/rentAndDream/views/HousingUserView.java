package es.uca.iw.rentAndDream.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Window;

import es.uca.iw.rentAndDream.Utils.HorizontalItemLayout;
import es.uca.iw.rentAndDream.Utils.WindowManager;
import es.uca.iw.rentAndDream.components.HousingEditForm;
import es.uca.iw.rentAndDream.components.HousingUserPreview;
import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.services.HousingService;


@SpringView(name = HousingUserView.VIEW_NAME)
public class HousingUserView extends CssLayout implements View {
	public static final String VIEW_NAME = "housingUserView";
	
	private HorizontalItemLayout horizontalItemLayout;
	private final HousingService housingService;
	private HousingEditForm housingEditForm;
	private HousingUserPreview housingUserPreview;

	Button addNew = new Button("Add new housing");

	@Autowired
	public HousingUserView(HousingService housingService, HousingEditForm housingEditForm, HousingUserPreview housingPreview) {
		this.horizontalItemLayout = new HorizontalItemLayout();
		this.housingService = housingService;
		this.housingEditForm = housingEditForm;
		this.housingUserPreview = housingPreview;
	}
	
	@PostConstruct
	void init() {
		
		// build layout		
		addComponent(horizontalItemLayout);
		
		horizontalItemLayout.addComponent(addNew);
		
		
		//listado inicial de casas housing preview de casas del usuario

		housingUserPreview.setHousingList(housingService.findByUser((User)VaadinService.getCurrentRequest()
				.getWrappedSession().getAttribute(User.class.getName())));
		
		horizontalItemLayout.addComponent(housingUserPreview);
		
		housingUserPreview.setChangeHandler(() -> {
			
			//horizontalItemLayout.removeComponent(housingPreview);
			housingUserPreview.setHousingList(housingService.findByUser((User)VaadinService.getCurrentRequest()
					.getWrappedSession().getAttribute(User.class.getName())));
			horizontalItemLayout.addComponent(housingUserPreview);
		});
		
		//Boton para añadir nuevo
		addNew.addClickListener(e -> {
			
			housingEditForm.setHousing(new Housing("", "", 0f, "", 0, 0, false ,null, null));
			housingEditForm.setUser((User)VaadinService.getCurrentRequest().getWrappedSession().getAttribute(User.class.getName()));

			Window window = new WindowManager("Housing management", housingEditForm).getWindow();

			housingEditForm.getSave().addClickListener(event -> 
			{
				housingUserPreview.setHousingList(housingService.findByUser((User)VaadinService.getCurrentRequest()
						.getWrappedSession().getAttribute(User.class.getName())));
				horizontalItemLayout.addComponent(housingUserPreview);
				window.close();
			});
	
		});

		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
