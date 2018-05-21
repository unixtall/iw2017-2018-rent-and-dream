package es.uca.iw.rentAndDream.housing;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Window;

import es.uca.iw.rentAndDream.Utils.HorizontalItemLayout;
import es.uca.iw.rentAndDream.Utils.HousingPreview;
import es.uca.iw.rentAndDream.Utils.WindowManager;
import es.uca.iw.rentAndDream.availabilities.AvailabilityEditor;
import es.uca.iw.rentAndDream.reserves.ReserveService;
import es.uca.iw.rentAndDream.users.User;
import es.uca.iw.rentAndDream.users.UserService;

@SpringView(name = HousingUserView.VIEW_NAME)
public class HousingUserView extends CssLayout implements View {
	public static final String VIEW_NAME = "housingUserView";
	
	private HorizontalItemLayout horizontalItemLayout;
	
	private final ReserveService reserveService;
	private final UserService userService;
	private final HousingService housingService;
	private final HousingEditor housingEditor;
	private final AvailabilityEditor availabilityEditor;
	Button addNew = new Button("Add new housing");

	@Autowired
	public HousingUserView(ReserveService reserveService, UserService userService, HousingService housingService
			, HousingEditor housingEditor, AvailabilityEditor availabilityEditor) {
		this.reserveService = reserveService;
		this.userService = userService;
		this.horizontalItemLayout = new HorizontalItemLayout();
		this.housingService = housingService;
		this.housingEditor = housingEditor;
		this.availabilityEditor = availabilityEditor;
	}
	
	@PostConstruct
	void init() {
		
		// build layout		
		addComponent(horizontalItemLayout);

		// Initialize listing
		/*reserveService.findByUserAndStatus((User)VaadinService.getCurrentRequest()
				.getWrappedSession().getAttribute(User.class.getName())
				, TypeReserveStatus.PENDING)
					.forEach(e -> horizontalItemLayout.addComponent(new HousingPreview(e)));*/
		
		horizontalItemLayout.addComponent(addNew);
		
		
		//listado inicial de casas housing preview de casas del usuario
		housingService.findByUser((User)VaadinService.getCurrentRequest()
				.getWrappedSession().getAttribute(User.class.getName()))
			.forEach(e -> {
					HousingPreview housingPreview = new HousingPreview(e, housingEditor, availabilityEditor);
					horizontalItemLayout.addComponent(housingPreview);
			});
		
		
		//Cuando haya cambios en el el item del editor
		housingEditor.setChangeHandler(()->{
			horizontalItemLayout.removeAllComponents();
			horizontalItemLayout.addComponent(addNew);
			housingService.findByUser((User)VaadinService.getCurrentRequest()
					.getWrappedSession().getAttribute(User.class.getName()))
					.forEach(e -> {
						HousingPreview housingPreview = new HousingPreview(e, housingEditor, availabilityEditor);
						horizontalItemLayout.addComponent(housingPreview);
					});
		});

		//Boton para añadir nuevo
		addNew.addClickListener(e -> {
			Window window = new WindowManager("Titulo", this.housingEditor).getWindow();
			

			Housing h = new Housing("", "", 0f, "", 0, 0, false);
			h.setUser((User)VaadinService.getCurrentRequest()
					.getWrappedSession().getAttribute(User.class.getName()));
			housingEditor.editHousing(h);
			
			//cerramos la ventana cuando haya un cambio
			housingEditor.setChangeHandler(()-> {
				window.close();	
				//horizontalItemLayout.addComponent(addNew);
			});
		});

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
