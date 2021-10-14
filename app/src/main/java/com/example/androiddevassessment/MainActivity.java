package com.example.androiddevassessment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.androiddevassessment.auth.LoginActivity;
import com.example.androiddevassessment.interfaces.AllInterfaces;
import com.example.androiddevassessment.utils.RealmUtils;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class MainActivity extends AppCompatActivity implements AllInterfaces {
    private NavHostFragment navHostFragment;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHost);
        navController = navHostFragment.getNavController();
        initFrag();
    }

    private void initFrag() {
        navController.navigate(R.id.dashboardFragment);
    }

    @Override
    public void goToAvailable() {
        navController.navigate(R.id.availableFragment);

    }

    @Override
    public void goToMain() {
        navController.navigate(R.id.dashboardFragment);
    }

    @Override
    public void goToTopDeals() {
        navController.navigate(R.id.topDeals);
    }

    @Override
    public void goToGarage() {
        navController.navigate(R.id.garage);

    }

    @Override
    public void logOut() {
        RealmUtils.setLogOutUser(false);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}