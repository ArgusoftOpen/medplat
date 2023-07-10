package com.argusoft.medplat.config.security;

import com.argusoft.medplat.common.service.SystemConfigurationService;
import com.argusoft.medplat.web.users.dto.UserMasterDto;
import com.argusoft.medplat.web.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.LinkedHashMap;

/**
 *
 * @author dax
 */
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @Autowired
    public void setUserDetailsService(SecurityUserService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            {

        try {
            LinkedHashMap<String,String> form=new LinkedHashMap<>();
            Authentication auth;
            if(authentication.getDetails() instanceof LinkedHashMap){
                 form = (LinkedHashMap<String,String>)authentication.getDetails();
            }
            if(form.get("login_code")!=null){
                UserMasterDto userMasterDto = userService.retrieveByLoginCode(form.get("login_code"));
                UserDetails user = retrieveUser(userMasterDto.getUserName(),(UsernamePasswordAuthenticationToken) authentication);
                auth = this.createSuccessAuthentication(user,authentication,user);
                userService.updateNoOfAttempts(userMasterDto.getUserName(), true);
            }else{
                 auth = super.authenticate(authentication);
                userService.updateNoOfAttempts(authentication.getName(), true);
            }
            return auth;
        } catch (BadCredentialsException e) {
            //invalid login, update to user_attempts
            userService.updateNoOfAttempts(authentication.getName(), false);
            throw e;
        } catch (LockedException e) {
            //this user is locked!
            int LOGIN_TIMEOUT = (int) Math.floor((float)(userService.getLoginTimeout()) / 60000);
            throw new LockedException("Your account is locked for " + LOGIN_TIMEOUT + " minutes. Please try again later");
        } catch (Exception e) {
            throw new AuthenticationException("user_not_found", "Invalid username or password");
        }
    }
}
