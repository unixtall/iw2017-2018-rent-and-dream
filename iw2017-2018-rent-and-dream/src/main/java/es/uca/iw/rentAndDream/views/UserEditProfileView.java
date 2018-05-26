package es.uca.iw.rentAndDream.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;

import es.uca.iw.rentAndDream.components.UserEditor;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.services.UserService;

@SpringView(name = UserEditProfileView.VIEW_NAME)
public class UserEditProfileView extends HorizontalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "userEditProfileView";
	private UserEditor editor;
	
	@Autowired
	public UserEditProfileView(UserEditor editor, UserService userService) {
		this.editor = editor;


		editor.editUser((User)(VaadinService.getCurrentRequest().getWrappedSession().getAttribute(User.class.getName())));
	}
	
	@PostConstruct
	void init() {
		this.setSizeFull();
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		addComponent(new HorizontalLayout(editor));
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}	
}
