package it.salvatoreabello.simpleblogapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @NotEmpty(message="Name cannot be empty")
    @Pattern(regexp = "[a-zA-Z ]{4,32}")
    private String name;

    @NotEmpty(message="Surname cannot be empty")
    @Pattern(regexp = "[a-zA-Z ]{4,32}")
    private String surname;

    @Email(message = "Email is not valid")
    @NotEmpty(message="Email cannot be empty")
    @Column(unique=true)
    private String email;

    @NotEmpty(message="Password cannot be empty")
    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<PostModel> posts;

}
