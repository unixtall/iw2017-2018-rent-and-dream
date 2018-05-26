package es.uca.iw.rentAndDream.components;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;

import com.vaadin.server.FileResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.uca.iw.rentAndDream.Utils.HorizontalItemLayout;
import es.uca.iw.rentAndDream.Utils.WindowManager;
import es.uca.iw.rentAndDream.entities.Housing;


@SpringComponent
@Scope("prototype")
public class HousingUserPreview extends HorizontalItemLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HousingEditForm housingEditForm;
	@Autowired
	public HousingUserPreview(HousingEditForm housingEditForm)
	{
		this.housingEditForm = housingEditForm;
	}
	
	public void setHousingList(List<Housing> housingList) {
		
		this.removeAllComponents();
		housingList.forEach( h -> {
		
			VerticalLayout previewLayout = new VerticalLayout();
			
			Button editButton = new Button("Edit");
			
			FileResource resource = null;
			ClassPathResource file = new ClassPathResource("images/foto1.jpg");
			try {
				resource = new FileResource(file.getFile());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
	
			// Show the image in the application
			Image image = new Image(null, resource);
			image.setWidth("240px");

			//hay que añadir el atributo tipo
			previewLayout.addComponent(image);
			previewLayout.addComponent(new Label("Name: " + h.getName()));
			previewLayout.addComponent(new CssLayout(editButton));
			previewLayout.addComponent(new Label("Number of reserves: " + h.getReserve().size()));
			previewLayout.setSizeFull();
			addComponent(previewLayout);
			
			//damos funcionalidad al boton de editar creando una ventana al editor
			
			editButton.addClickListener(e -> {
				
				housingEditForm.setHousing(h);
				
				Window window = new WindowManager("Housing management", housingEditForm).getWindow();
				
				housingEditForm.getSave().addClickListener(event -> {
					
					window.close();
				});
				
				housingEditForm.getDelete().addClickListener(event -> {
					window.close();
				});
			});	
		});
	}
	
	public interface ChangeHandler {

		void onChange();
	}
	
	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		housingEditForm.getSave().addClickListener(e -> h.onChange());
		housingEditForm.getDelete().addClickListener(e -> h.onChange());
	}
}