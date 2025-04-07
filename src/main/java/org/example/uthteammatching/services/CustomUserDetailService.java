package org.example.uthteammatching.services;

import org.example.uthteammatching.models.CustomUserDetail;
import org.example.uthteammatching.models.UserRole;
import org.example.uthteammatching.models.UthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UthUser user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Sai");
        }
         Collection<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
         Set<UserRole> roles = user.getUserRoles();
         for (UserRole userRole : roles) {
             grantedAuthoritySet.add(new SimpleGrantedAuthority(userRole.getRole().getTen()));
         }
        return new CustomUserDetail(user, grantedAuthoritySet);
    }
}
