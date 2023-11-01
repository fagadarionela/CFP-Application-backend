package com.app.cfp.controller;

import com.app.cfp.dto.AccountDTO;
import com.app.cfp.dto.StringResponseDTO;
import com.app.cfp.entity.Account;
import com.app.cfp.mapper.AccountMapper;
import com.app.cfp.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
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
        System.out.println("Userul " + principal.getName() + " s-a conectat");
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
    public ResponseEntity<StringResponseDTO> insertUser(@RequestBody AccountDTO accountDTO) {
        Account account = accountService.addAccount(accountMapper.toDomain(accountDTO));

        if (account != null) {
            return new ResponseEntity<>(StringResponseDTO.builder().message("The account with username " + account.getUsername() + " was created!").build(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(StringResponseDTO.builder().message("Can not add account!").build(), HttpStatus.CONFLICT);
    }

    @DeleteMapping("/users/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StringResponseDTO> deleteUser(@PathVariable("username") String username) {
        accountService.deleteAccount(username);

        return new ResponseEntity<>(StringResponseDTO.builder().message("The account with username " + username + " was deleted!").build(), HttpStatus.CREATED);
    }
}
