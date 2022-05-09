package ru.poly.studentstestingsystem.service;

import ru.poly.studentstestingsystem.entity.User;
import ru.poly.studentstestingsystem.pojo.request.SignUpRequest;

public interface AuthService {

    User registerUser(SignUpRequest signUpRequest);
}
