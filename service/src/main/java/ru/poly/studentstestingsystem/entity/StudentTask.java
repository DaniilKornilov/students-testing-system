package ru.poly.studentstestingsystem.entity;

import java.time.LocalDateTime;
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
@Table(name = "student_task")
public class StudentTask {

    @Id
    @SequenceGenerator(
            name = "student_task_sequence",
            sequenceName = "student_task_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_task_sequence"
    )
    private long id;

    @Column(name = "is_correct")
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "task_id", foreignKey = @ForeignKey(name = "student_task_task_fk"), nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "student_test_id", foreignKey = @ForeignKey(name = "student_task_student_test_fk"), nullable = false)
    private StudentTest studentTest;

    @Column(name = "started_timestamp", nullable = false)
    private LocalDateTime startedTimestamp;

    @Column(name = "finished_timestamp", nullable = false)
    private LocalDateTime finishedTimestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentTask that)) {
            return false;
        }
        return getId() == that.getId() &&
                isCorrect() == that.isCorrect() &&
                getTask().equals(that.getTask()) &&
                getStudentTest().equals(that.getStudentTest()) &&
                getStartedTimestamp().equals(that.getStartedTimestamp()) &&
                getFinishedTimestamp().equals(that.getFinishedTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), isCorrect(), getTask(), getStudentTest(), getStartedTimestamp(),
                getFinishedTimestamp());
    }
}
