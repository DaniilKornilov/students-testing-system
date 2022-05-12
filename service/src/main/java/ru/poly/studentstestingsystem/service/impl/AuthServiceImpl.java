package ru.poly.studentstestingsystem.service.impl;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.poly.studentstestingsystem.entity.Role;
import ru.poly.studentstestingsystem.entity.User;
import ru.poly.studentstestingsystem.entity.enumeration.RoleEnum;
import ru.poly.studentstestingsystem.exception.InvalidSignUpRequestBody;
import ru.poly.studentstestingsystem.pojo.request.SignUpRequest;
import ru.poly.studentstestingsystem.repository.RoleRepository;
import ru.poly.studentstestingsystem.repository.UserRepository;
import ru.poly.studentstestingsystem.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String USERNAME_TAKEN_MESSAGE = "Ошибка! имя пользователя: %s уже занято";

    private static final String ROLES_INVALID_MESSAGE =
            "Введена неверная роль! Возможные варианты: ADMIN, TEACHER, STUDENT. Получены: %s.";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Override
    public User registerUser(SignUpRequest signUpRequest) {
        validateSignUpRequest(signUpRequest);
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        signUpRequest.getRoles().forEach(r -> {
            Role role = roleRepository.findByRoleEnum(RoleEnum.get(r));
            roles.add(role);
        });
        user.setRoles(roles);
        return userRepository.save(user);
    }

    private void validateSignUpRequest(SignUpRequest signUpRequest) {
        String username = signUpRequest.getUsername();
        validateUsername(username);

        Set<String> roles = signUpRequest.getRoles();
        validateRoles(roles);
    }

    private void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new InvalidSignUpRequestBody(String.format(USERNAME_TAKEN_MESSAGE, username));
        }
    }

    private void validateRoles(Set<String> roles) {
        roles.forEach(role -> {
            if (!roleRepository.existsByRoleEnum(RoleEnum.get(role))) {
                throw new InvalidSignUpRequestBody(String.format(ROLES_INVALID_MESSAGE, roles));
            }
        });
    }
}
