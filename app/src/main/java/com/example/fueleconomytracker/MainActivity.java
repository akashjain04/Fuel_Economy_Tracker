package com.example.fueleconomytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText odometerReading;
    EditText fuelInLiters;
    EditText TotalCost;
    Button submit;
    SQLiteDatabase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        odometerReading = (EditText) findViewById(R.id.odometerReading);
        fuelInLiters = (EditText) findViewById(R.id.FuelInLiters);
        TotalCost = (EditText) findViewById(R.id.TotalCost);
        submit = (Button) findViewById(R.id.Submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    //Create a database if it not exists or open an existing database.
                    dataBase = openOrCreateDatabase("dataBase",MODE_PRIVATE,null);
                    //Create a table if not exists.
                    dataBase.execSQL("CREATE TABLE IF NOT EXISTS FUEL_DATA(ID INTEGER PRIMARY KEY AUTOINCREMENT, ODOMETER INTEGER, FUEL_FILLED REAL, FUEL_COST REAL )");
                }catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Error: "+ ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                try{
                    long odometerValue = Long.parseLong(odometerReading.getText().toString());
                    double fuelLiters = Double.parseDouble(fuelInLiters.getText().toString());
                    double totalCost = Double.parseDouble(TotalCost.getText().toString());

                    dataBase.execSQL(
                            "INSERT INTO FUEL_DATA(ODOMETER, FUEL_FILLED, FUEL_COST) VALUES ("+odometerValue+","+fuelLiters+","+totalCost+");"
                    );
                    Toast.makeText(MainActivity.this, "Insertion Successful!", Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Error: "+ ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                odometerReading.setText("");
                TotalCost.setText("");
                fuelInLiters.setText("");
            }
        });

    }
}