package com.uas.kelompoksatu.recipe.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "premiumRecipes")
public class Premium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String premiumName;

    @Lob
    @Column(name = "data_recipes", columnDefinition = "LONGBLOB")
    private byte[] dataRecipes;

}
