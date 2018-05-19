package es.uca.iw.rentAndDream.users;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

@SpringView(name = UserEditProfileView.VIEW_NAME)
public class UserEditProfileView extends CssLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "userEditProfileView";
	
	private UserEditor editor;
	
	@Autowired
	public UserEditProfileView(UserEditor editor, UserService userService) {
		this.editor = editor;
		editor.editUser(userService.findOne(1L));
	}
	
	@PostConstruct
	void init() {

		addComponent(editor);
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}	
}
