package com.net1707.backend.security;

import com.net1707.backend.model.Account;
import com.net1707.backend.repository.AccountRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private  AccountRepos accountRepos;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            Account account = accountRepos.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return new CustomUser(account.getEmail(), account.getPassword() == null ? "" : account.getPassword(), List.of(new SimpleGrantedAuthority(account.getRole().toString())));
        }catch(Exception e){
            throw new UsernameNotFoundException("User Not Found");
        }
    }

}
