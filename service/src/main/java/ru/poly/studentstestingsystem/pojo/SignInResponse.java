package ru.poly.studentstestingsystem.pojo;

import java.util.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SignInResponse {

    private String subject;
    private Collection<?> authorities;
    private String token;
}
