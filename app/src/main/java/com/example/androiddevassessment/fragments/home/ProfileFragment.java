package com.example.androiddevassessment.fragments.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.androiddevassessment.R;
import com.example.androiddevassessment.interfaces.AllInterfaces;
import com.example.androiddevassessment.utils.RealmUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private ImageView profilePic;
    private TextView name;
    private ImageView email;
    private ImageView firstName;
    private ImageView lastName;
    private ImageView password;
    private ImageView support;
    private RelativeLayout logOut;
    private CircularProgressIndicator circularProgressIndicator;

    private String imgUrl;
    private String userFirstname;
    private String userLastname;
    private String userEmail;

    private BottomSheetDialog bottomSheetDialog;

    private AllInterfaces allInterfaces;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profilePic = view.findViewById(R.id.imgProfile);
        name = view.findViewById(R.id.nameProfile);
        email = view.findViewById(R.id.imgEmail);
        firstName = view.findViewById(R.id.imgFirstName);
        lastName = view.findViewById(R.id.imgLastName);
        password = view.findViewById(R.id.imgPassword);
        support = view.findViewById(R.id.imgSupport);
        logOut = view.findViewById(R.id.relSignOut);
        circularProgressIndicator = view.findViewById(R.id.circular_progress_profile);
        circularProgressIndicator.setProgress(90, 100);

        email.setOnClickListener(this);
        firstName.setOnClickListener(this);
        lastName.setOnClickListener(this);
        password.setOnClickListener(this);
        support.setOnClickListener(this);
        logOut.setOnClickListener(this);

        setUser();
        return view;
    }

    private void setUser() {
        if (RealmUtils.checkStatus()){
            imgUrl = RealmUtils.getUser().getAvatar();
            userFirstname = RealmUtils.getUser().getFirstname();
            userLastname = RealmUtils.getUser().getLastname();
            userEmail = RealmUtils.getUser().getEmail();

            if (imgUrl != null){
                Picasso.Builder picasso = new Picasso.Builder(getContext());
                picasso.downloader(new OkHttp3Downloader(getContext()));
                picasso.build().load(imgUrl).into(profilePic);

                name.setText("Hi, " + userFirstname);
            }
        }
        else {
            allInterfaces.logOut();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgEmail:
                viewEmail();
                break;
            case R.id.imgFirstName:
                break;
            case R.id.imgLastName:
                break;
            case R.id.imgPassword:
                break;
            case R.id.imgSupport:
                break;
            case R.id.relSignOut:
                allInterfaces.logOut();
                break;
        }

    }

    private void viewEmail() {
        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.SheetDialog);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.email_sheet);

        TextView emailUpdate = bottomSheetDialog.findViewById(R.id.curEmail);
        TextInputEditText newEmail = bottomSheetDialog.findViewById(R.id.emailUpdate);
        TextView update = bottomSheetDialog.findViewById(R.id.txtUpdate);

        bottomSheetDialog.show();

        emailUpdate.setText(userEmail);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        allInterfaces = (AllInterfaces) context;
    }
}