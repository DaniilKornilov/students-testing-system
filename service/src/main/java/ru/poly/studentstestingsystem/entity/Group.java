package ru.poly.studentstestingsystem.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "groups", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name", name = "group_name_uk"),
})
public class Group {
    @Id
    @SequenceGenerator(
            name = "group_sequence",
            sequenceName = "group_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "group_sequence"
    )
    private long id;

    @Column(nullable = false)
    private String name;
}
