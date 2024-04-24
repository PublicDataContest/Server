package com.example.publicdatabackend.member.repository;

import com.example.publicdatabackend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Member,String> {
    Member findByUserName(String userName);
}
