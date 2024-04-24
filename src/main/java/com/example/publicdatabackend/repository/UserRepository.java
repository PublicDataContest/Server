package com.example.publicdatabackend.repository;

import com.example.publicdatabackend.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,String> {
    Users findByUserName(String userName);
}
