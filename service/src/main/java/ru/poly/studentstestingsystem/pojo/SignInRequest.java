package ru.poly.studentstestingsystem.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SignInRequest {

    private String username;
    private String password;
}
