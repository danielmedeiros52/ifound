package com.Ifound.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "search" )
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "documents_on_searching", uniqueConstraints = {@UniqueConstraint(columnNames = {"search_id" , "documents_id"})})
    private List<Document> documents;
    @OneToOne
    private User userOwner;
    @OneToOne
    private User userFound;
    @Column(name = "search_at")
    private LocalDateTime searchAt;
    @Column(name = "found_at")
    private LocalDateTime foundAt;

    public Search(LocalDateTime searchAt ,List<Document> documents) {
        this.documents = documents;
        this.searchAt = searchAt;
    }
}
