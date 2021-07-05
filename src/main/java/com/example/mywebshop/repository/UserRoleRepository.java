package com.example.mywebshop.repository;

import com.example.mywebshop.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByName(String name);
}
