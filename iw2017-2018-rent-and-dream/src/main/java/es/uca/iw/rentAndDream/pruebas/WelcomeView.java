/**
 * 
 */
package es.uca.iw.rentAndDream.pruebas;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;

/**
 * @author ruizrube
 *
 */
@SpringView(name = WelcomeView.VIEW_NAME)
public class WelcomeView extends CssLayout implements View {
	public static final String VIEW_NAME = "welcomeView";

	@PostConstruct
	void init() {
		
		// Create the model and the Vaadin view implementation
		CalculatorModel    model = new CalculatorModel();
		CalculatorViewImpl view  = new CalculatorViewImpl();

		// The presenter binds the model and view together
		new CalculatorPresenter(model, view);

		// The view implementation is a Vaadin component
		addComponent(view);
       	Notification.show("hola");
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
