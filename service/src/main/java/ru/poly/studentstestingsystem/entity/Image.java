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
public class Image {

    @Id
    @SequenceGenerator(
            name = "image_sequence",
            sequenceName = "image_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "image_sequence"
    )
    private long id;

    @ManyToOne
    @JoinColumn(name = "task_id", foreignKey = @ForeignKey(name = "image_task_fk"), nullable = false)
    private Task task;

    @Column(nullable = false)
    private String path;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Image image)) {
            return false;
        }
        return getId() == image.getId() &&
                getTask().equals(image.getTask()) &&
                getPath().equals(image.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTask(), getPath());
    }
}
