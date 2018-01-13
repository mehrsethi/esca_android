package itp341.sethi.mehr.finalapp.Controller.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import itp341.sethi.mehr.finalapp.Controller.Fragments.ResultsFragment;
import itp341.sethi.mehr.finalapp.R;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        //launch the results fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = new ResultsFragment();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container_frame, f);
        ft.commit();
    }
}
