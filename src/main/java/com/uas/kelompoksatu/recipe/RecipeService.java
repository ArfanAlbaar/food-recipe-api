package com.uas.kelompoksatu.recipe;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository repo;

    @Transactional
    public Recipe create(Recipe recipe) {
        return repo.save(recipe);
    }

    public List<Recipe> readAll() {
        return repo.findAll();

    }

    public Recipe readById(Integer recipeId) {
        return repo.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found"));
    }

    @Transactional
    public ResponseEntity<Recipe> update(Recipe update) {
        Recipe recipe = repo.findById(update.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe is not found"));
        recipe.setRecipeName(update.getRecipeName());
        recipe.setIngredients(update.getIngredients());
        recipe.setInstructions(update.getInstructions());
        recipe.setCategory(update.getCategory());
        recipe.setFavorite(update.getFavorite());
        repo.save(recipe);
        return new ResponseEntity<Recipe>(recipe, HttpStatus.ACCEPTED);
    }

    @Transactional
    public String delete(Integer recipeId) {
        Recipe recipe = repo.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found"));
        repo.deleteById(recipe.getId());
        return "Deleted";
    }
}