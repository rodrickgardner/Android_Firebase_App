package com.midterm.lab5_firebase_car;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.lab5_firebase_car.model.Car;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener, ChildEventListener {

    Button btnAdd, btnUpdate, btnRemove, btnFind, btnList;
    EditText edId, edPrice;
    RadioGroup rgBrand;
    RadioButton rdToyota, rdMazda, rdHyundai;
    CheckBox chkStatus;

    ArrayList<Car> listOfCars;

    DatabaseReference carDatabase, carChild;

    String brand, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnRemove = findViewById(R.id.btnDelete);
        btnFind = findViewById(R.id.btnFind);
        btnList = findViewById(R.id.btnList);

        edId = findViewById(R.id.edId);
        edPrice = findViewById(R.id.edPrice);
        rgBrand = findViewById(R.id.rgBrands);
        rdToyota = findViewById(R.id.rdToyota);
        rdMazda = findViewById(R.id.rdMazda);
        rdHyundai = findViewById(R.id.rdHyundai);
        chkStatus = findViewById(R.id.chkStatus);

        btnAdd.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnRemove.setOnClickListener(this);
        btnFind.setOnClickListener(this);
        btnList.setOnClickListener(this);

        rdHyundai.setOnClickListener(this);
        rdToyota.setOnClickListener(this);
        rdMazda.setOnClickListener(this);
        chkStatus.setOnClickListener(this);

        listOfCars = new ArrayList<Car>();

        carDatabase = FirebaseDatabase.getInstance().getReference("car");

        //carDatabase.addChildEventListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.chkStatus:
                if (chkStatus.isChecked())
                    status = "New";
                else
                    status = "Used";
                break;
            case R.id.rdToyota:
                brand = "Toyota";
                break;
            case R.id.rdMazda:
                brand = "Mazda";
                break;
            case R.id.rdHyundai:
                brand = "Hyundai";
                break;
            case R.id.btnAdd:
                addCar();
                break;
            case R.id.btnUpdate:
                updateCar();
                break;
            case R.id.btnDelete:
                deleteCar();
                break;
            case R.id.btnFind:
                findCar();
                break;
            case R.id.btnList:
                listCar();
                break;
        }
    }

    private void listCar() {
        carDatabase.addChildEventListener(this);
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("cars", listOfCars);
        startActivity(intent);
        listOfCars.clear();
    }

    private void findCar() {
        String id = edId.getText().toString();
        carChild = carDatabase.child(id);
        carChild.addValueEventListener(this);
    }

    private void deleteCar() {
        String id = edId.getText().toString();
        carChild = carDatabase.child(id);
        carChild.removeValue();
        Toast.makeText(this, "The document with the key: " + id
                + " has being deleted successfully.", Toast.LENGTH_LONG).show();
        clearDocument();
    }

    private void updateCar() {
        String id = edId.getText().toString();
        String price = edPrice.getText().toString();

        try {
            if (brand == "" && id == "" && price == ""){
                Toast.makeText(this, "Please fulfill all the require field of information.", Toast.LENGTH_LONG).show();
            } else {
                Car car = new Car(Integer.valueOf(id), brand, status, Integer.valueOf(price));
                carChild = carDatabase.child(id);
                carChild.setValue(car);
                clearDocument();
                Toast.makeText(this, "The document with the key " + id + " is updated.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void addCar() {
        String id = edId.getText().toString();
        String price = edPrice.getText().toString();

        try {
            if (brand == "" && id == "" && price == ""){
                Toast.makeText(this, "Please fulfill all the require field of information.", Toast.LENGTH_LONG).show();
            } else {
                Car car = new Car(Integer.valueOf(id), brand, status, Integer.valueOf(price));
                carChild = carDatabase.child(id);
                carChild.setValue(car);
                clearDocument();
                Toast.makeText(this, "The document with the key " + id + " is added.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void clearDocument() {
        edId.setText(null);
        edPrice.setText(null);
        rgBrand.clearCheck();
        chkStatus.setChecked(false);
        status = "Used";
        edId.requestFocus();
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Car car = snapshot.getValue(Car.class);
        listOfCars.add(car);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()) {
            String brand = snapshot.child("brand").getValue().toString();
            String status = snapshot.child("status").getValue().toString();
            String price = snapshot.child("price").getValue().toString();

            edPrice.setText(price);
            if (status.equals("New")) {
                chkStatus.setChecked(true);
            } else if (status.equals("Used")) {
                chkStatus.setChecked(false);
            }
            if (brand.equals("Toyota")) {
                rdToyota.setChecked(true);
            } else if (brand.equals("Mazda")) {
                rdMazda.setChecked(true);
            } else if (brand.equals("Hyundai")) {
                rdHyundai.setChecked(true);
            }
        } else {
            Toast.makeText(this, "Document with the key" + edId.getText().toString() + " doesn't exists", Toast.LENGTH_LONG).show();
            clearDocument();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}