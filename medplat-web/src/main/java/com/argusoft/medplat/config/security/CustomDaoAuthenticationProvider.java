package com.argusoft.medplat.config.security;

import com.argusoft.medplat.common.service.SystemConfigurationService;
import com.argusoft.medplat.web.users.dto.UserMasterDto;
import com.argusoft.medplat.common.util.LoginAESEncryptionUtil;
import com.argusoft.medplat.web.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.LinkedHashMap;
import java.util.Objects;

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
    public Authentication authenticate(Authentication authentication) {
        UsernamePasswordAuthenticationToken updatedAuthentication = null;
        try {
            String username = LoginAESEncryptionUtil.decrypt(authentication.getName(), false);
            String password = LoginAESEncryptionUtil.decrypt((String) authentication.getCredentials(), false);
            updatedAuthentication =
                    new UsernamePasswordAuthenticationToken(username, password);
            updatedAuthentication.setDetails(authentication.getDetails());
            LinkedHashMap<String, String> form = new LinkedHashMap<>();
            Authentication auth;
            if (authentication.getDetails() instanceof LinkedHashMap) {
                form = (LinkedHashMap<String, String>) authentication.getDetails();
            }
            if (form.get("login_code") != null) {
                UserMasterDto userMasterDto = userService.retrieveByLoginCode(form.get("login_code"));
                UserDetails user = retrieveUser(userMasterDto.getUserName(), (UsernamePasswordAuthenticationToken) authentication);
                auth = this.createSuccessAuthentication(user, authentication, user);
                userService.updateNoOfAttempts(userMasterDto.getUserName(), true);
            } else {
                auth = super.authenticate(updatedAuthentication);
                userService.updateNoOfAttempts(updatedAuthentication.getName(), true);
            }
            return auth;
        } catch (BadCredentialsException e) {
            //invalid login, update to user_attempts
            if (Objects.nonNull(updatedAuthentication)) {
                userService.updateNoOfAttempts(updatedAuthentication.getName(), false);
            }
            throw e;
        } catch (LockedException e) {
            //this user is locked!
            int LOGIN_TIMEOUT = (int) Math.floor((float) (userService.getLoginTimeout()) / 60000);
            throw new LockedException("Your account is locked for " + LOGIN_TIMEOUT + " minutes. Please try again later");
        } catch (Exception e) {
            throw new AuthenticationException("user_not_found", "Invalid username or password");
        }
    }
}
