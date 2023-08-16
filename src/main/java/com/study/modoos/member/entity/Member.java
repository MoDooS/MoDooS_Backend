package com.study.modoos.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Column(name = "department", length = 30)
    @Enumerated(EnumType.STRING)
    private Department department;

    @NotNull
    @Column(name = "ranking", length = 10)
    private String ranking;

    @NotNull
    @Column(name = "score")
    private Long score;

    @NotNull
    @Column(name = "isMember")
    private Boolean isMember;
    @Enumerated(EnumType.STRING)
    private Role role;


    @Builder
    public Member(String nickname, String password, String email,
                  Campus campus, Department department) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.campus = campus;
        this.ranking = "B";
        this.score = 200L;
        this.isMember = true;
        this.role = Role.MEMBER;
        this.department = department;
    }

    public Member(Member writer) {
        this.id = writer.getId();
        this.nickname = writer.getNickname();
        this.password = writer.getPassword();
        this.email = writer.getEmail();
        this.campus = writer.getCampus();
        this.ranking = writer.getRanking();
        this.score = writer.getScore();
        this.isMember = writer.getIsMember();
        this.role = writer.getRole();
        this.department = writer.getDepartment();
    }

    public Member(Long writerId, String writerNickname) {
        this.id = writerId;
        this.nickname = writerNickname;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateCampus(Campus campus) {
        this.campus = campus;
    }

    public void updateDepartment(Department department) {
        this.department = department;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
