package itp341.sethi.mehr.finalapp.Controller.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import itp341.sethi.mehr.finalapp.Controller.Fragments.MainFragment;
import itp341.sethi.mehr.finalapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        //launch the main fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = new MainFragment();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container_frame, f);
        ft.commit();
    }
}
