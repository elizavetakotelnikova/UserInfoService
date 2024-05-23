package com.example.outermicroservice.role;
import com.example.jpa.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesDao extends JpaRepository<Role, Long> {
    Role findRoleById(Long id);
    Role findRoleByAuthority(String authority);
}