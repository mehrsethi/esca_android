package itp341.sethi.mehr.finalapp.Controller.Fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import itp341.sethi.mehr.finalapp.Controller.Activities.ResultsActivity;
import itp341.sethi.mehr.finalapp.Model.Ingredient;
import itp341.sethi.mehr.finalapp.Model.ProductSingleton;
import itp341.sethi.mehr.finalapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {
    Button buttonBack;
    ListView listAllIngredients;
    Toolbar toolbar;

    ArrayAdapter adapter;

    ProductSingleton productSingleton;

    public IngredientsFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ingredients, container, false);

        // Toolbar
        toolbar = (Toolbar) v.findViewById(R.id.toolbar_ingredients);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        //references to views
        buttonBack = (Button) v.findViewById(R.id.button_back);
        listAllIngredients = (ListView) v.findViewById(R.id.list_all_ingredients);

        //get the singleton
        productSingleton = ProductSingleton.getSingleton(getContext());

        //adjustment for using the adpater
        for (int i=0; i<productSingleton.getTranslatedArray().size(); i++){
            productSingleton.getIngredientsArray().get(i).setTranslate(productSingleton.getTranslatedArray().get(i));
        }

        //set the adapter for the ingredients list view
        adapter = new IngredientsAdapter(getActivity(), R.layout.list_ingredient_item, productSingleton.getIngredientsArray());
        listAllIngredients.setAdapter(adapter);

        //if the back button is clicked, finish the activity
        buttonBack.setOnClickListener(new View.OnClickListener() {
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

    //ingredients adapter
    //translated and original ingredients in different colors
    private class IngredientsAdapter extends ArrayAdapter<Ingredient> {

        @Override
        public long getItemId(int position) {
            return position;
        }

        public IngredientsAdapter(@NonNull Context context, int resId, List<Ingredient> ingredientsList) {
            super(context, resId, ingredientsList);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_ingredient_item, null);
            }

            TextView textOriginal = (TextView) convertView.findViewById(R.id.text_original);
            TextView textTranslate = (TextView) convertView.findViewById(R.id.text_translate);

            Ingredient ingredient = productSingleton.getIngredientsArray().get(position);

            textOriginal.setText(ingredient.getOriginal());
            textTranslate.setText(ingredient.getTranslate());

            return convertView;
        }
    }

}