package es.uca.iw.rentAndDream;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.rentAndDream.availabilities.AvailabilityManagementView;
import es.uca.iw.rentAndDream.cities.CityManagementView;
import es.uca.iw.rentAndDream.countries.CountryManagementView;
import es.uca.iw.rentAndDream.housing.HousingManagementView;
import es.uca.iw.rentAndDream.housing.HousingSearchView;
import es.uca.iw.rentAndDream.reserves.ReserveManagementView;
import es.uca.iw.rentAndDream.security.LoginScreen;
import es.uca.iw.rentAndDream.security.SecurityUtils;
import es.uca.iw.rentAndDream.security.UserRegisterScreen;
import es.uca.iw.rentAndDream.users.RoleType;
import es.uca.iw.rentAndDream.users.UserManagementView;
import es.uca.iw.rentAndDream.users.UserView;

@SpringViewDisplay
public class MainScreen extends VerticalLayout implements ViewDisplay {

	private Panel springViewDisplay;
	
	@Override
    public void attach() {
        super.attach();
        this.getUI().getNavigator().navigateTo("");
    }
	
	@PostConstruct
	void init() {

		final VerticalLayout root = new VerticalLayout();
		root.setSizeFull();
		
		// Creamos la cabecera 
		//root.addComponent(new Label("This is the session: " + VaadinSession.getCurrent()));
		//root.addComponent(new Label("This is the UI: " + this.toString()));

		// Creamos la barra de navegación
		final CssLayout navigationBar = new CssLayout();
		navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		// añadimos elementos al menu segun los roles de usuario
		if(SecurityUtils.hasRole(RoleType.GUEST)) 
			addGuestMenu(navigationBar);
		else
		if(SecurityUtils.hasRole(RoleType.USER)) 
			addRegisterUserMenu(navigationBar);
		else
		if(SecurityUtils.hasRole(RoleType.MANAGER)) 
			addManagerMenu(navigationBar);
		else
		if(SecurityUtils.hasRole(RoleType.ADMIN)) 
			addAdministrationMenu(navigationBar);
	
		root.addComponent(navigationBar);

		// Creamos el panel
		springViewDisplay = new Panel();
		springViewDisplay.setSizeFull();
		root.addComponent(springViewDisplay);
		root.setExpandRatio(springViewDisplay, 1.0f);

		addComponent(root);
		
	}

	private Button createNavigationButton(String caption, final String viewName) {
		Button button = new Button(caption);
		button.addStyleName(ValoTheme.BUTTON_SMALL);
		// If you didn't choose Java 8 when creating the project, convert this
		// to an anonymous listener class
		button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
		return button;
	}
	
	
	@Override
	public void showView(View view) {
		springViewDisplay.setContent((Component) view);
	}

	public void addAdministrationMenu(CssLayout navigationBar)
	{
		navigationBar.addComponent(createNavigationButton("User Management", UserManagementView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Housing Management", HousingManagementView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Country Management", CountryManagementView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("City Management", CityManagementView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Reserve Management", ReserveManagementView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Availability Management", AvailabilityManagementView.VIEW_NAME));


		Button logoutButton = new Button("Logout", event -> logout());
		logoutButton.addStyleName(ValoTheme.BUTTON_SMALL);
		navigationBar.addComponent(logoutButton);
	}
	
	public void addGuestMenu(CssLayout navigationBar)
	{
		navigationBar.addComponent(createNavigationButton("Welcome", WelcomeView.VIEW_NAME));
	
		navigationBar.addComponent(createNavigationButton("Login", LoginScreen.VIEW_NAME));	

		navigationBar.addComponent(createNavigationButton("Search your housing", HousingSearchView.VIEW_NAME));
		
		navigationBar.addComponent(createNavigationButton("User Registration", UserRegisterScreen.VIEW_NAME));
	}
	
	public void addRegisterUserMenu(CssLayout navigationBar)
	{
		navigationBar.addComponent(createNavigationButton("Welcome", WelcomeView.VIEW_NAME));
		
		Button logoutButton = new Button("Logout", event -> logout());
		logoutButton.addStyleName(ValoTheme.BUTTON_SMALL);
		navigationBar.addComponent(logoutButton);
	}
	
	public void addManagerMenu(CssLayout navigationBar)
	{
		navigationBar.addComponent(createNavigationButton("Users", UserView.VIEW_NAME));
		
		Button logoutButton = new Button("Logout", event -> logout());
		logoutButton.addStyleName(ValoTheme.BUTTON_SMALL);
		navigationBar.addComponent(logoutButton);
	}
	
	private void logout() {
		getUI().getPage().reload();
		getSession().close();
	}
}
