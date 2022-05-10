package ru.poly.studentstestingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.poly.studentstestingsystem.entity.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
