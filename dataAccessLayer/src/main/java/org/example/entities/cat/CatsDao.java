package org.example.entities.cat;

import org.example.entities.owner.Owner;
import org.example.valueObjects.Color;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CatsDao extends JpaRepository<Cat, Long> {
    @NotNull List<Cat> findAll();
    Cat findById(long id);
    /*@Modifying
    @Query("update Cat c set c.name = ?1, c.color = ?2, c.birthday = ?3, c.breed = ?4, c.owner = ?5, c.friends = ?6")
    Cat update(Cat cat);*/
    //List<Cat> findByCriteria(FindCriteria criteria);
    List<Cat> findByOwnerId(Long ownerId);
    List<Cat> findByBirthday(LocalDate date);
    List<Cat> findByColor(Color color);
    List<Cat> findByName(String name);
    List<Cat> findByBreed(String breed);
    @NotNull Cat save(@NotNull Cat cat);
    void deleteById(long id);
}
