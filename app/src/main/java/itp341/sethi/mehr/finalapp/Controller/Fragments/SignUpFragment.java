package itp341.sethi.mehr.finalapp.Controller.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import itp341.sethi.mehr.finalapp.Constants.StringValues;
import itp341.sethi.mehr.finalapp.Model.UserSingleton;
import itp341.sethi.mehr.finalapp.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    ImageView profileImage;
    Button profileImageButton;

    EditText editName;
    EditText editAge;

    CheckBox checkMilk;
    CheckBox checkPeanuts;
    CheckBox checkSoybean;
    CheckBox checkGarlic;
    CheckBox checkEggs;
    CheckBox checkWheat;
    CheckBox checkGluten;
    CheckBox checkFish;
    CheckBox checkCrustacean;
    CheckBox checkTreeNut;
    CheckBox checkOats;
    CheckBox checkRice;
    CheckBox checkSulfites;

    Spinner spinnerDiet;

    CheckBox checkDiabetes;
    CheckBox checkCeliacDisease;
    CheckBox checkOsteoporosis;
    CheckBox checkKidneyDisease;
    CheckBox checkGout;
    CheckBox checkHighCholesterol;
    CheckBox checkLactoseIntolerance;

    Toolbar toolbar;

    Button buttonContinue;

    UserSingleton user;

    String[] dietChoicesArray;

    public SignUpFragment() {
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
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Toolbar
        toolbar = (Toolbar) v.findViewById(R.id.toolbar_sign_up);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        //references to views
        profileImage = (ImageView) v.findViewById(R.id.image_profile_sign_up);
        profileImageButton = (Button) v.findViewById(R.id.button_profile_sign_up);

        editName = (EditText) v.findViewById(R.id.edit_name);
        editAge = (EditText) v.findViewById(R.id.edit_age);

        checkMilk = (CheckBox) v.findViewById(R.id.milk);
        checkPeanuts = (CheckBox) v.findViewById(R.id.peanuts);
        checkSoybean = (CheckBox) v.findViewById(R.id.soybeans);
        checkGarlic = (CheckBox) v.findViewById(R.id.garlic);
        checkEggs = (CheckBox) v.findViewById(R.id.eggs);
        checkWheat = (CheckBox) v.findViewById(R.id.wheat);
        checkGluten = (CheckBox) v.findViewById(R.id.gluten);
        checkFish = (CheckBox) v.findViewById(R.id.fish);
        checkCrustacean = (CheckBox) v.findViewById(R.id.crustacean);
        checkTreeNut = (CheckBox) v.findViewById(R.id.tree_nut);
        checkOats = (CheckBox) v.findViewById(R.id.oats);
        checkRice = (CheckBox) v.findViewById(R.id.rice);
        checkSulfites = (CheckBox) v.findViewById(R.id.sulfites);

        spinnerDiet = (Spinner) v.findViewById(R.id.spinner_diet_preference);

        checkDiabetes = (CheckBox) v.findViewById(R.id.diabetes);
        checkCeliacDisease = (CheckBox) v.findViewById(R.id.celiac_disease);
        checkOsteoporosis = (CheckBox) v.findViewById(R.id.osteoporosis);
        checkKidneyDisease = (CheckBox) v.findViewById(R.id.kidney_diseases);
        checkGout = (CheckBox) v.findViewById(R.id.gout);
        checkHighCholesterol = (CheckBox) v.findViewById(R.id.high_cholesterol);
        checkLactoseIntolerance = (CheckBox) v.findViewById(R.id.lactose_intolerance);

        buttonContinue = (Button) v.findViewById(R.id.button_continue);

        dietChoicesArray = getResources().getStringArray(R.array.diet_preference_array);

        buttonContinue.setEnabled(false);

        //if the user doesn't have a profile, use default
        user = UserSingleton.getSingleton(getActivity());
        if (user.getProfileImage() == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(
                    getActivity().getApplicationContext().getResources(),
                    R.drawable.placeholder_profile);
            profileImage.setImageBitmap(bitmap);
            user.setProfileImage(bitmap);
        }
        //otherwise use the locally stored picture
        else{
            profileImage.setImageBitmap(user.getProfileImage());
        }

        //setting up the UI to match the info stored in the user object
        if (user.getName() != null){
            editName.setText(user.getName());
            buttonContinue.setEnabled(true);
        }

        if (user.getAge() != 0){
            editAge.setText(String.valueOf(user.getAge()));
            buttonContinue.setEnabled(true);
        }

        for (int i=0; i<user.getAllergiesArray().size(); i++){
            if (StringValues.milk.equals(user.getAllergiesArray().get(i))){
                checkMilk.setChecked(true);
            }
            if (StringValues.peanut.equals(user.getAllergiesArray().get(i))){
                checkPeanuts.setChecked(true);
            }
            if (StringValues.soybean.equals(user.getAllergiesArray().get(i))){
                checkSoybean.setChecked(true);
            }
            if (StringValues.garlic.equals(user.getAllergiesArray().get(i))){
                checkGarlic.setChecked(true);
            }
            if (StringValues.egg.equals(user.getAllergiesArray().get(i))){
                checkEggs.setChecked(true);
            }
            if (StringValues.wheat.equals(user.getAllergiesArray().get(i))){
                checkWheat.setChecked(true);
            }
            if (StringValues.gluten.equals(user.getAllergiesArray().get(i))){
                checkGluten.setChecked(true);
            }
            if (StringValues.fish.equals(user.getAllergiesArray().get(i))){
                checkFish.setChecked(true);
            }
            if (StringValues.crustacean.equals(user.getAllergiesArray().get(i))){
                checkCrustacean.setChecked(true);
            }
            if (StringValues.treeNut.equals(user.getAllergiesArray().get(i))){
                checkTreeNut.setChecked(true);
            }
            if (StringValues.oat.equals(user.getAllergiesArray().get(i))){
                checkOats.setChecked(true);
            }
            if (StringValues.rice.equals(user.getAllergiesArray().get(i))){
                checkRice.setChecked(true);
            }
            if (StringValues.sulfites.equals(user.getAllergiesArray().get(i))){
                checkSulfites.setChecked(true);
            }
        }

        for (int i=0; i<user.getMedicalArray().size(); i++) {
            if (StringValues.diabetes.equals(user.getMedicalArray().get(i))) {
                checkDiabetes.setChecked(true);
            }
            if (StringValues.celiacDisease.equals(user.getMedicalArray().get(i))) {
                checkCeliacDisease.setChecked(true);
            }
            if (StringValues.osteoporosis.equals(user.getMedicalArray().get(i))) {
                checkOsteoporosis.setChecked(true);
            }
            if (StringValues.kidneyDisease.equals(user.getMedicalArray().get(i))) {
                checkKidneyDisease.setChecked(true);
            }
            if (StringValues.gout.equals(user.getMedicalArray().get(i))) {
                checkGout.setChecked(true);
            }
            if (StringValues.highCholesterol.equals(user.getMedicalArray().get(i))) {
                checkHighCholesterol.setChecked(true);
            }
            if (StringValues.lactoseIntolerance.equals(user.getMedicalArray().get(i))) {
                checkLactoseIntolerance.setChecked(true);
            }
        }

        if (user.getDietPreference() != null) {
            for (int i=0; i<dietChoicesArray.length; i++){
                if (user.getDietPreference().equals(dietChoicesArray[i])){
                    spinnerDiet.setSelection(i);
                }
            }
        }

        //if the profile photo button is clicked, launch the take picture intent
        //check if the continue button should be enabled
        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
                buttonEnabled();
            }
        });

        //if the text in edit name changes, check if the continue button should be enabled
        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonEnabled();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //if the text in edit age changes, check if the continue button should be enabled
        editAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonEnabled();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //if the user changes diet preference, update the model
        //save the model locally
        //check if the continue button should be enabled
        spinnerDiet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                user.setDietPreference(getResources().getStringArray(R.array.diet_preference_array)[i]);
                buttonEnabled();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // if the continue button is clicked, update the model
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set name and age
                user.setName(editName.getText().toString());
                user.setAge(Integer.valueOf(editAge.getText().toString()));

                user.getMedicalArray().clear();
                user.getAllergiesArray().clear();

                //check for any allergies
                if (checkMilk.isChecked()){
                    user.addAllergy(StringValues.milk);
                }
                if (checkPeanuts.isChecked()){
                    user.addAllergy(StringValues.peanut);
                }
                if (checkSoybean.isChecked()){
                    user.addAllergy(StringValues.soybean);
                }
                if (checkGarlic.isChecked()){
                    user.addAllergy(StringValues.garlic);
                }
                if (checkEggs.isChecked()){
                    user.addAllergy(StringValues.egg);
                }
                if (checkWheat.isChecked()){
                    user.addAllergy(StringValues.wheat);
                }
                if (checkGluten.isChecked()){
                    user.addAllergy(StringValues.gluten);
                }
                if (checkFish.isChecked()){
                    user.addAllergy(StringValues.fish);
                }
                if (checkCrustacean.isChecked()){
                    user.addAllergy(StringValues.crustacean);
                }
                if (checkTreeNut.isChecked()){
                    user.addAllergy(StringValues.treeNut);
                }
                if (checkOats.isChecked()){
                    user.addAllergy(StringValues.oat);
                }
                if (checkRice.isChecked()){
                    user.addAllergy(StringValues.rice);
                }
                if (checkSulfites.isChecked()){
                    user.addAllergy(StringValues.sulfites);
                }

                //check for medical conditions
                if (checkDiabetes.isChecked()){
                    user.addMedical(StringValues.diabetes);
                }
                if (checkCeliacDisease.isChecked()){
                    user.addMedical(StringValues.celiacDisease);
                }
                if (checkOsteoporosis.isChecked()){
                    user.addMedical(StringValues.osteoporosis);
                }
                if (checkKidneyDisease.isChecked()){
                    user.addMedical(StringValues.kidneyDisease);
                }
                if (checkGout.isChecked()){
                    user.addMedical(StringValues.gout);
                }
                if (checkHighCholesterol.isChecked()){
                    user.addMedical(StringValues.highCholesterol);
                }
                if (checkLactoseIntolerance.isChecked()){
                    user.addMedical(StringValues.lactoseIntolerance);
                }

                user.save(getActivity().getApplicationContext());

                //finish activity
                getActivity().finish();

            }
        });

        return v;
    }

    //check if the continue button should be enabled based on the length of edit name and edit age
    private void buttonEnabled(){
        if (editName.getText().toString().length() > 0 && editAge.getText().toString().length() > 0) {
            buttonContinue.setEnabled(true);
        }
        else{
            buttonContinue.setEnabled(false);
        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        ((AppCompatActivity)getActivity()).getMenuInflater().inflate(R.menu.menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.menu_settings) {
//            Intent i = new Intent(getActivity(), LangActivity.class);
//            startActivity(i);
//        }
//        getActivity().finish();
//        return super.onOptionsItemSelected(item);
//    }

    //launch an intent that takes a picture
    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    //when the picture intent comes back, update the model
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            user.setProfileImage(imageBitmap);
            profileImage.setImageBitmap(imageBitmap);
        }
    }
}
