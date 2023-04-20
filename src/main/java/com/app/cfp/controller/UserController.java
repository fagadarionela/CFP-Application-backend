package com.app.cfp.controller;

import com.app.cfp.dto.AccountDTO;
import com.app.cfp.dto.ResidentDTO;
import com.app.cfp.entity.Account;
import com.app.cfp.entity.Resident;
import com.app.cfp.mapper.AccountMapper;
import com.app.cfp.service.AccountService;
import com.app.cfp.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@Configuration
@AllArgsConstructor
public class UserController {

    private final AccountService accountService;

    private final AccountMapper accountMapper;

    @RequestMapping(value = "/login")
    public Principal login(Principal principal) {
        return principal;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AccountDTO>> getAllUsers() {
        List<Account> accounts = accountService.getAllAccounts();

        return new ResponseEntity<>(accounts.stream().map(accountMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> insertUser(@RequestBody AccountDTO accountDTO) {
        Account account = accountService.addAccount(accountMapper.toDomain(accountDTO));

        if (account != null) {
            return new ResponseEntity<>("The account with username " + account.getUsername() + " was created!", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Can not add account!", HttpStatus.CONFLICT);
    }
}
