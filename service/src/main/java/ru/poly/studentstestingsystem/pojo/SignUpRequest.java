package ru.poly.studentstestingsystem.pojo;

import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest {

    private String username;
    private String email;
    private Set<String> roles;
    private String password;
}
