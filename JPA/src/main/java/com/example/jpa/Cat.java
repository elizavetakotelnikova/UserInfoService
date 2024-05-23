package com.example.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Enumerated(EnumType.STRING)
    @Convert(converter = ColorConverter.class)
    private Color color;
    @ManyToOne(targetEntity = Owner.class)
    @JoinColumn(name="owner_id", nullable = false)
    private Owner owner;
    private LocalDate birthday;
    @ManyToMany(targetEntity = Cat.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "friends",
            joinColumns = { @JoinColumn(name = "second_cat_id") },
            inverseJoinColumns = { @JoinColumn(name = "first_cat_id") }
    )
    private List<Cat> friends = new ArrayList<>();

    public Cat(String name, String breed, Color color, Owner owner, LocalDate date, List<Cat> friends) {
        this.name = name;
        this.breed = breed;
        this.color = color;
        this.owner = owner;
        this.birthday = date;
        this.friends = friends;
    }
}
