package es.uca.iw.rentAndDream.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.entities.Transaction;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.services.TransactionService;

@SpringView(name  = UserTransactionManagementView.VIEW_NAME)
public class UserTransactionManagementView extends VerticalLayout implements View {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "userTransactionManagementView";
	
	private Grid<Transaction> grid;
	private TextField filter;
	private final TransactionService service;
	
	@Autowired
	public UserTransactionManagementView(TransactionService service) {
		this.service = service;
		this.grid = new Grid<>(Transaction.class);
		this.filter = new TextField();

	}

	
	@PostConstruct
	void init() {
		
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter);
	
		addComponents(actions, grid);

		grid.setSizeFull();
		grid.setColumns("id","reserve","host","guest", "type", "dateTime", "invoice", "amount");
		grid.getColumn("invoice").setWidth(90);
		filter.setPlaceholder("Filter by id");

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listTransaction(e.getValue()));

		// Initialize listing
		listTransaction(null);

	}

	private void listTransaction(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(service.findAllMyTransactions((User)VaadinService.getCurrentRequest()
				    .getWrappedSession().getAttribute(User.class.getName())));
		} else {
			grid.setItems(service.findOneWithGuestAndHostAndReserve(Long.parseLong(filterText)));
		}
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
