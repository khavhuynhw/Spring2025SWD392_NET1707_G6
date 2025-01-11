package com.net1707.backend.security;

import com.net1707.backend.model.Account;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;



public class CustomUser extends User {

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    @NotNull
    public Account.Role getRole() {
        return getAuthorities().isEmpty() ? Account.Role.MEMBER :
                Account.Role.valueOf(getAuthorities().iterator().next().getAuthority());
    }
}
