package com.chunjae.nest.domain.user.entity;

import com.chunjae.nest.common.BaseEntity;
import com.chunjae.nest.domain.paper.entity.PaperAssignment;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String userId;

    @Column(length = 300, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String part;

    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<PaperAssignment> paperAssignments = new ArrayList<>();


}
