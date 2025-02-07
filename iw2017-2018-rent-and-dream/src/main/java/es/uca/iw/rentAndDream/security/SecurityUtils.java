package es.uca.iw.rentAndDream.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import es.uca.iw.rentAndDream.entities.UserRoleType;;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public static boolean hasRole(UserRoleType role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if(authentication == null)
        	return role == UserRoleType.GUEST;
        else
        	return authentication.getAuthorities().contains(new SimpleGrantedAuthority(role.toString()));
    }
    
    public static Collection<? extends GrantedAuthority> roles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		list.add(new SimpleGrantedAuthority(UserRoleType.GUEST.toString()));
        
        if(authentication != null ){
        	return authentication.getAuthorities();
        } else{
        	return list;
        }
    }
}
