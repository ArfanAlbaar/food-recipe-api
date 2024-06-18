package com.uas.kelompoksatu.recipe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uas.kelompoksatu.recipe.entities.Recipe;
import com.uas.kelompoksatu.recipe.entities.RecipeCategory;
import com.uas.kelompoksatu.recipe.services.RecipeService;
import com.uas.kelompoksatu.user.User;

@RestController
@RequestMapping("/api/recipe")
@CrossOrigin
public class RecipeController {

    @Autowired
    private RecipeService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Recipe create(@RequestHeader("API-TOKEN") String token, @RequestBody Recipe recipe) {
        try {
            return service.create(token, recipe);
        } catch (Exception e) {
            throw new RuntimeException("Error saving Recipe", e);
        }
    }

    @GetMapping
    public List<Recipe> readByCategory(@RequestParam(required = false) String category) {
        if (category != null) {
            try {
                RecipeCategory recipeCategory = RecipeCategory.valueOf(category.toUpperCase());
                return service.getRecipesByCategory(recipeCategory);
            } catch (IllegalArgumentException e) {
                // Handle invalid category value
                throw new IllegalArgumentException("Invalid category: " + category);
            }
        } else {
            return service.readAll();
        }
    }

    @GetMapping(path = "/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Recipe readById(@PathVariable("recipeId") Integer recipeId) {
        return service.readById(recipeId);
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<Recipe> update(User user, @PathVariable("recipeId") Integer recipeId,
            @RequestBody Recipe recipe) {
        recipe.setId(recipeId);
        return service.update(user, recipe);
    }

    @DeleteMapping(path = "/{recipeId}")
    public String delete(@RequestHeader("API-TOKEN") String token, @PathVariable("recipeId") Integer recipeId) {
        return service.delete(token, recipeId);
    }
}
