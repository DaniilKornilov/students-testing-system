package ru.poly.studentstestingsystem.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.poly.studentstestingsystem.dto.StudentDto;
import ru.poly.studentstestingsystem.entity.Group;
import ru.poly.studentstestingsystem.entity.Student;
import ru.poly.studentstestingsystem.excelhandler.ExcelStudentsReader;
import ru.poly.studentstestingsystem.exception.StudentNotFoundException;
import ru.poly.studentstestingsystem.mapper.StudentMapper;
import ru.poly.studentstestingsystem.repository.GroupRepository;
import ru.poly.studentstestingsystem.repository.StudentRepository;
import ru.poly.studentstestingsystem.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

    private static final String STUDENT_NOT_FOUND_MESSAGE = "Student with id %s not found!";

    private static final String STUDENT_WITH_EMAIL_EXISTS = "Student with email %s already exists!";

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    private ExcelStudentsReader excelStudentsReader;

    private GroupRepository groupRepository;

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
            Student student = studentMapper.map(studentDto);
//            if (studentRepository.existsByEmail(student.getEmail())) {
//                throw new StudentConstraintException(String.format(STUDENT_WITH_EMAIL_EXISTS, student.getEmail()));
//            }
            Group group = student.getGroup();
            Optional<Group> groupOptional = groupRepository.findByName(group.getName());
            if (groupOptional.isEmpty()) {
                group = groupRepository.saveAndFlush(group);
            } else {
                group = groupOptional.get();
            }
            student.setGroup(group);
            Student savedStudent = studentRepository.save(student);
            savedStudentDtos.add(studentMapper.map(savedStudent));
        }

        return savedStudentDtos;
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
}
