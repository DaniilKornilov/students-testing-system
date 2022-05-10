package ru.poly.studentstestingsystem.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
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
@Table
public class Test {

    @Id
    @SequenceGenerator(
            name = "test_sequence",
            sequenceName = "test_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "test_sequence"
    )
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "created_timestamp", nullable = false)
    private LocalDateTime createdTimestamp;

    @Column(name = "updated_timestamp", nullable = false)
    private LocalDateTime updatedTimestamp;

    @Column(name = "time_limit")
    private LocalTime timeLimit;

    @Column(name = "available_from", nullable = false)
    private LocalDateTime availableFrom;

    @Column(name = "available_to", nullable = false)
    private LocalDateTime availableTo;

    @ManyToOne
    @JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "test_course_fk"), nullable = false)
    private Course course;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Test test)) {
            return false;
        }
        return getId() == test.getId() &&
                getName().equals(test.getName()) &&
                getDescription().equals(test.getDescription()) &&
                getCreatedTimestamp().equals(test.getCreatedTimestamp()) &&
                getUpdatedTimestamp().equals(test.getUpdatedTimestamp()) &&
                getTimeLimit().equals(test.getTimeLimit()) &&
                getCourse().equals(test.getCourse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getCreatedTimestamp(), getUpdatedTimestamp(),
                getTimeLimit(), getCourse());
    }
}
