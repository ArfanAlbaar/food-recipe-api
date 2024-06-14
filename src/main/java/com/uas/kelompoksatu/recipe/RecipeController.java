package com.uas.kelompoksatu.recipe;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    private RecipeService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Recipe create(@RequestBody Recipe recipe) {
        return service.create(recipe);
    }

    @GetMapping("/list")
    public List<Recipe> getAll() {
        return service.readAll();
    }

    @GetMapping(path = "/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Recipe readById(@PathVariable("recipeId") Integer recipeId) {
        return service.readById(recipeId);
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<Recipe> update(@PathVariable("recipeId") Integer recipeId, @RequestBody Recipe recipe) {
        recipe.setId(recipeId);
        return service.update(recipe);
    }

    @DeleteMapping("/{recipeId}")
    public String delete(@PathVariable("recipeId") int recipeId) {

        return service.delete(recipeId);
    }
}
