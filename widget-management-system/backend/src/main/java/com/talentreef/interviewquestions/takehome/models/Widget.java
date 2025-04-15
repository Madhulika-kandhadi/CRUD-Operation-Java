package com.talentreef.interviewquestions.takehome.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "widgets", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Widget {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Name cannot be blank")
  @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
  @Column(nullable = false, unique = true)
  private String name;

  @Size(min = 5, max = 1000, message = "Description must be between 5 and 1000 characters")
  @Column(length = 1000)
  private String description;

  @NotNull(message = "Price cannot be null")
  @DecimalMin(value = "1.00", message = "Price must be at least 1.00")
  @DecimalMax(value = "20000.00", message = "Price cannot exceed 20000.00")
  @Digits(integer = 5, fraction = 2, message = "Price must have up to 5 integer digits and 2 decimal places")
  @Column(nullable = false)
  private BigDecimal price;
}