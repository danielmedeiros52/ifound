package com.Ifound.model;

import com.Ifound.enumerates.EnumUserType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @OneToMany
    @JoinColumn(name="owner_document_id")
    private List<Document> documents;
    @Email
    @Column(name = "email",nullable = false, unique = true)
    private String email;
    @Column(name = "cellphone")
    private String cellphone;
    @Column(name = "street")
    private String street;
    @Column(name = "city")
    private String city;
    @Column(name = "zip_code")
    private String zipCode;
    @Column(name = "home_number")
    private String homeNumber;
    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    private EnumUserType type;
    @Column(name = "number_of_principal_document" , nullable = false, unique = true)
    private String numberOfPrincipalDocument;
    private boolean isActive;
    @Column(nullable = false, unique = true)
    private String username;
    private String token;
    @Column(nullable = false)
    private String password;
    private Date lastPasswordResetDate;
    private Date lastLogin;
    private boolean isAdministrator;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;

    public User(String subject, List<Role> role, int userID) {
        this.username = subject;
        this.roles= role;
        this.id=userID;
    }

    public User(int id, String username) {
        this.username = username;
        this.id = id;
    }
}
