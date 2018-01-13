package itp341.sethi.mehr.finalapp.Controller.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import itp341.sethi.mehr.finalapp.Controller.Activities.IngredientsActivity;
import itp341.sethi.mehr.finalapp.Controller.Activities.ResultsActivity;
import itp341.sethi.mehr.finalapp.Model.Ingredient;
import itp341.sethi.mehr.finalapp.Model.ProductSingleton;
import itp341.sethi.mehr.finalapp.Model.UserSingleton;
import itp341.sethi.mehr.finalapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScanFragment extends Fragment{

    Button buttonSelect;
    Toolbar toolbar;

    ProductSingleton productSingleton;
    UserSingleton user;

    SurfaceView cameraView;

    CameraSource cameraSource;

    SparseArray<Barcode> barcodes;

    private RequestQueue queue;

    private ProgressBar spinner;

    boolean yes;

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scan, container, false);

        //references to views
        cameraView = (SurfaceView) v.findViewById(R.id.preview);
        buttonSelect = (Button) v.findViewById(R.id.button_select);
        spinner=(ProgressBar) v.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        yes = true;

        //get the singletons
        productSingleton = ProductSingleton.getSingleton(getContext());
        user = UserSingleton.getSingleton(getContext());

        //volley stuff
        queue = Volley.newRequestQueue(getContext());

        // Toolbar
        toolbar = (Toolbar) v.findViewById(R.id.toolbar_scan);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        //Make a barcode detector
        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(getActivity())
                        .build();

        productSingleton.clearAllergens();
        productSingleton.clearNutrition();
        productSingleton.clearIngredients();
        productSingleton.clearDiet();

        //Make a camera source to be able to get input from the camera and decode barcodes
        cameraSource = new CameraSource
        .Builder(getActivity(), barcodeDetector)
        .setRequestedPreviewSize(640, 480)
        .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        //whenever barcodes are detected, process it to get a list of strings and update barcodes
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                barcodes = detections.getDetectedItems();
            }
        });

        //when the button is pressed, process the barcode
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (barcodes.size() != 0) {
                    //launch the results activity
                    requestJSONParse(String.format("http://world.openfoodfacts.org/api/v0/product/%s.json", barcodes.valueAt(0).rawValue));
                    if ((productSingleton.getDietsArray().isEmpty() ||
                            productSingleton.getTranslatedArray().isEmpty()
                            || productSingleton.getAllergensArray().isEmpty()) && yes) {
                        spinner.setVisibility(View.VISIBLE);
                    }
                    else {
                        Intent i = new Intent(getActivity().getApplicationContext(), ResultsActivity.class);
                        startActivityForResult(i, 0);
                    }
                }
            }
        });
        return v;
    }

    public void requestJSONParse(String reqURL) {
        JsonObjectRequest request = new JsonObjectRequest(reqURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //get the status
                            String status = response.getString("status_verbose");
                            //if the food item corresponding to the barcode doesn't exist in the database, show an alert
                            if (status.equals("product not found")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("This item does not exist in our system. Please try again.");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick (DialogInterface dialog,
                                                         int which){
                                    };
                                });
                                builder.show();
                                spinner.setVisibility(View.INVISIBLE);
                                return;
                            }
                            // if the food item exists, parse the JSON to get the relevant information
                            // update the user and product singletons accordingly
                            // if the languages don't match, translate before updating
                            else {
                                JSONObject product = (JSONObject) response.getJSONObject("product");

                                String lang = product.getString("lang");
                                String currentLang = Locale.getDefault().getLanguage();

                                String productName = product.getString("product_name");
                                productSingleton.setName(productName);


                                //ingredients
                                productSingleton.clearIngredients();
                                JSONArray ingredientsFromJSON = product.getJSONArray("ingredients_tags");
                                if (ingredientsFromJSON == null){
                                    yes = false;
                                }
                                for (int j = 0; j < ingredientsFromJSON.length(); j++) {
                                    String ingredient = (String) ingredientsFromJSON.getString(j);
                                    Ingredient ingredientObj = new Ingredient();
                                    if (lang.equals(currentLang)) {
                                        productSingleton.addTranslated(ingredient);
                                    } else {
                                        translateJSON(ingredient, currentLang, productSingleton.getTranslatedArray());
                                    }
                                    ingredientObj.setOriginal(ingredient);
                                    productSingleton.addIngredient(ingredientObj);
                                }

                                //allergens
                                productSingleton.clearAllergens();
                                JSONArray allergensFromJSON = product.getJSONArray("allergens_tags");
                                if (allergensFromJSON == null){
                                    yes = false;
                                }
                                for (int j = 0; j < allergensFromJSON.length(); j++) {
                                    String allergen = (String) allergensFromJSON.getString(j);
                                    allergen = allergen.substring(3);
                                    if (lang.equals(currentLang)) {
                                        productSingleton.addAllergen(allergen);
                                    } else {
                                        translateJSON(allergen, currentLang, productSingleton.getAllergensArray());
                                    }
                                }

                                //nutrients
                                productSingleton.clearNutrition();
                                JSONObject nutrientsFromJSON = product.getJSONObject("nutrient_levels");
                                String sugarLevel = nutrientsFromJSON.getString("sugars");
                                if (sugarLevel == null){
                                    productSingleton.getNutritionDict().put("sugars", "");
                                }
                                else{
                                    productSingleton.getNutritionDict().put("sugars", sugarLevel);
                                }
                                String saltLevel = nutrientsFromJSON.getString("salt");
                                if (saltLevel == null){
                                    productSingleton.getNutritionDict().put("salt", "");
                                }
                                else{
                                    productSingleton.getNutritionDict().put("salt", saltLevel);
                                }
                                String fatLevel = nutrientsFromJSON.getString("fat");
                                if (fatLevel == null){
                                    productSingleton.getNutritionDict().put("fat", "");
                                }
                                else{
                                    productSingleton.getNutritionDict().put("fat", fatLevel);
                                }
                                JSONObject nutrimentsFromJSON = product.getJSONObject("nutriments");
                                String proteinLevel = nutrimentsFromJSON.getString("proteins");
                                if (proteinLevel == null){
                                    productSingleton.getNutritionDict().put("proteins", "");
                                }
                                else{
                                    productSingleton.getNutritionDict().put("proteins", proteinLevel);
                                }

                                //diet
                                productSingleton.clearDiet();
                                JSONArray dietsFromJSON = product.getJSONArray("labels_tags");
                                if (dietsFromJSON == null){
                                    yes = false;
                                }
                                for (int j = 0; j < dietsFromJSON.length(); j++) {
                                    String diet = (String) dietsFromJSON.getString(j);
                                    if (lang.equals(currentLang)) {
                                        productSingleton.addDietTag(diet);
                                    } else {
                                        translateJSON(diet, currentLang, productSingleton.getDietsArray());
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(request);
    }

    // update the given array with translated value of the data
    public void translateJSON(String q, String target, final ArrayList<String> addTo){
        JsonObjectRequest request = new JsonObjectRequest(String.format("https://translation.googleapis.com/language/translate/v2?q=%s&target=%s&key=someKey", q, target), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = (JSONObject) response.getJSONObject("data");
                            JSONArray translations = data.getJSONArray("translations");
                            JSONObject translation = translations.getJSONObject(0);
                            String translated = translation.getString("translatedText");
                            addTo.add(translated);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        spinner.setVisibility(View.INVISIBLE);
        productSingleton.clearAllergens();
        productSingleton.clearNutrition();
        productSingleton.clearIngredients();
        productSingleton.clearDiet();
    }

}
