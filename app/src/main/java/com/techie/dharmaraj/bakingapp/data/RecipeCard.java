package com.techie.dharmaraj.bakingapp.data;

/**
 * this class holds the name and image url to be displayed in the main activity List
 */

public class RecipeCard {
    private String recipeName;
    private String recipeImageUrl;

    public RecipeCard(String recipeName,String recipieImageUrl){
        this.recipeName = recipeName;
        this.recipeImageUrl = recipieImageUrl;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }
}
