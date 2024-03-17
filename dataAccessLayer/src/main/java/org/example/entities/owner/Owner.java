package org.example.entities.owner;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.entities.cat.Cat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="owners")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private java.time.LocalDate birthday;
    @OneToMany(mappedBy = "owner")
    private List<Cat> cats;

    public Owner(LocalDate date, List<Cat> cats) {
        this.birthday = date;
        this.cats = cats;
    }
}
