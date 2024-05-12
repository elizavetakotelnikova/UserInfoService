package org.example.entities.cat;

import org.example.valueObjects.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CatsDao extends JpaRepository<Cat, Long> {
    Cat findById(long id);
    List<Cat> findByOwnerId(Long ownerId);
    List<Cat> findByBirthday(LocalDate date);
    List<Cat> findByColor(Color color);
    List<Cat> findByName(String name);
    List<Cat> findByBreed(String breed);
    void deleteById(long id);
}
