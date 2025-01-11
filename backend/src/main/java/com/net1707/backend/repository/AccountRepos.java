package com.net1707.backend.repository;

import com.net1707.backend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepos extends JpaRepository<Account,Long> {
    Optional<Account> findByEmail(String email);
}
