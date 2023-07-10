package com.argusoft.medplat.config.security;

import com.argusoft.medplat.common.service.SystemConfigurationService;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.web.users.dto.UserMasterDto;
import com.argusoft.medplat.web.users.model.UserMaster;
import com.argusoft.medplat.web.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author alpesh
 */
public class SecurityUserService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenStore tokenStore;

//    @Autowired
//    private MyTechoUserService myTechoUserService;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        Integer LOGIN_ATTEMPTS = userService.getLoginAttempts() - 1;

        String grantType = request.getParameter("grant_type");
        String loginCode = request.getParameter("login_code");
        String clientId = request.getParameter("client_id");
        UserMasterDto userMasterDto;

        if (clientId != null && clientId.equals(ConstantUtil.SPRING_SECURITY_CLIENT_ID_DRTECHO)) {
            userMasterDto = authenticateDrTechoUser(username);
        } else {
            if (loginCode != null) {
                userMasterDto = userService.retrieveByLoginCode(loginCode);
            } else {
                userMasterDto = userService.retrieveByUserName(username);
            }
        }

        if (userMasterDto == null) {
            throw new AuthenticationException("user_not_found", "Invalid username");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (grantType.equals("password")) {
            Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(clientId, username);
            for (OAuth2AccessToken token : tokens) {
                tokenStore.removeRefreshToken(token.getRefreshToken());
                tokenStore.removeAccessToken(token);
            }
        }

        if (userMasterDto.getNoOfAttempts() != null && userMasterDto.getNoOfAttempts() > LOGIN_ATTEMPTS) {
            return new AuthenticationUser(userMasterDto.getId(), userMasterDto.getFullName(), userMasterDto.getUserName(), userMasterDto.getPassword(),
                    userMasterDto.getRoleId(), userMasterDto.getRoleName(), userMasterDto.getMinLocationId(), userMasterDto.getMinLocationName(), userMasterDto.getRoleCode(), userMasterDto.getRchInstitutionId(),
                    userMasterDto.getPrefferedLanguage(), userMasterDto.getContactNumber(), userMasterDto.getGender(), userMasterDto.getAddress(),
                    userMasterDto.getDateOfBirth(), true, true, true, false,
                    authorities, userMasterDto.getMinLocationLevel());
        } else {
            return new AuthenticationUser(userMasterDto.getId(), userMasterDto.getFullName(), userMasterDto.getUserName(), userMasterDto.getPassword(),
                    userMasterDto.getRoleId(), userMasterDto.getRoleName(), userMasterDto.getMinLocationId(), userMasterDto.getMinLocationName(), userMasterDto.getRoleCode(), userMasterDto.getRchInstitutionId(),
                    userMasterDto.getPrefferedLanguage(), userMasterDto.getContactNumber(), userMasterDto.getGender(), userMasterDto.getAddress(),
                    userMasterDto.getDateOfBirth(), true, true, true, true,
                    authorities, userMasterDto.getMinLocationLevel());
        }

    }

    private UserMasterDto authenticateDrTechoUser(String username) {
        UserMasterDto userMasterDto = userService.retrieveByUserName(username);
        if (userMasterDto == null) {
            UserMaster userMaster = userService.retriveUserByUserName(username);
            if (userMaster != null) {
                throw new AuthenticationException("user_not_verified", "User is not yet approved");
            } else {
                throw new AuthenticationException("user_not_verified", "User not found");
            }
        }
        if (userMasterDto.getRoleId() == null || !userMasterDto.getRoleId().equals(ConstantUtil.DRTECHO_USER_ROLE_ID)) {
            throw new AuthenticationException("user_not_found", "Invalid username or password");
        }
        return userMasterDto;
    }
}
