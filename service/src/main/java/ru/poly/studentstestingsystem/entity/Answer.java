package ru.poly.studentstestingsystem.entity;

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
public class Answer {

    @Id
    @SequenceGenerator(
            name = "answer_sequence",
            sequenceName = "answer_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "answer_sequence"
    )
    private long id;

    @ManyToOne
    @JoinColumn(name = "task_id", foreignKey = @ForeignKey(name = "answer_task_fk"), nullable = false)
    private Task task;

    @Column(nullable = false)
    private double value;

    @Column(nullable = false)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Answer answer)) {
            return false;
        }
        return getId() == answer.getId() &&
                Double.compare(answer.getValue(), getValue()) == 0 &&
                getTask().equals(answer.getTask()) &&
                getName().equals(answer.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTask(), getValue(), getName());
    }
}
