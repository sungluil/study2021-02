package com.study.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    @Query("select m from Account m")
    List<Account> findAllAccount();

    Account findByEmail(String emailOrNickname);

    Account findByNickname(String emailOrNickname);

}
