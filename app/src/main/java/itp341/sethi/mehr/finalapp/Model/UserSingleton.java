package itp341.sethi.mehr.finalapp.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import itp341.sethi.mehr.finalapp.R;

import static itp341.sethi.mehr.finalapp.Constants.StringValues.celiacDisease;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.crustacean;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.egg;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.fish;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.gluten;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.gout;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.highCholesterol;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.lactoseIntolerance;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.milk;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.oat;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.osteoporosis;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.soybean;
import static itp341.sethi.mehr.finalapp.Constants.StringValues.treeNut;

/**
 * Created by mehrsethi on 11/17/17.
 */

public class UserSingleton {

    private static UserSingleton userSingleton = null;

    private Bitmap profileImage;
    private String name;
    private int age;
    ArrayList<String> allergiesArray;
    ArrayList<String> medicalArray;
    ArrayList<String> yesFoods;
    ArrayList<String> noFoods;
    private String dietPreference;

    //constructor
    private UserSingleton(Context c){
        name = "";
        age = 0;
        allergiesArray = new ArrayList<String>();
        medicalArray = new ArrayList<String>();
        yesFoods = new ArrayList<String>();
        noFoods = new ArrayList<String>();

        //read local json for an existing user's information
        try {
            InputStream inputStream = c.openFileInput("userInfo.json");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                String ret = stringBuilder.toString();
                try{
                    JSONObject outerObject = new JSONObject(ret);
                    name = outerObject.getString("name");
                    age = Integer.valueOf(outerObject.getString("age"));

                    JSONArray tempAllergies = outerObject.getJSONArray("allergies");
                    for (int i=0; i<tempAllergies.length(); i++){
                        allergiesArray.add(tempAllergies.get(i).toString());
                    }

                    JSONArray tempMedical = outerObject.getJSONArray("medical");
                    for (int i=0; i<tempMedical.length(); i++){
                        medicalArray.add(tempMedical.get(i).toString());
                    }

                    JSONArray tempYesFoods = outerObject.getJSONArray("yesFood");
                    for (int i=0; i<tempYesFoods.length(); i++){
                        yesFoods.add(tempYesFoods.get(i).toString());
                    }

                    JSONArray tempNoFoods = outerObject.getJSONArray("noFood");
                    for (int i=0; i<tempNoFoods.length(); i++){
                        noFoods.add(tempNoFoods.get(i).toString());
                    }

                    dietPreference = outerObject.getString("diet");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //get singleton
    public static UserSingleton getSingleton(Context c){
        if (userSingleton == null){
            userSingleton = new UserSingleton(c);
        }
        return userSingleton;
    }

    //getters
    public ArrayList<String> getMedicalArray() {
        return medicalArray;
    }

    public String getDietPreference() {
        return dietPreference;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public ArrayList<String> getAllergiesArray() {
        return allergiesArray;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public ArrayList<String> getYesFoods() {
        return yesFoods;
    }

    public ArrayList<String> getNoFoods() {
        return noFoods;
    }

    //setters
    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public void setDietPreference(String dietPreference) {
        this.dietPreference = dietPreference;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    //add methods
    public void addAllergy(String allergy){
        allergiesArray.add(allergy);
    }

    public void addMedical(String condition){
        medicalArray.add(condition);
    }

    //write the user's information to a json file
    public void save(Context c){
        try {
            JSONObject outerObject = new JSONObject();
            outerObject.put("name", name);
            outerObject.put("age", age);

            JSONArray allergies = new JSONArray(allergiesArray);
            outerObject.put("allergies", allergies);

            JSONArray medical = new JSONArray(medicalArray);
            outerObject.put("medical", medical);

            JSONArray yesFood = new JSONArray(yesFoods);
            outerObject.put("yesFood", yesFood);

            JSONArray noFood = new JSONArray(noFoods);
            outerObject.put("noFood", noFood);

            outerObject.put("diet", dietPreference);

            String JSONtoString = outerObject.toString();

            FileOutputStream outputStream;
            try {
                outputStream = c.openFileOutput("userInfo.json", Context.MODE_PRIVATE);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                outputStreamWriter.write(JSONtoString);
                outputStreamWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //loop through the medical conditions array
    //and figure out which foods should be eaten/avoided by that person
    //edit both yesfoods and nofoods
    //and add to the allergens list, if necessary
    public void editYesNoFood(){
        noFoods.clear();
        yesFoods.clear();
        for (int i=0; i<medicalArray.size(); i++){
            if (medicalArray.get(i).equals(gout)){
                boolean found1 = false;
                for (int j=0; j<allergiesArray.size(); j++){
                    if (allergiesArray.get(j).equals(fish)){
                        found1 = true;
                    }
                }
                if (!found1){
                    addAllergy(fish);
                }

                boolean found2 = false;
                for (int j=0; j<allergiesArray.size(); j++){
                    if (allergiesArray.get(j).equals(crustacean)){
                        found2 = true;
                    }
                }
                if (!found2){
                    addAllergy(crustacean);
                }

                noFoods.add("high-fructose-corn-syrup");
                noFoods.add("anchov");
                noFoods.add("sardine");
                noFoods.add("mussel");
                noFoods.add("codfish");
                noFoods.add("scallop");
                noFoods.add("trout");
                noFoods.add("bacon");
                noFoods.add("turkey");
                noFoods.add("veal");
                noFoods.add("venison");
                noFoods.add("liver");
                noFoods.add("caffeine");
            }
            if (medicalArray.get(i).equals(highCholesterol)){
                boolean found1 = false;
                for (int j=0; j<allergiesArray.size(); j++){
                    if (allergiesArray.get(j).equals(oat)){
                        found1 = true;
                    }
                }
                if (!found1){
                    yesFoods.add(oat);
                }

                boolean found2 = false;
                for (int j=0; j<allergiesArray.size(); j++){
                    if (allergiesArray.get(j).equals(soybean)){
                        found2 = true;
                    }
                }
                if (!found2){
                    yesFoods.add(soybean);
                }

                boolean found3 = false;
                for (int j=0; j<allergiesArray.size(); j++){
                    if (allergiesArray.get(j).equals(treeNut)){
                        found3 = true;
                    }
                }
                if (!found3){
                   yesFoods.add(treeNut);
                }

                boolean found4 = false;
                for (int j=0; j<allergiesArray.size(); j++){
                    if (allergiesArray.get(j).equals(egg)){
                        found4 = true;
                    }
                }
                if (!found4){
                    noFoods.add(egg);
                }

                yesFoods.add("porridge");
                yesFoods.add("barley");
                yesFoods.add("bean");
                yesFoods.add("lentil");
                yesFoods.add("tofu");
            }
            if (medicalArray.get(i).equals(lactoseIntolerance)) {
                boolean found = false;
                for (int j = 0; j < allergiesArray.size(); j++) {
                    if (allergiesArray.get(j).equals(milk)) {
                        found = true;
                    }
                }
                if (!found) {
                    addAllergy(milk);
                }
            }
            if (medicalArray.get(i).equals(osteoporosis)) {
                boolean found1 = false;
                for (int j = 0; j < allergiesArray.size(); j++) {
                    if (allergiesArray.get(j).equals(milk)) {
                        found1 = true;
                    }
                }
                if (!found1) {
                    yesFoods.add(milk);
                }

                boolean found2 = false;
                for (int j = 0; j < allergiesArray.size(); j++) {
                    if (allergiesArray.get(j).equals(fish)) {
                        found2 = true;
                    }
                }
                if (!found2) {
                    yesFoods.add("tuna");
                    yesFoods.add("sardine");
                    yesFoods.add("salmon");
                    yesFoods.add("mackerel");
                }
            }
            if (medicalArray.get(i).equals(celiacDisease)) {
                boolean found = false;
                for (int j = 0; j < allergiesArray.size(); j++) {
                    if (allergiesArray.get(j).equals(gluten)) {
                        found = true;
                    }
                }
                if (!found) {
                    noFoods.add(gluten);
                }
            }
        }
    }

}
