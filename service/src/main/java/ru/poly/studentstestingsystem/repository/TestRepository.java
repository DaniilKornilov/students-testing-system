package ru.poly.studentstestingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.poly.studentstestingsystem.entity.Course;

@Repository
public interface TestRepository extends JpaRepository<Course, Long> {

}
