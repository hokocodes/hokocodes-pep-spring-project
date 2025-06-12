package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepo;

    public Optional<Account> register(Account account) {
      if (account.getUsername() == null || account.getUsername().isBlank()) {
            return Optional.empty(); // will result in 400
        }

        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return Optional.empty(); // will result in 400
        }

        if (accountRepo.findByUsername(account.getUsername()).isPresent()) {
            return Optional.of(new Account(-1, "", "")); // use magic value to signal 409
        }

        Account saved = accountRepo.save(account);
        return Optional.of(saved);
    
    }

    public Optional<Account> login(String username, String password) {
        return accountRepo.findByUsername(username)
                .filter(account -> account.getPassword().equals(password));
    }

    public Optional<Account> findById(int id) {
        return accountRepo.findById(id);
    }
}
