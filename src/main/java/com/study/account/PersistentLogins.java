package com.study.account;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "persistent_logins")
public class PersistentLogins {

    @Id @GeneratedValue
    private String series;
    private String username;
    private String token;
    @Column(name = "last_used")
    private LocalDateTime lastUsed;
}
