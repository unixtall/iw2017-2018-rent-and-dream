package es.uca.iw.rentAndDream.services;

import java.time.LocalDate;
import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.entities.Availability;
import es.uca.iw.rentAndDream.entities.City;
import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.entities.UserRoleType;
import es.uca.iw.rentAndDream.repositories.HousingRepository;
import es.uca.iw.rentAndDream.security.SecurityUtils;
import es.uca.iw.rentAndDream.templates.AvailabilityEditForm;
import es.uca.iw.rentAndDream.templates.CitySearchForm;
import es.uca.iw.rentAndDream.templates.HousingEditForm;

@Service
public class HousingService {

	@Autowired
	private HousingRepository repo;
	
	@Autowired
	private CityService cityService;
	
	
	/*@Autowired
	private HousingEditor housingEditor;
	
	@Autowired
	private AvailabilityEditor availabilityEditor;*/
	

	public Housing loadHousingByName(String name) throws NameNotFoundException {

		Housing housing = repo.findByName(name);
		if (housing == null) {
			throw new NameNotFoundException(name);
		}
		return housing;
	}

	public Housing save(Housing housing) {
		return repo.save(housing);
	}

	public List<Housing> findByNameStartsWithIgnoreCase(String name) {
		return repo.findByNameStartsWithIgnoreCase(name);
	}
	
	public Housing findOne(Long arg0) {
		return repo.findOne(arg0);
	}
	
	public Housing findOneWithAvailability(Long id)
	{
		return repo.findOneWithAvailabilityAndCity(id);
	}
	

	public void delete(Housing arg0) {
		repo.delete(arg0);
	}

	public List<Housing> findAll() {
		return repo.findAll();
	}
	
	@Transactional
	public List<Housing> findByCityidAndAvailabilityAndGuest(City city, LocalDate startDate, LocalDate endDate, Integer guests)
	{
		return repo.findByCityidAndAvailabilityAndGuest(city, startDate, endDate, guests);
		
	}
	
	@Transactional
	public List<Housing> findByUser(User user)
	{
		List<Housing> housing = repo.findByUser(user);
		housing.forEach(e ->{
			e.getReserve();
			e.getAvailability();
			
		});
		return housing;
	}
	/*
	public List<HousingPreview> getLoginUserListOfHousingPreview(User user)
	{
		List<HousingPreview> housingPreview = new ArrayList<HousingPreview>();
		
		findByUser(user)
			.forEach(e -> {
				//housingPreview.add(new HousingPreview(e, housingEditor, availabilityEditor));
			}
		);
		
		return housingPreview;
	}
	*/
	
	
	public VerticalLayout getEditForm(Housing housing)
	{
		CitySearchForm citySearchForm = cityService.getCitySearchForm();
		HousingEditForm editForm = new HousingEditForm(housing);
		citySearchForm.addComponent(editForm);
		
		if(housing.getCity() != null)
			citySearchForm.getCity().setValue(housing.getCity());
		
		citySearchForm.getBinder().addValueChangeListener(e-> {
			editForm.getSave().setEnabled(editForm.getBinder().isValid() && citySearchForm.getBinder().isValid());
			housing.setCity(citySearchForm.getCity().getValue());
		});
		
		editForm.getBinder().addValueChangeListener(e-> 
			editForm.getSave().setEnabled(editForm.getBinder().isValid() && citySearchForm.getBinder().isValid())
		);
		
		
		editForm.getSave().addClickListener(event-> {
			save(editForm.getBinder().getBean());
			Notification.show("Change sucessfull");
		});
		
		editForm.getDelete().addClickListener(event-> {
			delete(editForm.getBinder().getBean());
			Notification.show("Change sucessfull");
			editForm.getBinder().removeBean();
			editForm.getDelete().setEnabled(false);
		});
		return citySearchForm;
	}
	
	
}