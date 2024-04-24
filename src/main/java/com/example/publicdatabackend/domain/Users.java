package com.example.publicdatabackend.domain;

import com.example.publicdatabackend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Column(name="login_id", nullable = false, unique = true)
    private String userName;

    @Column(name="password",nullable = false)
    private String password;

    @Builder
    private Users(Long id, String userName, String password){
        this.id=id;
        this.userName=userName;
        this.password=password;
    }
}
