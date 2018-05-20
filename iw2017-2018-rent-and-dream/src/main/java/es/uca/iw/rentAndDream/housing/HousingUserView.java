package es.uca.iw.rentAndDream.housing;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CssLayout;

import es.uca.iw.rentAndDream.Utils.HorizontalItemLayout;
import es.uca.iw.rentAndDream.Utils.HousingPreview;
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

	@Autowired
	public HousingUserView(ReserveService reserveService, UserService userService, HousingService housingService, HousingEditor housingEditor) {
		this.reserveService = reserveService;
		this.userService = userService;
		this.horizontalItemLayout = new HorizontalItemLayout();
		this.housingService = housingService;
		this.housingEditor = housingEditor;
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
		
		housingService.findByUser((User)VaadinService.getCurrentRequest()
				.getWrappedSession().getAttribute(User.class.getName()))
				.forEach(e -> {
					HousingPreview housingPreview = new HousingPreview(e, housingEditor);
					horizontalItemLayout.addComponent(housingPreview);
				});
		
		housingEditor.setChangeHandler(()->{
			horizontalItemLayout.removeAllComponents();
			housingService.findByUser((User)VaadinService.getCurrentRequest()
					.getWrappedSession().getAttribute(User.class.getName()))
					.forEach(e -> {
						HousingPreview housingPreview = new HousingPreview(e, housingEditor);
						horizontalItemLayout.addComponent(housingPreview);
					});
		});

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
