package es.uca.iw.rentAndDream;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.rentAndDream.Utils.WindowManager;
import es.uca.iw.rentAndDream.components.LoginForm;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.entities.UserRoleType;
import es.uca.iw.rentAndDream.security.SecurityUtils;
import es.uca.iw.rentAndDream.views.AvailabilityManagementView;
import es.uca.iw.rentAndDream.views.AvailabilityUserView;
import es.uca.iw.rentAndDream.views.CityManagementView;
import es.uca.iw.rentAndDream.views.CountryManagementView;
import es.uca.iw.rentAndDream.views.HousingManagementView;
import es.uca.iw.rentAndDream.views.HousingSearchView;
import es.uca.iw.rentAndDream.views.HousingUserView;
import es.uca.iw.rentAndDream.views.ReserveHostView;
import es.uca.iw.rentAndDream.views.ReserveManagementView;
import es.uca.iw.rentAndDream.views.ReserveUserView;
import es.uca.iw.rentAndDream.views.TransactionManagementView;
import es.uca.iw.rentAndDream.views.UserEditProfileView;
import es.uca.iw.rentAndDream.views.UserManagementView;
import es.uca.iw.rentAndDream.views.UserRegisterView;
import es.uca.iw.rentAndDream.views.UserTransactionManagementView;
import es.uca.iw.rentAndDream.views.UserView;

@SpringViewDisplay
public class MainScreen extends VerticalLayout implements ViewDisplay {

	private Panel springViewDisplay;
	
	
	private LoginForm loginForm;
	
	HorizontalLayout headerLayout = new HorizontalLayout();
	
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
	public MainScreen(LoginForm loginForm)
	{
		this.loginForm = loginForm;
	}
	
	@PostConstruct
	void init() {		
        
		final VerticalLayout root = new VerticalLayout();
		//CssLayout logo = new CssLayout();		
		//headerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		//headerLayout.setWidth("15%");
		
		root.setSizeFull();
		root.setMargin(false);
		
		
		// Image as a file resource

		FileResource resource = null;
		ClassPathResource file = new ClassPathResource("images/logo.png");
			try {
				resource = new FileResource(file.getFile());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				

		// Show the image in the application
		Image image = new Image(null, resource);
		image.setWidth("70%");
		HorizontalLayout logoLayout = new HorizontalLayout(image);
		logoLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		logoLayout.setWidth("50%");
		logoLayout.setMargin(false);
		
		headerLayout.addComponent(logoLayout);
		
		headerLayout.setSizeFull();
		headerLayout.setMargin(false);
		
		//Boton Login/logout
		HorizontalLayout loginLogoutLayout = new HorizontalLayout();
		
		Button logoutButton = new Button("Logout", event -> logout());
		logoutButton.addStyleName(ValoTheme.BUTTON_SMALL);
		
		Button loginButton = new Button("Login", e -> 
			new WindowManager("Enter to you account", loginForm)
			);	
		
		if(SecurityUtils.isLoggedIn()) {
			Label username = new Label("Welcome " + VaadinService.getCurrentRequest()
				    .getWrappedSession().getAttribute(User.class.getName()).toString());
			
			
			username.setStyleName(ValoTheme.LABEL_COLORED);
			
			HorizontalLayout logoutLayout = new HorizontalLayout(username, logoutButton);
			logoutLayout.setMargin(false);
			loginLogoutLayout.addComponent(logoutLayout);
		}else
			loginLogoutLayout.addComponent(loginButton);
		
		loginLogoutLayout.setMargin(false);
		
		loginButton.setStyleName(ValoTheme.BUTTON_SMALL);
		headerLayout.addComponent(loginLogoutLayout);
		headerLayout.setComponentAlignment(loginLogoutLayout, Alignment.MIDDLE_RIGHT);
		headerLayout.setMargin(false);
		
		
		//root.setMargin(false);

		// Creamos la barra de navegación
		final CssLayout navigationBar = new CssLayout() {
			
			
		};
		navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		
		root.addComponent(headerLayout);
		root.addComponent(navigationBar);

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


		// Creamos el panel
		springViewDisplay = new Panel();
		springViewDisplay.setSizeFull();
		springViewDisplay.setStyleName(ValoTheme.PANEL_BORDERLESS);
		root.addComponent(springViewDisplay);
		//root.setExpandRatio(springViewDisplay, 1.0f);
		
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
		navigationBar.addComponent(createNavigationButton("Transaction Management", TransactionManagementView.VIEW_NAME));


		Button logoutButton = new Button("Logout", event -> logout());
		logoutButton.addStyleName(ValoTheme.BUTTON_SMALL);
		//navigationBar.addComponent(logoutButton);
	}
	
	public void addGuestMenu(CssLayout navigationBar)
	{
		
		navigationBar.addComponent(createNavigationButton("Search homes", HousingSearchView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("User Registration", UserRegisterView.VIEW_NAME));
		
	}
	
	public void addRegisterUserMenu(CssLayout navigationBar)
	{
		
		//CssLayout welcome = new CssLayout();
		//welcome.addComponent(new Label("Welcome " + VaadinService.getCurrentRequest()
		//		.getWrappedSession().getAttribute(User.class.getName()).toString() + " !"));
		
		navigationBar.addComponent(createNavigationButton("Search homes", HousingSearchView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Edit Profile", UserEditProfileView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Housing Management", HousingUserView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Availability Management", AvailabilityUserView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("My reserves", ReserveUserView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Reserves of my houses", ReserveHostView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("My transactions", UserTransactionManagementView.VIEW_NAME));
		//navigationBar.addComponent(logoutButton);

	}
	
	public void addManagerMenu(CssLayout navigationBar)
	{
		navigationBar.addComponent(createNavigationButton("Users", UserView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Reserve Management", ReserveManagementView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Edit Profile", UserEditProfileView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Transaction Management", TransactionManagementView.VIEW_NAME));

		
		Button logoutButton = new Button("Logout", event -> logout());
		logoutButton.addStyleName(ValoTheme.BUTTON_SMALL);
		//navigationBar.addComponent(logoutButton);
	}
	
	private void logout() {
		getUI().getPage().reload();
		getSession().close();
	}
}