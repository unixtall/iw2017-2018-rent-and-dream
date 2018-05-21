package es.uca.iw.rentAndDream.Utils;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;

import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.uca.iw.rentAndDream.availabilities.Availability;
import es.uca.iw.rentAndDream.availabilities.AvailabilityEditor;
import es.uca.iw.rentAndDream.housing.Housing;
import es.uca.iw.rentAndDream.housing.HousingEditor;

public class HousingPreview extends VerticalLayout {
	
	private Housing housing;
	private HousingEditor housingEditor;
	private AvailabilityEditor availabilityEditor;
	
	private Button editButton = new Button("Edit");
	private Button availabilityButton = new Button("Availabilities");
	
	public HousingPreview(Housing housing, HousingEditor housingEditor, AvailabilityEditor availabilityEditor)
	{
		this.housing = housing;
		this.housingEditor = housingEditor;
		this.availabilityEditor = availabilityEditor;
		this.availabilityButton = availabilityButton;
		
		// Image as a file resource

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
		setSpacing(true);
		//hay que añadir el atributo tipo
		addComponent(image);
		addComponent(new Label("Name: " + housing.getName()));
		addComponents(new CssLayout(editButton, availabilityButton));
		addComponent(new Label("Number of reserves: " + housing.getReserve().size()));
		
		setSizeFull();

		//damos funcionalidad al boton de editar creando una ventana al editor

		
		editButton.addClickListener(e -> {
			Window window = new WindowManager("Housing management", this.housingEditor).getWindow();
			housingEditor.editHousing(housing);
			
			//cerramos la ventana cuando haya un cambio
			housingEditor.setChangeHandler(()-> {
				window.close();	
			});
		});
		
		availabilityButton.addClickListener(e -> {
			
			Window window = new WindowManager("Availability management", new CssLayout(this.availabilityEditor)).getWindow();
			availabilityEditor.editAvailability(new Availability(null, null, 0f, housing));
			/*Window window = new WindowManager("Availability management", this.availabilityEditor).getWindow();
			availabilityEditor.editAvailability(new Availability(null, null, 0f, housing));*/
			
			//cerramos la ventana cuando haya un cambio
			housingEditor.setChangeHandler(()-> {
				window.close();	
			});
		});
	
	}
}