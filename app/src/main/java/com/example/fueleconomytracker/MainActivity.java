package com.example.fueleconomytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/*
    This activity is responsible for adding new entry two the database.
 */
public class MainActivity extends AppCompatActivity {
    EditText odometerReading;
    EditText fuelInLiters;
    EditText TotalCost;
    Button submit;
    SQLiteDatabase dataBase; //Database object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //Initialize UI components.
        odometerReading = (EditText) findViewById(R.id.odometerReading);
        fuelInLiters = (EditText) findViewById(R.id.FuelInLiters);
        TotalCost = (EditText) findViewById(R.id.TotalCost);
        submit = (Button) findViewById(R.id.Submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if user has entered all the required data.
                if(odometerReading.getText().toString().equals("")  || fuelInLiters.getText().toString().equals("") || TotalCost.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Please fill in all the details.", Toast.LENGTH_SHORT).show();
                    return;
                }
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

                    if(odometerValue == 0 || fuelLiters == 0 || totalCost == 0)
                    {
                        Toast.makeText(MainActivity.this, "Please enter valid data", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //Insert valid data to the database.
                    dataBase.execSQL(
                            "INSERT INTO FUEL_DATA(ODOMETER, FUEL_FILLED, FUEL_COST) VALUES ("+odometerValue+","+fuelLiters+","+totalCost+");"
                    );
                    Toast.makeText(MainActivity.this, "Insertion Successful!", Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Error: "+ ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                //Clear all the fields after Inserting into database.
                odometerReading.setText("");
                TotalCost.setText("");
                fuelInLiters.setText("");
            }
        });

    }
}