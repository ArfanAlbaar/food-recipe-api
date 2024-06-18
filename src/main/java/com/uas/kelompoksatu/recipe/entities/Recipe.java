package com.uas.kelompoksatu.recipe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uas.kelompoksatu.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String recipeName;

    @Column(columnDefinition = "LONGTEXT")
    private String ingredients;

    @Column(columnDefinition = "LONGTEXT")
    private String instructions;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecipeCategory category;

    private Boolean favorite = false;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;
}
