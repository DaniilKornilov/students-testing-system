package ru.poly.studentstestingsystem.pojo.request;

import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest extends SignInRequest {

    @Email(message = "Введите корректный email!")
    private String email;

    @NotNull(message = "Добавьте хотя бы 1 роль!")
    @Size(min = 1, message = "Добавьте хотя бы 1 роль!")
    private Set<String> roles;
}
