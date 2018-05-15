package es.uca.iw.rentAndDream.security;

import org.springframework.stereotype.Component;

import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;
import es.uca.iw.rentAndDream.users.RoleType;

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
    	} else if (beanName.equals("welcomeView") || beanName.equals("loginScreen") || beanName.equals("userRegisterScreen") || beanName.equals("housingSearchView")) {
            return true;
        } else if (beanName.equals("userView")) {
            return SecurityUtils.hasRole(RoleType.USER) || SecurityUtils.hasRole(RoleType.MANAGER);
        } else if (beanName.equals("userManagementView") || beanName.equals("reserveManagementView")) {
            return SecurityUtils.hasRole(RoleType.MANAGER);
        } else {
        	return false;
        }
    }
}