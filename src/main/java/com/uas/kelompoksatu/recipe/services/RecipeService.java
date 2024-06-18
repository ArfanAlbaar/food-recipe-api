package com.uas.kelompoksatu.recipe.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uas.kelompoksatu.dontdelete.ValidationService;
import com.uas.kelompoksatu.recipe.entities.Recipe;
import com.uas.kelompoksatu.recipe.entities.RecipeCategory;
import com.uas.kelompoksatu.recipe.repositories.RecipeRepository;
import com.uas.kelompoksatu.user.User;
import com.uas.kelompoksatu.user.service.AuthUserService;

import jakarta.transaction.Transactional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository repo;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private AuthUserService authUserService;

    @Transactional
    public Recipe create(String token, Recipe recipe) {
        validationService.validate(recipe);
        User users = authUserService.findByToken(token).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SORRY, YOU DON'T HAVE ACCESS"));
        if (users.getToken().equals(token)) {
            return repo.save(recipe);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Login First");
        }
    }

    public List<Recipe> readAll() {
        return repo.findAll();

    }

    public List<Recipe> getRecipesByCategory(RecipeCategory category) {
        return repo.findByCategory(category);
    }

    public Recipe readById(Integer recipeId) {
        return repo.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found"));
    }

    @Transactional
    public ResponseEntity<Recipe> update(User user, Recipe update) {
        validationService.validate(update);

        if (user != null) {
            if (user.getToken() != null) {
                Recipe recipe = repo.findById(update.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe is not found"));
                recipe.setRecipeName(update.getRecipeName());
                recipe.setIngredients(update.getIngredients());
                recipe.setInstructions(update.getInstructions());
                recipe.setCategory(update.getCategory());
                recipe.setFavorite(update.getFavorite());
                repo.save(recipe);
                return new ResponseEntity<Recipe>(recipe, HttpStatus.ACCEPTED);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not logged in");

            }

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You don't have permission");
        }

    }

    @Transactional
    public String delete(String token, Integer recipeId) {

        User users = authUserService.findByToken(token)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "SORRY, YOU DON'T HAVE ACCESS"));
        if (users.getToken().equals(token)) {

            Recipe recipe = repo.findById(recipeId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "recipe not found"));
            repo.deleteById(recipe.getId());
            return "Deleted";
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please Login First");
        }

    }
}