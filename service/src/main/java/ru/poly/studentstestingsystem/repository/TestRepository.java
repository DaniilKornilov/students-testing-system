package ru.poly.studentstestingsystem.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.poly.studentstestingsystem.entity.Test;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

    List<Test> findTestsByCourse_Name(String courseName);
}
