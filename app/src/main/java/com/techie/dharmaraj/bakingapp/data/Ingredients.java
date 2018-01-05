package com.techie.dharmaraj.bakingapp.data;

/**
 * Class which stores all the details of a ingredient in a object
 */
public class Ingredients {
    public double quantity;
    public String measure;
    public String ingredient;
    public Ingredients(double quantity,String measure, String ingredients){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredients;
    }

    /**
     * this method returns a readable single line which contains the measurement and quantity of
     * ingredient needed
     * @return
     */
    public String getStringIngredient(){
        return String.valueOf(quantity) +
                " "+
                getCorrectMeasure(measure) +
                " of " +
                ingredient;
    }

    /**
     * this returns a readable measurement from the given measure
     */
    private String  getCorrectMeasure(String s){
            switch (s){
                case "CUP":
                    return "cup";
                case "TBLSP":
                    return "tablespoon";
                case "TSP":
                    return "teaspoon";
                case "K":
                    return "kilo";
                case "G":
                    return "gram";
                case "OZ":
                    return "ounce ";
                case "UNIT":
                    return "unit ";
                default:
                    return "";
            }
    }
}
