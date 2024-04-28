package org.example.entities.owner;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.entities.cat.Cat;

import java.time.LocalDate;
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
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Cat> cats;
    private byte[] password;
    private String username;
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authorities",
            joinColumns = { @JoinColumn(name = "authority_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private List<Role> authorities;
    public Owner(LocalDate date, List<Cat> cats) {
        this.birthday = date;
        this.cats = cats;
    }

    public Owner(LocalDate birthday, List<Cat> cats, String username, byte[] password, List<Role> role) {
        this.birthday = birthday;
        this.cats = cats;
        this.username = username;
        this.password = password;
        this.authorities = role;
    }
}
