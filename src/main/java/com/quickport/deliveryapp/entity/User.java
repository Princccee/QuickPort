package com.quickport.deliveryapp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Column(unique = true, nullable = false)
    private String phone;

    private Boolean isVerified;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private Set<Role> roles;

    @Enumerated(EnumType.STRING)
    private Roles role;

    // One user can have multiple addresses
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Address> addresses;
}
