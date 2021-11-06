package com.midterm.lab5_firebase_car;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.midterm.lab5_firebase_car.model.Car;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnReturn;
    TextView data;
    ArrayList<Car> listOfCars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initialize();
    }

    private void initialize() {
        btnReturn = findViewById(R.id.btnReturn);
        data = findViewById(R.id.tvList);

        listOfCars = (ArrayList<Car>) getIntent().getExtras().getSerializable("cars");

        StringBuilder list = new StringBuilder();
        for (Car oneCar : listOfCars) {
            list.append(oneCar + "\n");
        }

        data.setText(list);

        btnReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}