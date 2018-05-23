package es.uca.iw.rentAndDream.services;

import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.rentAndDream.entities.Housing;
import es.uca.iw.rentAndDream.entities.Reserve;
import es.uca.iw.rentAndDream.entities.ReserveTypeStatus;
import es.uca.iw.rentAndDream.entities.User;
import es.uca.iw.rentAndDream.entities.UserRoleType;
import es.uca.iw.rentAndDream.repositories.ReserveRepository;
import es.uca.iw.rentAndDream.security.SecurityUtils;
import es.uca.iw.rentAndDream.templates.ReserveEditForm;

@Service
public class ReserveService {

	@Autowired
	private ReserveRepository repo;
	
	@Autowired
	private HousingService housingService;
	
	@Autowired
	private UserService userService;

	public Reserve loadReserveById(Long id) throws NameNotFoundException {

		Reserve reserve = repo.findById(id);
		if (reserve == null) {
			throw new NameNotFoundException(id.toString());
		}
		return reserve;
	}

	public Reserve save(Reserve reserve) {
		return repo.save(reserve);
	}

	/*
	public List<Reserve> findByIdStartsWithIgnoreCase(Long id) {
		return repo.findByIdStartsWithIgnoreCase(id);
	}
	
*/
	
	public List<Reserve> findByHousingAndHost(String housing, User user)
	{
		return repo.findByHousingAndHost(housing, user);
	}
	

	public List<Reserve> findAsHost(User user)
	{
		return repo.findAsHost(user);
	}
	
	
	public List<Reserve> findAsGuest(User user)
	{
		return repo.findAsGuest(user);
	}
	
	public Reserve findOne(Long arg0) {
		return repo.findOne(arg0);
	}

	public void delete(Reserve arg0) {
		repo.delete(arg0);
	}

	public List<Reserve> findAll() {
		return repo.findAll();
	}
	
	public List<Housing> findByUserAndStatus(User user, ReserveTypeStatus status){
		return repo.findByUserAndStatus(user, status);
	}
	
	public VerticalLayout getEditForm(Reserve reserve)
	{
		ReserveEditForm editForm = new ReserveEditForm(reserve);
		
		if(reserve.getHousing() != null)
		{
			editForm.getHousing().setItems(editForm.getHousing().getValue());
			editForm.getHousing().setSelectedItem(editForm.getHousing().getValue());
		}
		
		if(reserve.getUser() != null)
		{
			editForm.getUser().setItems(editForm.getUser().getValue());
			editForm.getUser().setSelectedItem(editForm.getUser().getValue());
		}
		
		if(SecurityUtils.hasRole(UserRoleType.ADMIN))
		{
			editForm.getHousing().setItems(housingService.findAll());
			editForm.getUser().setItems(userService.findAll());
		}
		else
			editForm.getHousing().setItems(housingService.findByUser((User)VaadinService.getCurrentRequest()
					.getWrappedSession().getAttribute(User.class.getName())));

		editForm.getUser().setEnabled(SecurityUtils.hasRole(UserRoleType.ADMIN));
		editForm.getPrice().setEnabled(SecurityUtils.hasRole(UserRoleType.ADMIN));
		editForm.getHousing().setEnabled(SecurityUtils.hasRole(UserRoleType.ADMIN));
		editForm.getStatus().setEnabled(SecurityUtils.hasRole(UserRoleType.ADMIN));
		
		
		editForm.getHousing().addContextClickListener(e-> 			
			reserve.setHousing(editForm.getHousing().getValue())
		);
		
		editForm.getBinder().addValueChangeListener(e-> {
			editForm.getSave().setEnabled(editForm.getBinder().isValid());

		});
		
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
		

		return editForm;
	}
}
