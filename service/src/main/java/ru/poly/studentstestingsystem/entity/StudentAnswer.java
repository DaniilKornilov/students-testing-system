package ru.poly.studentstestingsystem.entity;

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
@Table(name = "student_answer")
public class StudentAnswer {

    @Id
    @SequenceGenerator(
            name = "student_answer_sequence",
            sequenceName = "student_answer_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_answer_sequence"
    )
    private long id;

    @Column(nullable = false)
    private double value;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "student_task_id", foreignKey = @ForeignKey(name = "student_answer_student_task_fk"), nullable = false)
    private StudentTask studentTask;
}
