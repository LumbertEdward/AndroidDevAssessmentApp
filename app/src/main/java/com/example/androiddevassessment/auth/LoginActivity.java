package com.example.androiddevassessment.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androiddevassessment.MainActivity;
import com.example.androiddevassessment.R;
import com.example.androiddevassessment.constants.constants;
import com.example.androiddevassessment.models.User;
import com.example.androiddevassessment.utils.AppUtils;
import com.example.androiddevassessment.utils.RealmUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email;
    private EditText password;
    private Button btn;
    private Button fire;
    private ProgressBar progressBar;
    private TextView firebaseSign;
    private TextView submit;

    //firebase
    private FirebaseAuth firebaseAuth;

    //volley
    private RequestQueue requestQueue;
    private RequestQueue requestQueue1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initLayouts();
        firebaseAuth = FirebaseAuth.getInstance();
        requestQueue = Volley.newRequestQueue(this);
        requestQueue1 = Volley.newRequestQueue(this);
        btn.setOnClickListener(this);
        fire.setOnClickListener(this);
        firebaseSign.setOnClickListener(this);
    }

    private void initLayouts() {
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        btn = findViewById(R.id.btnSubmit);
        fire = findViewById(R.id.btnFireSubmit);
        progressBar = findViewById(R.id.progressLogin);
        firebaseSign = findViewById(R.id.firebaseAcc);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnFireSubmit:
                firebaseLogin();
                break;
            case R.id.btnSubmit:
                apiLogin();
                break;
            case R.id.firebaseAcc:
                showBottom();
                break;
        }
    }

    private void apiLogin() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(userEmail)){
            email.setError("Email Required");
            email.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(userPassword)){
            password.setError("Password Required");
            password.requestFocus();
            return;
        }
        else {
            if (AppUtils.checkNetwork(this)){
                progressBar.setVisibility(View.VISIBLE);
                btn.setEnabled(false);
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmail);
                params.put("password", userPassword);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, constants.BASE_URL + constants.LOGIN_URL, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null){
                            try {
                                String token = response.getString("token");
                                if (token != null){
                                    btn.setEnabled(true);
                                    progressBar.setVisibility(View.GONE);
                                    getUserDetails();
                                }
                                else {
                                    btn.setEnabled(true);
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        else {
                            btn.setEnabled(true);
                            Toast.makeText(LoginActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        btn.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                    }
                });

                requestQueue1.add(jsonObjectRequest);

            }
            else {
                Toast.makeText(this, "Check Network Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getUserDetails() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, constants.BASE_URL + constants.USER_2_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null){
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        int id = jsonObject.getInt("id");
                        String firstname = jsonObject.getString("first_name");
                        String lastname = jsonObject.getString("last_name");
                        String avatar = jsonObject.getString("avatar");
                        String email = jsonObject.getString("email");
                        RealmUtils.setUser(id, firstname, lastname, email, avatar);

                        RealmUtils.setLogOutUser(true);
                        if (RealmUtils.checkStatus()){
                            progressBar.setVisibility(View.GONE);
                            fire.setEnabled(true);
                            btn.setEnabled(true);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    fire.setEnabled(true);
                    btn.setEnabled(true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fire.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                btn.setEnabled(true);
                Toast.makeText(LoginActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void firebaseLogin() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(userEmail)){
            email.setError("Email Required");
            email.requestFocus();
            return;
        }
        else if (TextUtils.isEmpty(userPassword)){
            password.setError("Password Required");
            password.requestFocus();
            return;
        }
        else {
            if (AppUtils.checkNetwork(this)){
                progressBar.setVisibility(View.VISIBLE);
                fire.setEnabled(false);
                firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            getUserDetails();
                            progressBar.setVisibility(View.GONE);
                            fire.setEnabled(true);
                        }
                        else {
                            fire.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else {
                Toast.makeText(this, "Check Network Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showBottom(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(LoginActivity.this, R.style.SheetDialog);
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.signup_sheet);

        TextInputEditText emailT = bottomSheetDialog.findViewById(R.id.email);
        TextInputEditText passwordT = bottomSheetDialog.findViewById(R.id.password);
        submit = bottomSheetDialog.findViewById(R.id.txtSign);

        bottomSheetDialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em = emailT.getText().toString().trim();
                String pass = passwordT.getText().toString().trim();

                if (TextUtils.isEmpty(em)){
                    emailT.setError("Email Required");
                    emailT.requestFocus();
                }
                else if (TextUtils.isEmpty(pass)){
                    passwordT.setError("Password Required");
                    passwordT.requestFocus();
                }
                else {
                    if (AppUtils.checkNetwork(LoginActivity.this)){
                        submit.setEnabled(false);
                        sendDetails(em, pass);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Check Network Connection", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    private void sendDetails(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    submit.setEnabled(true);
                    getUserDetails();
                }
                else {
                    submit.setEnabled(true);
                    Toast.makeText(LoginActivity.this, "Not Registered", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (RealmUtils.checkStatus()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        else {

        }
    }
}