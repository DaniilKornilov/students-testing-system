package ru.poly.studentstestingsystem.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "name", name = "course_name_uk"),
})
public class Course {

    @Id
    @SequenceGenerator(
            name = "course_sequence",
            sequenceName = "course_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "course_sequence"
    )
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "teacher_id", foreignKey = @ForeignKey(name = "course_teacher_fk"), nullable = false)
    private Teacher teacher;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "course_group",
            joinColumns = @JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "course_group_fk")),
            inverseJoinColumns = @JoinColumn(name = "group_id", foreignKey = @ForeignKey(name = "group_course_fk")))
    private Set<Group> groups = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course course)) {
            return false;
        }
        return getId() == course.getId() &&
                getName().equals(course.getName()) &&
                Objects.equals(getTeacher(), course.getTeacher()) &&
                Objects.equals(getGroups(), course.getGroups());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getTeacher(), getGroups());
    }
}
