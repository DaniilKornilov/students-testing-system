package ru.poly.studentstestingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.poly.studentstestingsystem.entity.Role;
import ru.poly.studentstestingsystem.entity.enumeration.RoleEnum;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleEnum(RoleEnum roleEnum);

    Boolean existsByRoleEnum(RoleEnum roleEnum);
}
