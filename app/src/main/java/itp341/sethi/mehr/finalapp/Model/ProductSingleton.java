package itp341.sethi.mehr.finalapp.Model;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static itp341.sethi.mehr.finalapp.Constants.StringValues.crustacean;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.diabetes;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.fish;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.gout;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.highCholesterol;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.kidneyDisease;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.osteoporosis;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.peanut;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.treeNut;

/**
 * Created by mehrsethi on 11/17/17.
 */

public class ProductSingleton {
    private ArrayList<Ingredient> ingredientsArray;
    private ArrayList<String> translatedArray;
    private ArrayList<String> allergensArray;
    private ArrayList<String> dietsArray;
    private ArrayList<String> questionableArray;
    private Map<String, String> nutritionDict;
    private String name;

    private int suitability;

    private static ProductSingleton productSingleton;

    private static UserSingleton user;


    //constructor
    private ProductSingleton(Context c){
        ingredientsArray = new ArrayList<>();
        allergensArray = new ArrayList<>();
        translatedArray = new ArrayList<>();
        dietsArray = new ArrayList<>();
        questionableArray = new ArrayList<>();
        nutritionDict = new HashMap<String, String>();
        suitability = 100;
        name = "";
        user = UserSingleton.getSingleton(c);
    }

    //get the singleton
    public static ProductSingleton getSingleton(Context c){
        if (productSingleton == null){
            productSingleton = new ProductSingleton(c);
        }
        return productSingleton;
    }


    //getters
    public int getSuitability() {
        return suitability;
    }

    public ArrayList<Ingredient> getIngredientsArray() {
        return ingredientsArray;
    }

    public ArrayList<String> getAllergensArray() {
        return allergensArray;
    }

    public ArrayList<String> getTranslatedArray() {
        return translatedArray;
    }

    public ArrayList<String> getDietsArray() {
        return dietsArray;
    }

    public ArrayList<String> getQuestionableArray() {
        return questionableArray;
    }

    public Map<String, String> getNutritionDict() {
        return nutritionDict;
    }

    public String getName() {
        return name;
    }

    //setters
    public void setNutritionDict(Map<String, String> nutritionDict) {
        this.nutritionDict = nutritionDict;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSuitability(int suitability) {
        this.suitability = suitability;
    }


    //clear methods
    public void clearIngredients(){
        ingredientsArray.clear();
        translatedArray.clear();
    }

    public void clearAllergens(){
        allergensArray.clear();
    }

    public void clearNutrition(){
        nutritionDict.clear();
    }

    public void clearDiet(){
        dietsArray.clear();
    }

    //add methods
    public void addIngredient(Ingredient ingredient){
        ingredientsArray.add(ingredient);
    }

    public void addTranslated(String translate){
        translatedArray.add(translate);
    }

    public void addAllergen(String allergen){
        allergensArray.add(allergen);
    }

    public void addDietTag(String dietTag){
        dietsArray.add(dietTag);
    }


    //calculate suitability based on the user's diet preferences
    public ArrayList<String> calculateSuitability(){
        //set up for calculation
        suitability = 100;
        questionableArray.clear();
        user.medicalArray.clear();
        user.editYesNoFood();

        //diet preference
        boolean found = false;
        if (!user.getDietPreference().equals("No preference")){
            for (int i=0; i<dietsArray.size(); i++){
                if (dietsArray.get(i).toLowerCase().contains(user.getDietPreference().toLowerCase())){
                    found = true;
                }
            }
        }
        else{
            found = true;
        }
        if (!found){
            suitability -=50;
            questionableArray.add(String.format("Not %s", user.getDietPreference()));
            if (suitability < 0){
                suitability = 0;
            }
        }

        //loop through the user's allergies, and see if any of them appear in the allergens array
        for (int j=0; j<allergensArray.size(); j++){
            ArrayList<String> array = user.allergiesArray;
            for (int i=0; i<user.allergiesArray.size(); i++){
                if (user.allergiesArray.get(i).equals(treeNut)){
                    String[] tempArray = new String[] {"almond", "pinenut", "cashew",
                            "chestnut", "hazelnut", "pistacio", "walnut", "macadamia", "pecan", "nut"};
                    boolean found1 = false;
                    ArrayList<String> foundArray = new ArrayList<>();
                    for (int k=0; k<tempArray.length; k++){
                        if (allergensArray.get(j).toLowerCase().contains(tempArray[k]) &&
                                (!allergensArray.get(j).toLowerCase().contains(peanut))){
                            found1 = true;
                        }
                    }
                    if (found1){
                        foundArray.add(allergensArray.get(j));
                        suitability -= 25;
                        for (int l=0; l<foundArray.size(); l++){
                            questionableArray.add(foundArray.get(l));
                        }
                        if (suitability < 0){
                            suitability = 0;
                        }
                    }
                }
                else if (user.allergiesArray.get(i).equals(fish)){
                    String[] tempArray = new String[] {"anchov", "bass", "catfish", "cod", "flounder",
                    "grouper", "haddock", "hake", "halibut", "herring", "mahi mahi", "perch", "pike",
                    "pollock", "salmon", "snapper", "sole", "swordfish", "tilapia", "trout", "tuna", "walleye"};
                    boolean found1 = false;
                    ArrayList<String> foundArray = new ArrayList<>();
                    for (int k=0; k<tempArray.length; k++){
                        if (allergensArray.get(j).toLowerCase().contains(tempArray[k])){
                            found1 = true;
                        }
                    }
                    if (found1){
                        foundArray.add(allergensArray.get(j));
                        suitability -= 25;
                        for (int l=0; l<foundArray.size(); l++){
                            questionableArray.add(foundArray.get(l));
                        }
                        if (suitability < 0){
                            suitability = 0;
                        }
                    }
                }
                else if (user.allergiesArray.get(i).equals(crustacean)){
                    String[] tempArray = new String[] {"crab", "lobster", "shrimp"};
                    boolean found1 = false;
                    ArrayList<String> foundArray = new ArrayList<>();
                    for (int k=0; k<tempArray.length; k++){
                        if (allergensArray.get(j).toLowerCase().contains(tempArray[k])){
                            found1 = true;
                        }
                    }
                    if (found1){
                        foundArray.add(allergensArray.get(j));
                        suitability -= 25;
                        for (int l=0; l<foundArray.size(); l++){
                            questionableArray.add(foundArray.get(l));
                        }
                        if (suitability < 0){
                            suitability = 0;
                        }
                    }
                }
                else{
                    if (allergensArray.get(j).toLowerCase().contains(user.getAllergiesArray().get(i).toLowerCase())){
                        suitability -= 25;
                        questionableArray.add(allergensArray.get(j));
                        if (suitability < 0){
                            suitability = 0;
                        }
                    }
                }
            }
        }

        //loop through the user's noFoods and see if any of them appear in ingredients
        for (int i=0; i<user.getNoFoods().size(); i++){
            for (int j=0; j<translatedArray.size(); j++){
                if (translatedArray.get(j).contains(user.getNoFoods().get(i))){
                    suitability -= 10;
                    questionableArray.add(translatedArray.get(j));
                    if (suitability < 0){
                        suitability = 0;
                    }
                }
            }
        }

        //loop through the user's yesFoods and see if any of them appear in ingredients
        for (int i=0; i<ingredientsArray.size(); i++){
            for (int j=0; j<user.yesFoods.size(); j++){
                if (translatedArray.get(i).contains(user.getNoFoods().get(j))){
                    suitability += 10;
                    if (suitability > 100){
                        suitability = 100;
                    }
                }
            }
        }

        //accounting for medical conditions of the user using nutrients -- besides ingredients
        for (int i=0; i<user.getMedicalArray().size(); i++){
            if (user.getMedicalArray().get(i).equals(diabetes)){
                if (nutritionDict.get("sugars").equals("high")){
                    suitability -= 10;
                    questionableArray.add("high sugar");
                    if (suitability < 0){
                        suitability = 0;
                    }
                }
            }
            if (user.getMedicalArray().get(i).equals(kidneyDisease)){
                if (Integer.valueOf(nutritionDict.get("proteins")) > 18){
                    suitability -= 10;
                    questionableArray.add("high protein");
                    if (suitability < 0){
                        suitability = 0;
                    }
                }
            }
            if (user.getMedicalArray().get(i).equals(kidneyDisease) || user.getMedicalArray().get(i).equals(highCholesterol)){
                if (nutritionDict.get("salt").equals("high")){
                    boolean found1 = false;
                    for (int j=0; j<questionableArray.size(); j++){
                        if (questionableArray.get(j).equals("high salt")){
                            found1 = true;
                        }
                    }
                    if (!found1){
                        suitability -= 10;
                        questionableArray.add("high salt");
                        if (suitability < 0){
                            suitability = 0;
                        }
                    }
                }
            }
            if (user.getMedicalArray().get(i).equals(osteoporosis) || user.getMedicalArray().get(i).equals(highCholesterol)
                    || user.getMedicalArray().get(i).equals(gout)){
                if (nutritionDict.get("fat").equals("high")){
                    boolean found1 = false;
                    for (int j=0; j<questionableArray.size(); j++){
                        if (questionableArray.get(j).equals("high fat")){
                            found1 = true;
                        }
                    }
                    if (!found1){
                        suitability -= 10;
                        questionableArray.add("high fat");
                        if (suitability < 0){
                            suitability = 0;
                        }
                    }
                }
            }
            if (user.getMedicalArray().get(i).equals(gout)){
                if (Integer.valueOf(nutritionDict.get("proteins")) > 18){
                    suitability += 10;
                    if (suitability > 100){
                        suitability = 100;
                    }
                }
            }
        }
        return questionableArray;
    }
}