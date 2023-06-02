package com.app.cfp.service;

import com.app.cfp.entity.Account;
import com.app.cfp.entity.Resident;
import com.app.cfp.repository.AccountRepository;
import com.app.cfp.repository.ResidentRepository;
import com.app.cfp.utils.AuthorityType;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final ResidentRepository residentRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account addAccount(Account account) {
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    public void deleteAccount(String username) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            if (AuthorityType.RESIDENT.equals(account.getRole())) {
                Optional<Resident> residentOptional = residentRepository.findByAccount_Username(username);
                residentOptional.ifPresent(resident -> residentRepository.deleteById(resident.getId()));
            }
            accountRepository.deleteByUsername(username);
        }
    }
}
