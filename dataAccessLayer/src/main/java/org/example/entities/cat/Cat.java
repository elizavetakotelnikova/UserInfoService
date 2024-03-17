package org.example.entities.cat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Color;
import org.example.entities.owner.Owner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="cats")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String breed;
    private Color color;
    @ManyToOne(targetEntity = Owner.class)
    private Long ownerId;
    private java.time.LocalDate birthday;
    @ManyToMany(targetEntity = Cat.class)
    private List<Cat> friends;

    public Cat(String name, String breed, Color color, Long id, LocalDate date, List<Cat> friends) {
        this.name = name;
        this.breed = breed;
        this.color = color;
        this.id = id;
        this.birthday = date;
        this.friends = friends;
    }
}
