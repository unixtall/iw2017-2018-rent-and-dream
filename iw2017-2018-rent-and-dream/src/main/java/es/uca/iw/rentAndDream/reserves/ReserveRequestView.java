package es.uca.iw.rentAndDream.reserves;

import java.time.LocalDate;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.Editor;

import es.uca.iw.rentAndDream.Utils.HorizontalItemLayout;
import es.uca.iw.rentAndDream.Utils.HousingPreview;
import es.uca.iw.rentAndDream.housing.Housing;
import es.uca.iw.rentAndDream.users.UserService;

@SpringView(name = ReserveRequestView.VIEW_NAME)
public class ReserveRequestView extends CssLayout implements View {
	public static final String VIEW_NAME = "reserveRequestView";
	
	private HorizontalItemLayout horizontalItemLayout;
	
	private final ReserveService reserveService;
	private final UserService userService;

	@Autowired
	public ReserveRequestView(ReserveService reserveService, UserService userService) {
		this.reserveService = reserveService;
		this.userService = userService;
		this.horizontalItemLayout = new HorizontalItemLayout();
	}
	
	@PostConstruct
	void init() {
		
		// build layout		
		addComponent(horizontalItemLayout);

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		//filter.setValueChangeMode(ValueChangeMode.LAZY);
		//filter.addValueChangeListener(e -> listReserve(Long.getLong(e.getValue())));

		// Connect selected User to editor or hide if none is selected
		//grid.asSingleSelect().addValueChangeListener(e -> {
			//reserveEditor.editReserve(e.getValue());
		//});

		// Instantiate and edit new User the new button is clicked
		//addNewBtn.addClickListener(e -> editor.editReserve(new Reserve(0, null, null, 0f, null)));

		// Listen changes made by the editor, refresh data from backend
		//reserveEditor.setChangeHandler(() -> {
			//reserveEditor.setVisible(false);
			//listReserve(Long.getLong(filter.getValue()));
		//});

		// Initialize listing
		listReserve(null);

	}

	private void listReserve(Long filterText) {
		if (StringUtils.isEmpty(filterText)) {
			reserveService.findByUserAndStatus(userService.findOne(1L), TypeReserveStatus.PENDING)
				.forEach(e -> horizontalItemLayout.addComponent(new HousingPreview(e)));
		} else {
			//grid.setItems(service.findByIdStartsWithIgnoreCase(filterText));
		}
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
