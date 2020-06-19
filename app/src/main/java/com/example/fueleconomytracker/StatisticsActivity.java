package com.example.fueleconomytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class StatisticsActivity extends AppCompatActivity {
    SQLiteDatabase dataBase;

    TextView cost;
    ProgressBar progressBar;
    TextView economyBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        cost = (TextView) findViewById(R.id.costKm);
        economyBar = (TextView) findViewById(R.id.economyBar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        computeEffeciency();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        computeEffeciency();
    }

    @Override
    protected void onResume() {
        super.onResume();
        computeEffeciency();
    }

    private void computeEffeciency() {
        try{
            //Create a database if it not exists or open an existing database.
            dataBase = openOrCreateDatabase("dataBase",MODE_PRIVATE,null);

            Cursor cs = dataBase.rawQuery("SELECT * FROM FUEL_DATA ORDER BY ID DESC", null);
            cs.moveToFirst();
            if(cs.getCount() <= 1){
                Toast.makeText(this, "Atleast 2 entries are required. Try after inserting next value.", Toast.LENGTH_SHORT).show();
                return;
            }
            long currentOdoValue = Long.parseLong(cs.getString(cs.getColumnIndex("ODOMETER")));
            cs.moveToNext();
            long previousOdoValue = Long.parseLong(cs.getString(cs.getColumnIndex("ODOMETER")));
            double fuelFilled = Double.parseDouble(cs.getString(cs.getColumnIndex("FUEL_FILLED")));
            double fuelCost = Double.parseDouble(cs.getString(cs.getColumnIndex("FUEL_COST")));

            long odoDiff = currentOdoValue - previousOdoValue;

            double economy = odoDiff / fuelFilled;

            double costPerKM = fuelCost / odoDiff;
            economyBar.setText(Math.round(economy)+" kmpl");
            cost.setText(Math.round(costPerKM) +" Rs/km");



        }catch (Exception ex){
            Toast.makeText(this, "Looks like you have not entered any values yet :(", Toast.LENGTH_SHORT).show();
        }
    }
}