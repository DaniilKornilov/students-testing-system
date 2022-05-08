package ru.poly.studentstestingsystem.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
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
import ru.poly.studentstestingsystem.pojo.SignUpRequest;
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

    private static final String STUDENT_WITH_EMAIL_EXISTS = "Студент с email %s уже существует!";

    private static final String STUDENT_WITH_USERNAME_EXISTS = "Студент с идентификатором %s уже существует!";

    private static final String TEACHER_WITH_USERNAME_NOT_EXISTS = "Учитель с идентификатором %s не существует!";

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
    public List<StudentDto> importStudents(MultipartFile file) {
        List<StudentDto> studentDtos = excelStudentsReader.readExcel(file);
        List<StudentDto> savedStudentDtos = new ArrayList<>();
        for (StudentDto studentDto : studentDtos) {
            StudentUsernameValues studentUsernameValues = getStudentUsernameValues(studentDto);
            Student student = studentMapper.map(studentDto);
            validateStudent(student);

            SignUpRequest signUpRequest = getSignUpRequest(studentDto.getUserDto());
            User user = authService.registerUser(signUpRequest);
            student.setUser(user);

            Group group = getGroupOrCreateNewByName(studentUsernameValues.getGroupName());
            student.setGroup(group);

            Course course = getCourseOrCreateNewByName(studentUsernameValues.getCourseName());
            course.getGroups().add(group);

            Teacher teacher = getTeacher(studentUsernameValues.getTeacherUsername());
            course.setTeacher(teacher);

            Student savedStudent = studentRepository.save(student);
            savedStudentDtos.add(studentMapper.map(savedStudent));
        }

        return savedStudentDtos;
    }

    private void validateStudent(Student student) {
        String email = student.getUser().getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new StudentConstraintException(String.format(STUDENT_WITH_EMAIL_EXISTS, email));
        }

        String username = student.getUser().getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new StudentConstraintException(String.format(STUDENT_WITH_USERNAME_EXISTS, username));
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

    private Course getCourseOrCreateNewByName(String courseName) {
        Optional<Course> courseOptional = courseRepository.findByName(courseName);
        if (courseOptional.isEmpty()) {
            Course course = new Course();
            course.setName(courseName);
            return courseRepository.saveAndFlush(course);
        } else {
            return courseOptional.get();
        }
    }

    private Teacher getTeacher(String teacherUsername) {
        Optional<Teacher> teacherOptional = teacherRepository.findTeacherByUser_Username(teacherUsername);
        return teacherOptional.orElseThrow(() -> new StudentConstraintException(TEACHER_WITH_USERNAME_NOT_EXISTS));
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
