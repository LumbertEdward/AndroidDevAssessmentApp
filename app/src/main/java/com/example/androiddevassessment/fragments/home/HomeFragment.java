package com.example.androiddevassessment.fragments.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.androiddevassessment.R;
import com.example.androiddevassessment.adapters.HomeAdapter;
import com.example.androiddevassessment.interfaces.AllInterfaces;
import com.example.androiddevassessment.models.Car;
import com.example.androiddevassessment.utils.RealmUtils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private CardView available;
    private RecyclerView topDeals;
    private ImageView imageView;
    private LinearLayout garage;
    private LinearLayout top;
    private CircularProgressIndicator circularProgressIndicator;
    private LinearLayoutManager linearLayoutManager;
    private HomeAdapter homeAdapter;
    private AllInterfaces allInterfaces;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ArrayList<Car> cars = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        available = view.findViewById(R.id.cardMoveToAvailable);
        imageView = view.findViewById(R.id.imgProf);
        garage = view.findViewById(R.id.linGarage);
        shimmerFrameLayout = view.findViewById(R.id.shimmerHome);
        top = view.findViewById(R.id.linTop);
        circularProgressIndicator = view.findViewById(R.id.circular_progress);
        circularProgressIndicator.setProgress(90, 100);
        available.setOnClickListener(this::onClick);
        homeAdapter = new HomeAdapter(getContext());
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        topDeals = view.findViewById(R.id.recyclerTopDeals);

        firebaseDatabase = FirebaseDatabase.getInstance();

        getDeals();
        setProfile();

        garage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allInterfaces.goToGarage();
            }
        });

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allInterfaces.goToTopDeals();
            }
        });

        shimmerFrameLayout.startShimmer();
        topDeals.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        return view;
    }

    private void setProfile() {
        if (RealmUtils.checkStatus()){
            Picasso.Builder picasso = new Picasso.Builder(getContext());
            picasso.downloader(new OkHttp3Downloader(getContext()));
            picasso.build().load(RealmUtils.getUser().getAvatar()).into(imageView);
        }
    }

    private void getDeals() {
        databaseReference = firebaseDatabase.getReference().child("cars").child("all");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Car car = new Car();
                        car.setTitle(dataSnapshot.child("name").getValue().toString());
                        car.setDuration(dataSnapshot.child("duration").getValue().toString());
                        car.setGearbox(dataSnapshot.child("gearbox").getValue().toString());
                        car.setImgurl(dataSnapshot.child("imgurl").getValue().toString());
                        car.setPrice(dataSnapshot.child("price").getValue().toString());
                        car.setPayment(dataSnapshot.child("payment").getValue().toString());
                        car.setSeat(dataSnapshot.child("seat").getValue().toString());

                        cars.add(car);
                    }

                    shimmerFrameLayout.setVisibility(View.GONE);
                    topDeals.setVisibility(View.VISIBLE);
                    homeAdapter.setCars(cars);
                    topDeals.setAdapter(homeAdapter);
                    topDeals.setLayoutManager(linearLayoutManager);
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        allInterfaces = (AllInterfaces) context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cardMoveToAvailable:
                allInterfaces.goToAvailable();
                break;
        }
    }
}