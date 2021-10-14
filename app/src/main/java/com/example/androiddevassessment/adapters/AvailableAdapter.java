package com.example.androiddevassessment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androiddevassessment.R;
import com.example.androiddevassessment.models.Car;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AvailableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Car> cars = new ArrayList<>();
    private Context context;

    public AvailableAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.available_cars_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.title.setText(cars.get(position).getTitle());
        myViewHolder.duration.setText(cars.get(position).getDuration());
        myViewHolder.payment.setText(cars.get(position).getPayment());
        myViewHolder.price.setText("AED " + cars.get(position).getPrice());
        Picasso.Builder picasso = new Picasso.Builder(context);
        picasso.downloader(new OkHttp3Downloader(context));
        picasso.build().load(cars.get(position).getImgurl()).into(myViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView price;
        private TextView payment;
        private TextView duration;
        private ImageView imageView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.availableTitle);
            price = itemView.findViewById(R.id.availablePrice);
            payment = itemView.findViewById(R.id.availablePayment);
            duration = itemView.findViewById(R.id.durationA);
            imageView = itemView.findViewById(R.id.imgA);
        }
    }

    public void getCars(ArrayList<Car> carArrayList){
        for (Car car: carArrayList){
            cars.add(car);
            notifyDataSetChanged();
        }
    }
}
