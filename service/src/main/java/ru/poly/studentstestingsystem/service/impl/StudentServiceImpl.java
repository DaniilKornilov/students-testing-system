package ru.poly.studentstestingsystem.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.poly.studentstestingsystem.dto.StudentDto;
import ru.poly.studentstestingsystem.dto.UserDto;
import ru.poly.studentstestingsystem.entity.Course;
import ru.poly.studentstestingsystem.entity.Group;
import ru.poly.studentstestingsystem.entity.Student;
import ru.poly.studentstestingsystem.entity.Teacher;
import ru.poly.studentstestingsystem.entity.User;
import ru.poly.studentstestingsystem.entity.enumeration.RoleEnum;
import ru.poly.studentstestingsystem.excelhandler.ExcelStudentsReader;
import ru.poly.studentstestingsystem.exception.StudentConstraintException;
import ru.poly.studentstestingsystem.exception.StudentNotFoundException;
import ru.poly.studentstestingsystem.mapper.StudentMapper;
import ru.poly.studentstestingsystem.parser.StudentUsernameParser;
import ru.poly.studentstestingsystem.pojo.request.SignUpRequest;
import ru.poly.studentstestingsystem.repository.CourseRepository;
import ru.poly.studentstestingsystem.repository.GroupRepository;
import ru.poly.studentstestingsystem.repository.StudentRepository;
import ru.poly.studentstestingsystem.repository.TeacherRepository;
import ru.poly.studentstestingsystem.repository.UserRepository;
import ru.poly.studentstestingsystem.service.AuthService;
import ru.poly.studentstestingsystem.service.StudentService;
import ru.poly.studentstestingsystem.vo.StudentUsernameValues;

@Service
public class StudentServiceImpl implements StudentService {

    private static final String STUDENT_NOT_FOUND_MESSAGE = "Студент с id %s не найден!";

    private static final String STUDENT_INVALID_EMAIL_MESSAGE =
            "Студент с невалидным email: %s!" + " Пожалуйста, введите корректный email";

    private static final String STUDENT_WITH_USERNAME_EXISTS_MESSAGE = "Студент с идентификатором %s уже существует!";

    private static final String TEACHER_WITH_USERNAME_NOT_EXISTS_MESSAGE =
            "Учитель с идентификатором %s не существует!";

    private static final String TEACHER_WITH_WRONG_USERNAME_MESSAGE =
            "Учитель может добавлять студентов только со своим идентификатором! Получено: %s, но ожидалось %s";

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    private ExcelStudentsReader excelStudentsReader;

    private GroupRepository groupRepository;

    private UserRepository userRepository;

    private CourseRepository courseRepository;

    private TeacherRepository teacherRepository;

    private StudentUsernameParser studentUsernameParser;

    private AuthService authService;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    public StudentDto getStudentById(long id) {
        return studentMapper.map(studentRepository.findById(id).orElseThrow(() ->
                new StudentNotFoundException(String.format(STUDENT_NOT_FOUND_MESSAGE, id))));
    }

    @Override
    public List<StudentDto> getStudents() {
        return studentRepository.findAll().stream()
                .map(studentMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<StudentDto> importStudents(MultipartFile file, String teacherUsername) {
        List<StudentDto> studentDtos = excelStudentsReader.readExcel(file);
        List<StudentDto> savedStudentDtos = new ArrayList<>();

        for (StudentDto studentDto : studentDtos) {
            StudentUsernameValues studentUsernameValues = getStudentUsernameValues(studentDto);
            validateTeacherUsername(teacherUsername, studentUsernameValues.getTeacherUsername());
            Student student = studentMapper.map(studentDto);
            validateStudent(student);

            SignUpRequest signUpRequest = getSignUpRequest(studentDto.getUserDto());
            User user = authService.registerUser(signUpRequest);
            student.setUser(user);

            Group group = getGroupOrCreateNewByName(studentUsernameValues.getGroupName());
            student.setGroup(group);

            Teacher teacher = getTeacher(studentUsernameValues.getTeacherUsername());

            getCourseOrCreateNewByName(studentUsernameValues.getCourseName(), group, teacher);

            Student savedStudent = studentRepository.save(student);
            savedStudentDtos.add(studentMapper.map(savedStudent));
        }

        return savedStudentDtos;
    }

    private void validateTeacherUsername(String teacherUsernameFromRequest, String teacherUsernameFromStudent) {
        if (!teacherUsernameFromRequest.equals(teacherUsernameFromStudent)) {
            throw new StudentConstraintException(String.format(TEACHER_WITH_WRONG_USERNAME_MESSAGE,
                    teacherUsernameFromStudent, teacherUsernameFromRequest));
        }
    }

    private void validateStudent(Student student) {
        String username = student.getUser().getUsername();
        validateStudentUsername(username);

        String email = student.getUser().getEmail();
        validateStudentEmail(email);
    }

    private void validateStudentUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new StudentConstraintException(String.format(STUDENT_WITH_USERNAME_EXISTS_MESSAGE, username));
        }
    }

    private void validateStudentEmail(String email) {
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new StudentConstraintException(String.format(STUDENT_INVALID_EMAIL_MESSAGE, email));
        }
    }

    private StudentUsernameValues getStudentUsernameValues(StudentDto studentDto) {
        String studentUsername = studentDto.getUserDto().getUsername();
        return studentUsernameParser.parse(studentUsername);
    }

    private Group getGroupOrCreateNewByName(String groupName) {
        Optional<Group> groupOptional = groupRepository.findByName(groupName);
        if (groupOptional.isEmpty()) {
            Group group = new Group();
            group.setName(groupName);
            return groupRepository.saveAndFlush(group);
        } else {
            return groupOptional.get();
        }
    }

    private void getCourseOrCreateNewByName(String courseName, Group group, Teacher teacher) {
        Optional<Course> courseOptional = courseRepository.findByName(courseName);
        if (courseOptional.isEmpty()) {
            Course course = new Course();
            course.setName(courseName);
            course.setTeacher(teacher);
            course.getGroups().add(group);
            courseRepository.saveAndFlush(course);
        }
    }

    private Teacher getTeacher(String teacherUsername) {
        Optional<Teacher> teacherOptional = teacherRepository.findTeacherByUser_Username(teacherUsername);
        return teacherOptional.orElseThrow(() -> new StudentConstraintException(
                String.format(TEACHER_WITH_USERNAME_NOT_EXISTS_MESSAGE, teacherUsername)));
    }

    private SignUpRequest getSignUpRequest(UserDto userDto) {
        SignUpRequest signUpRequest = new SignUpRequest();

        signUpRequest.setEmail(userDto.getEmail());
        signUpRequest.setPassword(userDto.getPassword());
        signUpRequest.setUsername(userDto.getUsername());

        Set<String> roles = new HashSet<>();
        roles.add(RoleEnum.ROLE_STUDENT.getRoleName());
        signUpRequest.setRoles(roles);

        return signUpRequest;
    }

    @Override
    public List<StudentDto> getStudentsByGroupName(String groupName) {
        List<Student> studentDtos = studentRepository.findStudentsByGroup_Name(groupName);
        return studentDtos.stream()
                .map(studentMapper::map)
                .collect(Collectors.toList());
    }

    @Autowired
    public void setExcelStudentsReader(ExcelStudentsReader excelStudentsReader) {
        this.excelStudentsReader = excelStudentsReader;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Autowired
    public void setStudentUsernameParser(StudentUsernameParser studentUsernameParser) {
        this.studentUsernameParser = studentUsernameParser;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Autowired
    public void setTeacherRepository(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }
}
