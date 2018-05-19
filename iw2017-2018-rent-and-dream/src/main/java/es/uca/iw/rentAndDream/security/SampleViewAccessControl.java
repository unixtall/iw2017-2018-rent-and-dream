package es.uca.iw.rentAndDream.security;

import org.springframework.stereotype.Component;

import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;

import es.uca.iw.rentAndDream.WelcomeView;
import es.uca.iw.rentAndDream.housing.HousingSearchView;
import es.uca.iw.rentAndDream.housing.HousingView;
import es.uca.iw.rentAndDream.reserves.ReserveManagementView;
import es.uca.iw.rentAndDream.reserves.ReserveRequestView;
import es.uca.iw.rentAndDream.users.RoleType;
import es.uca.iw.rentAndDream.users.UserManagementView;
import es.uca.iw.rentAndDream.users.UserRegisterView;
import es.uca.iw.rentAndDream.users.UserView;

/**
 * This demonstrates how you can control access to views.
 */
@Component
public class SampleViewAccessControl implements ViewAccessControl {

    @Override
    public boolean isAccessGranted(UI ui, String beanName) {
    	
    	System.out.println("COMPROBANDO " + beanName + " PARA USUARIO CON ROLES: "+ SecurityUtils.roles());

    	if(SecurityUtils.hasRole(RoleType.ADMIN)){
    		return true;
    	} else if (beanName.equals(WelcomeView.VIEW_NAME) || beanName.equals(LoginScreen.VIEW_NAME) || beanName.equals(UserRegisterView.VIEW_NAME) 
    			|| beanName.equals(HousingSearchView.VIEW_NAME) || beanName.equals(HousingView.VIEW_NAME) || beanName.equals(ReserveRequestView.VIEW_NAME)) {
            return true;
        } else if (beanName.equals(UserView.VIEW_NAME)) {
            return SecurityUtils.hasRole(RoleType.USER) || SecurityUtils.hasRole(RoleType.MANAGER);
        } else if (beanName.equals(UserManagementView.VIEW_NAME) || beanName.equals(ReserveManagementView.VIEW_NAME)) {
            return SecurityUtils.hasRole(RoleType.MANAGER);
        } else {
        	return false;
        }
    }
}