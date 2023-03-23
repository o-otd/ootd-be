package com.ootd.be.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Member {

    @Id
    private Long id;

    private String email;
    private String name;

    private String password;

    private Set<Authority> authorities = new HashSet<>();

    @OneToMany
    private List<Feed> feeds;

}
