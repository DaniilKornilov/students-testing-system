package ru.poly.studentstestingsystem.entity;

import java.time.LocalDateTime;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "student_test")
public class StudentTest {

    @Id
    @SequenceGenerator(
            name = "student_test_sequence",
            sequenceName = "student_test_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_test_sequence"
    )
    private long id;

    @ManyToOne
    @JoinColumn(name = "student_id", foreignKey = @ForeignKey(name = "student_test_student_fk"), nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "test_id", foreignKey = @ForeignKey(name = "student_test_test_fk"), nullable = false)
    private Test test;

    @Column(name = "started_timestamp", nullable = false)
    private LocalDateTime startedTimestamp;

    @Column(name = "finished_timestamp", nullable = false)
    private LocalDateTime finishedTimestamp;
}
