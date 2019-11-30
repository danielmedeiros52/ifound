package com.Ifound.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "number_of_registry")
    private String numberOfRegistry;
    @Column(name="owner_name")
    private String ownerName;
    @Column(name="found_date")
    private LocalDateTime foundDate;


}
