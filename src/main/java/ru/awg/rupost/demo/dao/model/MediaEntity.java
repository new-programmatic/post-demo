package ru.awg.rupost.demo.dao.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "MEDIAS")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class MediaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "path", nullable = false)
    private String path;
}
