package ru.awg.rupost.demo.dao.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCTS")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 250)
    private String title;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @Column(name = "reviews_count", nullable = false)
    private Integer reviewsCount;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @OneToOne
    @JoinColumn(name = "media_id", nullable = false)
    private MediaEntity media;

    @Column(name = "description")
    private String description;
}
