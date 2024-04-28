package org.example.entities.owner;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OwnersDao extends JpaRepository<Owner, Long> {
    @NotNull Owner save(Owner owner);
    /*@Modifying
    @Query("update Owner o set o.birthday = ?1, o.cats = ?2")
    Owner update(Owner owner);*/
    @NotNull List<Owner> findAll();
    Owner findById(long id);
    List<Owner> findByBirthday(LocalDate birthday);
    Owner findOwnerByUsername(String username);
    void deleteById(long id);
}
