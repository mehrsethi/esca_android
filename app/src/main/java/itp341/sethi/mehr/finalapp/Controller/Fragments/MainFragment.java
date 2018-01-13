package itp341.sethi.mehr.finalapp.Controller.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import itp341.sethi.mehr.finalapp.Controller.Activities.ScanActivity;
import itp341.sethi.mehr.finalapp.Controller.Activities.SignUpActivity;
import itp341.sethi.mehr.finalapp.Model.UserSingleton;
import itp341.sethi.mehr.finalapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    ImageView imageProfile;
    Button buttonEditProfile;
    Button buttonStartScanning;

    UserSingleton user;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //if this is the first time the user is opening the app
        user = UserSingleton.getSingleton(getActivity());
        if (user.getName().equals("")){
            //launch the sign up activity
            Intent i = new Intent(getActivity(), SignUpActivity.class);
            startActivityForResult(i, 1);
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        //references to views
        imageProfile = (ImageView) v.findViewById(R.id.image_profile);
        buttonEditProfile = (Button) v.findViewById(R.id.button_edit_profile);
        buttonStartScanning = (Button) v.findViewById(R.id.button_scan);

        //if the user hasn't set a profile picture, use default
        if (user.getProfileImage() == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(
                    getActivity().getApplicationContext().getResources(),
                    R.drawable.placeholder_profile);
            imageProfile.setImageBitmap(bitmap);
            user.setProfileImage(bitmap);
        }
        //otherwise use the locally stored picture
        else{
            imageProfile.setImageBitmap(user.getProfileImage());
        }

        //if the edit profile button is clicked, launch the sign up activity
        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent i = new Intent(getActivity(), SignUpActivity.class);
            startActivityForResult(i, 1);
            }
        });

        //if the start scanning button is clicked, launch the scan activity
        buttonStartScanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ScanActivity.class);
                startActivityForResult(i, 2);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the user doesn't have a photo, use default
        if (user.getProfileImage() == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(
                    getActivity().getApplicationContext().getResources(),
                    R.drawable.placeholder_profile);
            imageProfile.setImageBitmap(bitmap);
            user.setProfileImage(bitmap);
        }
        //otherwise use the locally stored picture
        else{
            imageProfile.setImageBitmap(user.getProfileImage());
        }
    }
}
