package com.uas.kelompoksatu.recipe.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.uas.kelompoksatu.recipe.entities.Recipe;
import com.uas.kelompoksatu.recipe.entities.RecipeCategory;
import com.uas.kelompoksatu.user.User;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer>, JpaSpecificationExecutor<Recipe> {

    Optional<Recipe> findFirstByUserAndId(User user, Integer id);

    List<Recipe> findByCategory(RecipeCategory category);

}
