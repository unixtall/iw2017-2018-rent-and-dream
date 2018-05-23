package es.uca.iw.rentAndDream.security;

import org.springframework.stereotype.Component;

import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;

import es.uca.iw.rentAndDream.entities.UserRoleType;
import es.uca.iw.rentAndDream.views.AvailabilityUserView;
import es.uca.iw.rentAndDream.views.HousingInfoView;
import es.uca.iw.rentAndDream.views.HousingSearchView;
import es.uca.iw.rentAndDream.views.HousingUserView;
import es.uca.iw.rentAndDream.views.ReserveHostView;
import es.uca.iw.rentAndDream.views.ReserveManagementView;
import es.uca.iw.rentAndDream.views.UserEditProfileView;
import es.uca.iw.rentAndDream.views.UserManagementView;
import es.uca.iw.rentAndDream.views.UserRegisterView;
import es.uca.iw.rentAndDream.views.UserView;

/**
 * This demonstrates how you can control access to views.
 */
@Component
public class SampleViewAccessControl implements ViewAccessControl {

    @Override
    public boolean isAccessGranted(UI ui, String beanName) {
    	
    	System.out.println("COMPROBANDO " + beanName + " PARA USUARIO CON ROLES: "+ SecurityUtils.roles());

    	if(SecurityUtils.hasRole(UserRoleType.ADMIN)){
    		return true;
    	} else if (beanName.equals(UserRegisterView.VIEW_NAME) 
    			|| beanName.equals(HousingSearchView.VIEW_NAME) || beanName.equals(HousingInfoView.VIEW_NAME)) {
            return true;
        } else if (beanName.equals(UserView.VIEW_NAME) || beanName.equals(UserEditProfileView.VIEW_NAME) || beanName.equals(HousingUserView.VIEW_NAME)
        		|| beanName.equals(AvailabilityUserView.VIEW_NAME) || beanName.equals(ReserveHostView.VIEW_NAME))
        {
            return SecurityUtils.hasRole(UserRoleType.USER) || SecurityUtils.hasRole(UserRoleType.MANAGER);
        } else if (beanName.equals(UserManagementView.VIEW_NAME) || beanName.equals(ReserveManagementView.VIEW_NAME)) {
            return SecurityUtils.hasRole(UserRoleType.MANAGER);
        } else {
        	return false;
        }
    }
}