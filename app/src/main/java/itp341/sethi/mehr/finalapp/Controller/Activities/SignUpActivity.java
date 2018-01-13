package itp341.sethi.mehr.finalapp.Controller.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import itp341.sethi.mehr.finalapp.Controller.Fragments.MainFragment;
import itp341.sethi.mehr.finalapp.Controller.Fragments.SignUpFragment;
import itp341.sethi.mehr.finalapp.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        //launch the sign up fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = new SignUpFragment();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container_frame, f);
        ft.commit();
    }

}

