package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,String> {
    Users findByUserName(String userName);
    Optional<Users> findById(Long id);
}
