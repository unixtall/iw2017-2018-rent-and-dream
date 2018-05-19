package es.uca.iw.rentAndDream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

import es.uca.iw.rentAndDream.security.AccessDeniedView;
import es.uca.iw.rentAndDream.security.ErrorView;
import es.uca.iw.rentAndDream.users.User;

@SpringUI
public class VaadinUI extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	SpringViewProvider viewProvider;

	@Autowired
    MainScreen mainScreen;
	
	User user;
	
	@Override
	protected void init(VaadinRequest request) {

	   	this.getUI().getNavigator().setErrorView(ErrorView.class);
		viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
		

		
		setContent(mainScreen);

	}
}