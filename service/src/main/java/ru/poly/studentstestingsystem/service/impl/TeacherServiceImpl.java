package ru.poly.studentstestingsystem.service.impl;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.poly.studentstestingsystem.dto.TeacherDto;
import ru.poly.studentstestingsystem.entity.Teacher;
import ru.poly.studentstestingsystem.entity.User;
import ru.poly.studentstestingsystem.mapper.SignUpRequestMapper;
import ru.poly.studentstestingsystem.mapper.TeacherMapper;
import ru.poly.studentstestingsystem.pojo.SignUpRequest;
import ru.poly.studentstestingsystem.pojo.TeacherSignUpRequest;
import ru.poly.studentstestingsystem.repository.TeacherRepository;
import ru.poly.studentstestingsystem.service.AuthService;
import ru.poly.studentstestingsystem.service.TeacherService;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final AuthService authService;

    private final TeacherRepository teacherRepository;

    private final SignUpRequestMapper signUpRequestMapper;

    private final TeacherMapper teacherMapper;

    @Autowired
    public TeacherServiceImpl(AuthService authService,
            TeacherRepository teacherRepository,
            SignUpRequestMapper signUpRequestMapper,
            TeacherMapper teacherMapper) {
        this.authService = authService;
        this.teacherRepository = teacherRepository;
        this.signUpRequestMapper = signUpRequestMapper;
        this.teacherMapper = teacherMapper;
    }

    @Override
    @Transactional
    public TeacherDto registerTeacher(TeacherSignUpRequest teacherSignUpRequest) {
        SignUpRequest signUpRequest = signUpRequestMapper.map(teacherSignUpRequest);
        User user = authService.registerUser(signUpRequest);

        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setFirstName(teacherSignUpRequest.getFirstName());
        teacher.setLastName(teacherSignUpRequest.getLastName());

        teacher = teacherRepository.saveAndFlush(teacher);
        return teacherMapper.map(teacher);
    }
}
