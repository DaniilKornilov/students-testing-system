package ru.poly.studentstestingsystem.pojo.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeacherSignUpRequest extends SignUpRequest {

    @Size(min = 2, max = 20, message = "Длина имени учителя должна быть от 2 до 20 символов!")
    @NotNull(message = "Введите имя учителя!")
    private String firstName;

    @Size(min = 2, max = 20, message = "Длина фамилии учителя должна быть от 2 до 20 символов!")
    @NotNull(message = "Введите фамилию учителя!")
    private String lastName;
}
