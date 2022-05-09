package ru.poly.studentstestingsystem.pojo.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SignInRequest {

    @Size(min = 2, max = 20, message = "Длина имени пользователя должна быть от 2 до 20 символов!")
    @NotNull(message = "Введите имя пользователя!")
    private String username;

    @Size(min = 6, max = 20, message = "Длина пароля должна быть от 6 до 20 символов!")
    @NotNull(message = "Введите пароль!")
    private String password;
}
