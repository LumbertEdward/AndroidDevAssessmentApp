package com.example.androiddevassessment.fragments.available;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.androiddevassessment.R;
import com.example.androiddevassessment.adapters.AvailableAdapter;
import com.example.androiddevassessment.interfaces.AllInterfaces;
import com.example.androiddevassessment.models.Car;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class BestMatchFragment extends Fragment {
    private RecyclerView recyclerView;
    private AvailableAdapter availableAdapter;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<Car> cars = new ArrayList<>();
    private RelativeLayout back;
    private AllInterfaces allInterfaces;

    //firebase
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
        View view = inflater.inflate(R.layout.fragment_best_match, container, false);
        recyclerView = view.findViewById(R.id.recyclerAvailable);
        back = view.findViewById(R.id.relBack);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        availableAdapter = new AvailableAdapter(getContext());

        firebaseDatabase = FirebaseDatabase.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allInterfaces.goToMain();
            }
        });
        getData();
        return view;
    }

    private void getData() {

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

                    availableAdapter.getCars(cars);
                    recyclerView.setAdapter(availableAdapter);
                    recyclerView.setLayoutManager(gridLayoutManager);
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
}