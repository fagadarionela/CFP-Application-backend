package com.app.cfp.service;

import com.app.cfp.entity.Account;
import com.app.cfp.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalUser = accountRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            Account account = optionalUser.get();
            Set<String> roleSet = new HashSet<>();
            roleSet.add(account.getRole().toString());
            return User.builder()
                    .username(username)
                    .password(account.getPassword())
                    .roles(roleSet.toArray(new String[0]))
                    .build();
        } else {
            throw new UsernameNotFoundException("User Name is not Found");
        }
    }
}
