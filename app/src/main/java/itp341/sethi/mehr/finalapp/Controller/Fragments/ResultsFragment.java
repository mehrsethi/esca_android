package itp341.sethi.mehr.finalapp.Controller.Fragments;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import itp341.sethi.mehr.finalapp.Controller.Activities.IngredientsActivity;
import itp341.sethi.mehr.finalapp.Controller.Activities.ResultsActivity;
import itp341.sethi.mehr.finalapp.Controller.Activities.ScanActivity;
import itp341.sethi.mehr.finalapp.Model.ProductSingleton;
import itp341.sethi.mehr.finalapp.Model.UserSingleton;
import itp341.sethi.mehr.finalapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultsFragment extends Fragment {

    Button viewAll;
    Button scanAnother;
    ListView questionableIngredientsList;
    ImageView imageSuitabilityOverlay;
    ImageView imageMarginBar;
    TextView textQuestionable;

    Toolbar toolbar;

    ArrayList<String> questionableIngredients;
    ProductSingleton product;
    UserSingleton user;

    ArrayAdapter adapter;

    public ResultsFragment() {
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
        View v = inflater.inflate(R.layout.fragment_results, container, false);

        // Toolbar
        toolbar = (Toolbar) v.findViewById(R.id.toolbar_results);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        //references to views
        viewAll = (Button) v.findViewById(R.id.button_view_all);
        scanAnother = (Button) v.findViewById(R.id.button_scan_another);
        questionableIngredientsList = (ListView) v.findViewById(R.id.list_questionable_ingredients);
        imageSuitabilityOverlay = (ImageView) v.findViewById(R.id.image_suitability_overlay);
        imageMarginBar = (ImageView) v.findViewById(R.id.image_margin_bar);
        textQuestionable = (TextView) v.findViewById(R.id.text_questionable_ingredients);

        //get the singletons
        product = ProductSingleton.getSingleton(getContext());
        user = UserSingleton.getSingleton(getContext());

        //update the listview with the ingredients that don't match the user's dietary preferences
        //launch the results activity
        questionableIngredients = new ArrayList<>();
        questionableIngredients = product.calculateSuitability();

        //change the size of the overlaying views to represent the suitability fo that product for the user
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int widthContentView = displaymetrics.widthPixels;
        final float scale = getContext().getResources().getDisplayMetrics().density;

        int pixelsS = (int) (((((widthContentView/scale-32)/100.0)*product.getSuitability())+16.0)* scale + 0.5f);
        int pixelsW = (int) (widthContentView-pixelsS-(16*scale+0.5f));
        int pixelsH = (int) (120 * scale + 0.5f);
        int pixelsT = (int) (toolbar.getHeight()+16 * scale +0.5f);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(pixelsW, pixelsH);
        layoutParams.topMargin=pixelsT;
        layoutParams.setMarginStart(pixelsS);
        imageSuitabilityOverlay.setLayoutParams(layoutParams);

        int pixelsH2 = (int) (128 * scale + 0.5f);
        int pixelsT2 = (int) (toolbar.getHeight()+12 * scale +0.5f);
        RelativeLayout.LayoutParams layoutParams2 =new RelativeLayout.LayoutParams((int)(5*scale+0.5f), pixelsH2);
        layoutParams2.topMargin=pixelsT2;
        layoutParams2.setMarginStart(pixelsS);
        imageMarginBar.setLayoutParams(layoutParams2);

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, questionableIngredients);
        questionableIngredientsList.setAdapter(adapter);

        if (product.getSuitability() == 100){
            textQuestionable.setText(String.format("%s is perfect for you!", product.getName()));
        }
        else{
            String text = getResources().getString(R.string.questionable_ingredients, product.getName());
            textQuestionable.setText(text);
        }

        //if the view all button is clicked, launch the ingredients activity
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), IngredientsActivity.class);
                startActivity(i);
            }
        });

        //if the scan another button is clicked, finish the activity
        scanAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return v;
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
}
