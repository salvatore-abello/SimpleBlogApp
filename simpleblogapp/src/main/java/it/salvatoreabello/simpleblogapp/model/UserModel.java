package it.salvatoreabello.simpleblogapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@Entity
@Data
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @JsonIgnore
    @JsonIgnore
    private int id;

    @NotEmpty(message="Name cannot be empty")
    private String name;

    @NotEmpty(message="Surname cannot be empty")
    private String surname;
    // @JsonIgnore
    @Email(message = "Email is not valid")
    @NotEmpty(message="Email cannot be empty")
    @Column(unique=true)
    private String email;
    // @JsonIgnore

    @NotEmpty(message="Password cannot be empty")
    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PostModel> posts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws NoSuchMethodException, MethodArgumentNotValidException {
        if(password.length() < 6){
            BindingResult bindingResult = new BeanPropertyBindingResult(this, "password");
            bindingResult.rejectValue("password", "length", "Password must be at least 6 characters long");

            throw new MethodArgumentNotValidException(new MethodParameter(this.getClass().getMethod("setPassword", String.class), 0), bindingResult);
        }
        // Is this ok?
        this.password = new BCryptPasswordEncoder().encode(password);
    }

}
