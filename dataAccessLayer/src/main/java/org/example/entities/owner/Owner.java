package org.example.entities.owner;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.entities.cat.Cat;
import org.example.entities.user.User;

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
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade=CascadeType.REMOVE)
    private List<Cat> cats = new ArrayList<>();
    @OneToOne(targetEntity = User.class)
    @JoinColumn(name="user_id")
    private User user;
    public Owner(LocalDate date, List<Cat> cats, User user) {
        this.birthday = date;
        this.cats = cats;
        this.user = user;
    }
}
