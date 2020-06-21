package com.example.fueleconomytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/*
    This activity is used to display the results to the user.
 */
public class StatisticsActivity extends AppCompatActivity {
    SQLiteDatabase dataBase; // Database object

    TextView cost;
    ProgressBar progressBar;
    TextView economyBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        //Hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //Initialize UI components
        cost = (TextView) findViewById(R.id.costKm);
        economyBar = (TextView) findViewById(R.id.economyBar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Call method to calculate economy and display
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
            //Get the current odoMeter value.
            long currentOdoValue = Long.parseLong(cs.getString(cs.getColumnIndex("ODOMETER")));
            cs.moveToNext();
            // Get the previous odometer value.
            long previousOdoValue = Long.parseLong(cs.getString(cs.getColumnIndex("ODOMETER")));
            double fuelFilled = Double.parseDouble(cs.getString(cs.getColumnIndex("FUEL_FILLED")));
            double fuelCost = Double.parseDouble(cs.getString(cs.getColumnIndex("FUEL_COST")));

            // Calculate the difference in the odometer to find the distance travelled.
            long odoDiff = currentOdoValue - previousOdoValue;

            // Calculate the economy value.
            double economy = odoDiff / fuelFilled;
            // Rounding off the value to the 2 decimal places.
            economy = Math.round(economy * 100.00) / 100.00;

            //Calculate the fuel cost per km
            double costPerKM = fuelCost / odoDiff;
            // Rounding off the value to the 2 decimal places.
            costPerKM = Math.round(costPerKM * 100.00) / 100.00;

            //Display the results.
            economyBar.setText(economy +" kmpl");
            cost.setText(costPerKM +" Rs/km");

        }catch (Exception ex){
            Toast.makeText(this, "Looks like you have not entered any values yet :(", Toast.LENGTH_SHORT).show();
        }
    }
}