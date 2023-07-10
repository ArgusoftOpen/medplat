package com.argusoft.medplat.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author dax
 */
public class AuthenticationUser extends User {

    private ImtechoSecurityUser user;

    public AuthenticationUser(Integer id, String name, String username, String password, Integer roleId, String roleName,
            Integer minLocationId, String minLocationName, String roleCode, Integer rchInstitutionId,
            String preferredLanguage, String mobileNumber, String gender, String address, Date dob,
            boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities, Integer minLocationLevel) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        user = new ImtechoSecurityUser(id, name, username, roleId, roleName, minLocationId, minLocationName, roleCode,
                rchInstitutionId, preferredLanguage, mobileNumber, gender, address, dob, minLocationLevel);

    }

    public ImtechoSecurityUser getUser() {
        return user;
    }

    public void setUser(ImtechoSecurityUser user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthenticationUser)) return false;
        if (!super.equals(o)) return false;
        AuthenticationUser that = (AuthenticationUser) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user);
    }
}
