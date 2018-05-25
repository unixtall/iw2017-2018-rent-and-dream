package es.uca.iw.rentAndDream.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.uca.iw.rentAndDream.Utils.WindowManager;
import es.uca.iw.rentAndDream.components.HousingEditForm;
import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.services.HousingService;

@UIScope
@SpringView(name = HousingManagementView.VIEW_NAME)
public class HousingManagementView extends VerticalLayout implements View {
	public static final String VIEW_NAME = "housingManagementView";

	private Grid<Housing> grid;
	private TextField filter;
	private Button addNewBtn;

	private final HousingService housingService;
	private HousingEditForm housingEditForm;

	@Autowired
	public HousingManagementView(HousingService service, HousingEditForm housingEditForm) {
		this.housingService = service;
		this.housingEditForm = housingEditForm;
		this.grid = new Grid<>(Housing.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("New Housing", FontAwesome.PLUS);
	    
	}
	
	@PostConstruct
	void init() {
		
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		
		addComponents(actions, grid);

		//grid.setHeight(300, Unit.PIXELS);
		grid.setSizeFull();
		grid.setColumns("id", "city", "name", "address", "assessment", "description", "bedrooms", "beds", "airConditioner");

		filter.setPlaceholder("Filter by name");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listHousing(e.getValue()));

		// Connect selected User to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			//editor.editHousing(e.getValue());
			if(e.getValue() != null)
			{
				housingEditForm.setHousing(e.getValue());
				Window window = new WindowManager("Housing Edit", housingEditForm).getWindow();
				window.addCloseListener(evt -> listHousing(null) );
				
				housingEditForm.getSave().addClickListener(event -> 
				{
						
					 listHousing(this.filter.getValue());
					 window.close();
				});
				
				housingEditForm.getDelete().addClickListener(event -> 
				{
						
					 listHousing(this.filter.getValue());
					 window.close();
				});
			}
			
		});

		// Instantiate and edit new User the new button is clicked
		addNewBtn.addClickListener(e ->{
			housingEditForm.setHousing(new Housing("", "", 0f, "", 0, 0, false));
			Window window = new WindowManager("Housing Edit", housingEditForm).getWindow();
			window.addCloseListener(evt -> listHousing(null) );
		});

		// Listen changes made by the editor, refresh data from backend
		/*editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listHousing(filter.getValue());
		});*/

		// Initialize listing
		listHousing(null);

	}

	private void listHousing(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(housingService.findAll());
		} else {
			grid.setItems(housingService.findByNameStartsWithIgnoreCase(filterText));
		}
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}