/**
 * 
 */
package es.uca.iw.rentAndDream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author ruizrube
 *
 */
@SpringView(name = WelcomeView.VIEW_NAME)
public class WelcomeView extends CssLayout implements View {
	public static final String VIEW_NAME = "welcomeView";

	@PostConstruct
	void init() {
		
        CssLayout sample = new CssLayout();
        //sample.addStyleName("outlined");
        //sample.setHeight(100.0f, Unit.PERCENTAGE);
 
        sample.addComponent(new CssLayout(new VerticalLayout(new Label("Menu"), new Label("menu"), new Label("menu"))));
        
        for (int i = 1; i <= 10; i++) {
            final CssLayout child = new CssLayout(new VerticalLayout(new Label("cosa" + i), new Label("cosa"), new Label("cosa")));
            //child.addStyleName("childcomponent");
            //child.setSizeUndefined();
            sample.addComponent(child);
        }
        
		addComponent(sample);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
