package es.uca.iw.rentAndDream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.rentAndDream.Utils.HorizontalItemLayout;
import es.uca.iw.rentAndDream.Utils.WindowManager;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.entities.UserRoleType;
import es.uca.iw.rentAndDream.security.SecurityUtils;
import es.uca.iw.rentAndDream.services.LoginService;
import es.uca.iw.rentAndDream.views.AvailabilityManagementView;
import es.uca.iw.rentAndDream.views.AvailabilityUserView;
import es.uca.iw.rentAndDream.views.CityManagementView;
import es.uca.iw.rentAndDream.views.CountryManagementView;
import es.uca.iw.rentAndDream.views.HousingManagementView;
import es.uca.iw.rentAndDream.views.HousingSearchView;
import es.uca.iw.rentAndDream.views.HousingUserView;
import es.uca.iw.rentAndDream.views.ReserveManagementView;
import es.uca.iw.rentAndDream.views.UserEditProfileView;
import es.uca.iw.rentAndDream.views.UserManagementView;
import es.uca.iw.rentAndDream.views.UserRegisterView;
import es.uca.iw.rentAndDream.views.UserView;

@SpringViewDisplay
public class MainScreen extends VerticalLayout implements ViewDisplay {

	private Panel springViewDisplay;
	
	private LoginService loginService;
	
	@Override
    public void attach() {
        super.attach();
        if(SecurityUtils.hasRole(UserRoleType.ADMIN))
        	this.getUI().getNavigator().navigateTo("userManagementView");
        else
        if(SecurityUtils.hasRole(UserRoleType.MANAGER))
        	this.getUI().getNavigator().navigateTo("reserveManagementView");
        else 
        	this.getUI().getNavigator().navigateTo("housingSearchView");
    }
	
	@Autowired
	public MainScreen(LoginService loginService)
	{
		this.loginService = loginService;
	}
	
	@PostConstruct
	void init() {		
        
		final CssLayout root = new CssLayout();
		root.setSizeFull();
		//root.setMargin(false);

		// Creamos la barra de navegación
		final CssLayout navigationBar = new CssLayout();
		navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		// añadimos elementos al menu segun los roles de usuario
		if(SecurityUtils.hasRole(UserRoleType.GUEST)) 
			addGuestMenu(navigationBar);
		else
		if(SecurityUtils.hasRole(UserRoleType.USER)) 
			addRegisterUserMenu(navigationBar);
		else
		if(SecurityUtils.hasRole(UserRoleType.MANAGER)) 
			addManagerMenu(navigationBar);
		else
		if(SecurityUtils.hasRole(UserRoleType.ADMIN)) 
			addAdministrationMenu(navigationBar);
	
		root.addComponent(navigationBar);

		// Creamos el panel
		springViewDisplay = new Panel();
		springViewDisplay.setSizeFull();
		springViewDisplay.setStyleName(ValoTheme.PANEL_BORDERLESS);
		root.addComponent(springViewDisplay);
		//root.setExpandRatio(springViewDisplay, 1.0f);
		
		addComponent(root);
		setMargin(false);
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
		//navigationBar.addComponent(createNavigationButton("test", WelcomeView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Search homes", HousingSearchView.VIEW_NAME));
		
		//navigationBar.addComponent(createNavigationButton("Welcome", WelcomeView.VIEW_NAME));
	
		//navigationBar.addComponent(createNavigationButton("Login", LoginScreen.VIEW_NAME));	
		HorizontalItemLayout horizontalItemLayout = new HorizontalItemLayout();
		horizontalItemLayout.addComponent(loginService.getLoginLayout());
		
		Button loginButton = new Button("login", e -> 
			new WindowManager("Housing management", horizontalItemLayout)
		);
		loginButton.setStyleName(ValoTheme.BUTTON_SMALL);
		
		navigationBar.addComponent(loginButton);

		navigationBar.addComponent(createNavigationButton("User Registration", UserRegisterView.VIEW_NAME));
		
	}
	
	public void addRegisterUserMenu(CssLayout navigationBar)
	{
		//System.out.println(VaadinService.getCurrentRequest().getWrappedSession().getAttribute(User.class.getName()).toString());
		//navigationBar.addComponent(createNavigationButton("Welcome", WelcomeView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Search homes", HousingSearchView.VIEW_NAME));

		navigationBar.addComponent(createNavigationButton(VaadinService.getCurrentRequest()
				.getWrappedSession().getAttribute(User.class.getName()).toString(), UserEditProfileView.VIEW_NAME));
		
		//navigationBar.addComponent(new Label(getUI().getSession().getAttribute(User.class).toString()));

		Button logoutButton = new Button("Logout", event -> logout());
		logoutButton.addStyleName(ValoTheme.BUTTON_SMALL);
		navigationBar.addComponent(logoutButton);
		navigationBar.addComponent(createNavigationButton("HousingManagement", HousingUserView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("AvailabilityManagement", AvailabilityUserView.VIEW_NAME));
		
	}
	
	public void addManagerMenu(CssLayout navigationBar)
	{
		navigationBar.addComponent(createNavigationButton("Users", UserView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton(VaadinService.getCurrentRequest().
				getWrappedSession().getAttribute(User.class.getName()).toString(), UserEditProfileView.VIEW_NAME));
		
		Button logoutButton = new Button("Logout", event -> logout());
		logoutButton.addStyleName(ValoTheme.BUTTON_SMALL);
		navigationBar.addComponent(logoutButton);
	}
	
	private void logout() {
		getUI().getPage().reload();
		getSession().close();
	}
}