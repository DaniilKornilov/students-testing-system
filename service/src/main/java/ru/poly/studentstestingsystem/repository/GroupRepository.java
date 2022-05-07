package ru.poly.studentstestingsystem.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.poly.studentstestingsystem.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);
}
