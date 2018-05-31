package es.uca.iw.rentAndDream.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.ratingstars.RatingStars;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.uca.iw.rentAndDream.Utils.WindowManager;
import es.uca.iw.rentAndDream.components.ReserveEditForm;
import es.uca.iw.rentAndDream.entities.Reserve;
import es.uca.iw.rentAndDream.entities.ReserveTypeStatus;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.entities.UserRoleType;
import es.uca.iw.rentAndDream.security.SecurityUtils;
import es.uca.iw.rentAndDream.services.HousingService;
import es.uca.iw.rentAndDream.services.ReserveService;

@SpringView(name = ReserveUserView.VIEW_NAME)
public class ReserveUserView extends VerticalLayout implements View {
	public static final String VIEW_NAME = "reserveUserView";

	private Grid<Reserve> grid;
	private TextField filter;
	private Button addNewBtn;

	private final ReserveService reserveService;
	private final HousingService housingService;
	private ReserveEditForm reserveEditForm;

	@Autowired
	public ReserveUserView(ReserveService reserveService, HousingService housingService, ReserveEditForm reserveEditForm) {
		this.reserveService = reserveService;
		this.housingService = housingService;
		this.reserveEditForm = reserveEditForm;
		this.grid = new Grid<>(Reserve.class);
		this.filter = new TextField();  
		this.addNewBtn = new Button("New Availability", FontAwesome.PLUS);
	}
	
	@PostConstruct
	void init() {
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		
		addNewBtn.setVisible(SecurityUtils.hasRole(UserRoleType.ADMIN) || SecurityUtils.hasRole(UserRoleType.MANAGER));

		addComponents(actions, grid);
		
		//grid.setHeight(300, Unit.PIXELS);
		grid.setSizeFull();
		grid.setColumns("user", "housing", "numberGuests", "entryDate", "departureDate", "price", "status");
		grid.getColumn("user").setCaption("Tenant");
		filter.setPlaceholder("Filter by housing");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listReserve(e.getValue()));

		// Connect selected User to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			if(e.getValue() != null)
			{
				reserveEditForm.setReserve(e.getValue());
				
				if(e.getValue().getStatus() == ReserveTypeStatus.PENDING)
				{
					Button canceledButton = new Button("Cancel reserve");
					
					Window window = new WindowManager("ReserveHost Edit",  canceledButton).getWindow();					
				
					canceledButton.addClickListener(event-> {
						e.getValue().setStatus(ReserveTypeStatus.CANCELEDBYGUEST);
						reserveService.save(e.getValue());
						listReserve(filter.getValue());
						window.close();
					});
				
	
					reserveEditForm.getSave().addClickListener(event->{
						listReserve(filter.getValue());
						window.close();
					});
					reserveEditForm.getDelete().addClickListener(event->{
						listReserve(filter.getValue());
						window.close();
					});
				}

				if(e.getValue().getStatus() == ReserveTypeStatus.CONFIRMED)
				{	
					Button canceledButton = new Button("Cancel reserve");
					Window window = new WindowManager("ReserveHost Edit", canceledButton).getWindow();
					
					canceledButton.addClickListener(event-> {
						ConfirmDialog.show(getUI().getCurrent(), "are you sure? Cancellation policies may apply, your refund is: " 
								+ reserveService.getAmountRefundCancellationPolicy(e.getValue()) * 0.95 + " €", new ConfirmDialog.Listener() {
							
				            public void onClose(ConfirmDialog dialog) {
				            	
								if(dialog.isConfirmed())
								{
									reserveService.cancelByGuest(e.getValue());
									listReserve(filter.getValue());
									window.close();
								}
				            }
				        });
						

					});

				}
				
				if(e.getValue().getStatus() == ReserveTypeStatus.FINISHED && e.getValue().getGuestReport() == null)
				{	
					RichTextArea guestReport = new RichTextArea();
					RatingStars rating = new RatingStars();
					Button submitButton = new Button("Submit comment");
					Window window = new WindowManager("Report problems or evaluate host", new VerticalLayout(guestReport, rating, submitButton)).getWindow();
					
					submitButton.addClickListener(click->{
						e.getValue().setGuestReport(guestReport.getValue());
						if(rating.getValue() != 0)
						{
							e.getValue().getHousing().setAssessmentNumber(e.getValue().getHousing().getAssessmentNumber() + 1);
							e.getValue().setGuestRating(rating.getValue());
							e.getValue().getHousing().setAssessment((housingService.getAssessmentSum(e.getValue().getHousing()) + rating.getValue()) 
									/ e.getValue().getHousing().getAssessmentNumber());
						}
						Notification.show("Comment and evaluate submit successfull");
						housingService.save(e.getValue().getHousing());
						reserveService.save(e.getValue());
						window.close();
					});

				}
				
			}
		});

		// Initialize listing
		listReserve(null);
		
		// Instantiate and edit new User the new button is clicked
		addNewBtn.addClickListener(e ->{
			
			reserveEditForm.setReserve(new Reserve(0, null, null, 0f, null));
			
			Window window = new WindowManager("Reserve Edit", reserveEditForm).getWindow();
			
			reserveEditForm.getSave().addClickListener(event->{
				listReserve(filter.getValue());
				window.close();
			});

			reserveEditForm.getDelete().addClickListener(event->{
				listReserve(filter.getValue());
				window.close();
			});
		});
		

	}

	private void listReserve(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(reserveService.findByGuestUsername(   (VaadinService.getCurrentRequest()
					.getWrappedSession().getAttribute(User.class.getName())).toString()    ));
		} else {
			grid.setItems(reserveService.findByGuestUsernameByHousingName((VaadinService.getCurrentRequest()
					.getWrappedSession().getAttribute(User.class.getName())).toString(), filterText));
		}
	}
	
	
	
	public TextField getFilter() {
		return filter;
	}

	public void setFilter(TextField filter) {
		this.filter = filter;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}

