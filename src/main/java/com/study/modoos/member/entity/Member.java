package com.study.modoos.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nickname", length = 20)
    private String nickname;

    @NotNull
    @Column(name = "password", length = 100)
    private String password;

    @Email
    @NotNull
    @Column(name = "email", length = 200)
    private String email;

    @NotNull
    @Column(name = "campus", length = 10)
    @Enumerated(EnumType.STRING)
    private Campus campus;

    @NotNull
    @Column(name = "ranking", length = 10)
    private String ranking;

    @NotNull
    @Column(name = "score")
    private Long score;

    @NotNull
    @Column(name = "isMember")
    private Boolean isMember;

    @Builder
    public Member(String nickname, String password, String email, Campus campus){
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.campus =campus;
        this.ranking = "B";
        this.score = 200L;
        this.isMember = true;
    }
}
