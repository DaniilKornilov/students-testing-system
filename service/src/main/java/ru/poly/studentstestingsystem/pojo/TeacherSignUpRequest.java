package ru.poly.studentstestingsystem.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeacherSignUpRequest extends SignUpRequest {

    private String firstName;

    private String lastName;
}
