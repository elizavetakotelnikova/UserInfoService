package org.example.entities.user;

import org.example.entities.owner.Owner;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersDao extends JpaRepository<User, Long> {
    User findById(long id);
    User findByUsername(String username);
    User findByOwner(Owner owner);
    List<User> findUserByAuthorities(Role role);
    void deleteById(long id);
}
