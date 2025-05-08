package com.grepp.matnam.app.model.user.entity;


import com.grepp.matnam.app.model.team.entity.Participant;
import com.grepp.matnam.app.model.user.code.Gender;
import com.grepp.matnam.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User extends BaseEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String userId;

    private String password;

    private String email;

    private String address;

    private String nickname;

    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private float temperature;

    @OneToOne
    @JoinColumn(name="userId")
    private Preference preference;

    @OneToMany
    @JoinColumn(name = "userId")
    private List<Map> maps;

    @OneToMany
    @JoinColumn(name = "userId")
    private List<Participant> participants;
}