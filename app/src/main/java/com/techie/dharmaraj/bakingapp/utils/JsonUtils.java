package com.techie.dharmaraj.bakingapp.utils;

import com.techie.dharmaraj.bakingapp.R;
import com.techie.dharmaraj.bakingapp.data.Ingredients;
import com.techie.dharmaraj.bakingapp.data.RecipeCard;
import com.techie.dharmaraj.bakingapp.data.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dharmaraj on 08-11-2017.
 */

public final class JsonUtils {

    private static final String INGREDIENTS_JSON = "ingredients";
    private static final String QUANTITY_JSON = "quantity";
    private static final String MEASURE_JSON = "measure";
    private static final String INGREDIENT_JSON = "ingredient";
    private static final String STEPS_JSON = "steps";
    private static final String STEPS_ID_JSON = "id";
    private static final String STEP_SHORT_DESCRIPTION_JSON = "shortDescription";
    private static final String STEP_DESCRIPTION_JSON = "description";
    private static final String STEP_VIDEO_URL_JSON = "videoURL";
    private static final String STEP_THUMBNAIL_URL_JSON = "thumbnailURL";
    public static ArrayList<JSONObject> mJsonObjects;

    /**
     * this method returns all the available recipe names in a list
     *
     * @return List<String>
     */
    public static ArrayList<RecipeCard> getRecipesCardDetails() {
        ArrayList<RecipeCard> namesArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < mJsonObjects.size(); i++) {
                JSONObject recipeJsonObject = mJsonObjects.get(i);
                String name = recipeJsonObject.getString("name");
                String url = recipeJsonObject.getString("image");
                namesArrayList.add(new RecipeCard(name,url));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return namesArrayList;
    }

    /**
     * @param recipeIndex the recipe's index which you want to get the steps
     * @return List<String> containing step's shortDescription
     */
    public static ArrayList<String> getRecipeStepsDescription(int recipeIndex) {
        ArrayList<String> shortDescriptionArrayList = new ArrayList<>();
        JSONObject recipeJsonObject = mJsonObjects.get(recipeIndex);
        try {
            JSONArray recipeStepsJsonArray = recipeJsonObject.getJSONArray("steps");
            for (int i = 0; i < recipeStepsJsonArray.length(); i++) {
                JSONObject object = recipeStepsJsonArray.getJSONObject(i);
                String shortDescription = object.getString("shortDescription");
                shortDescriptionArrayList.add(shortDescription);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shortDescriptionArrayList;
    }

    /**
     * get the recipe steps of current recipe
     * @param recipeIndex current recipe which steps are needed
     * @return arrayList of Steps
     */
    public static ArrayList<Steps> getRecipeSteps(int recipeIndex){
        ArrayList<Steps> stepsArrayList = new ArrayList<>();
        JSONObject recipeJsonObject = mJsonObjects.get(recipeIndex);
        try {
            JSONArray recipeStepsJsonArray = recipeJsonObject.getJSONArray(STEPS_JSON);
            for (int i = 0; i < recipeStepsJsonArray.length(); i++) {
                JSONObject object = recipeStepsJsonArray.getJSONObject(i);
                int id = object.getInt(STEPS_ID_JSON);
                String shortDescription = object.getString(STEP_SHORT_DESCRIPTION_JSON);
                String description = object.getString(STEP_DESCRIPTION_JSON);
                String videoURL = object.getString(STEP_VIDEO_URL_JSON);
                String thumbnailURL = object.getString(STEP_THUMBNAIL_URL_JSON);
                stepsArrayList.add(new Steps(id,shortDescription,description,videoURL,thumbnailURL));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stepsArrayList;
    }

    /**
     * get the recipe ingredients of current recipe
     * @param recipeIndex current recipe which ingredients are needed
     * @return arrayList of Steps
     */
    public static ArrayList<Ingredients> getRecipeIngredents(int recipeIndex){
        ArrayList<Ingredients> ingredientsArrayList = new ArrayList<>();
        JSONObject recipeJsonObject = mJsonObjects.get(recipeIndex);
        try {
            JSONArray recipeIngredientsJsonArray = recipeJsonObject.getJSONArray(INGREDIENTS_JSON);
            for (int i = 0; i < recipeIngredientsJsonArray.length(); i++){
                JSONObject object = recipeIngredientsJsonArray.getJSONObject(i);
                double quantity = object.getDouble(QUANTITY_JSON);
                String measure = object.getString(MEASURE_JSON);
                String ingredient = object.getString(INGREDIENT_JSON);
                ingredientsArrayList.add(new Ingredients(quantity,measure,ingredient));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ingredientsArrayList;
    }

    /**
     * this method will make the response string to jsonArray Object and
     * traverse all the JsonObject from it and store them in a  ArrayList Variable recipeJsonObjects and returns it
     */
    public static ArrayList<JSONObject> getRecipeJsonObjects(String response) {
        ArrayList<JSONObject> recipeJsonObjects = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeJsonObject = jsonArray.getJSONObject(i);
                recipeJsonObjects.add(recipeJsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipeJsonObjects;
    }

    public static void setmJsonObjects(ArrayList<JSONObject> mJsonObjects) {
        JsonUtils.mJsonObjects = mJsonObjects;
    }

    /**
     * get the image of the recipe
     * @param position
     * @return image resource id of the recipe
     */
    public static int getRecipeImageResourceId(int position){
        switch (position){
            case 0:
                return R.drawable.nutella_pie;
            case 1:
                return R.drawable.brownie;
            case 2:
                return R.drawable.yellow_cake;
            case 3:
                return R.drawable.cheesecake;
            default:
                return 0;
        }
    }
}
