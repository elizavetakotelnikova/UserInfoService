package com.example.outermicroservice.user.services;
import com.example.jpa.Role;
import com.example.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersDao extends JpaRepository<User, Long> {
    User findById(long id);
    User findByUsername(String username);
    List<User> findUserByAuthorities(Role role);
    void deleteById(long id);
}
