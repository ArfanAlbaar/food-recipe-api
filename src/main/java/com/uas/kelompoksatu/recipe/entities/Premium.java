package com.uas.kelompoksatu.recipe.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "premiumRecipes")
public class Premium {

    @Id
    private Integer id;

    private String premiumName;

    @Lob
    private byte[] dataRecipes;

}
