package com.example.ownermicroservice.repositories;

import com.example.jpa.Owner;
import com.example.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OwnersDao extends JpaRepository<Owner, Long> {
    Owner findById(long id);
    List<Owner> findByBirthday(LocalDate birthday);
    Owner findByUser(User user);
    void deleteById(long id);
}
