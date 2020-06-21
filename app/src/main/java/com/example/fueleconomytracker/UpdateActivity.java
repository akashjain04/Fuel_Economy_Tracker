package com.example.fueleconomytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/*
    This activity is used to update the previous value in the database.
    Please Note :- We can only update the last entered value as results depends on last updated value.
 */
public class UpdateActivity extends AppCompatActivity {
    EditText odometerReading;
    EditText fuelInLiters;
    EditText TotalCost;
    Button update;
    SQLiteDatabase dataBase; // Database object
    int id; // ID for updating
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //Initialize UI components
        odometerReading = (EditText) findViewById(R.id.odometerReading2);
        fuelInLiters = (EditText) findViewById(R.id.fuelInLiters);
        TotalCost = (EditText) findViewById(R.id.totalCost);
        update = (Button) findViewById(R.id.update);
        //Display the last entered values in the textboxes to edit.
        displayLastRowData();

        //On click of Update button update the entry in the database.
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag) {
                    // If no entries present in the database.
                    Toast.makeText(UpdateActivity.this, "Looks like you have not entered any values yet :(", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get the values from the textbox.
                long odometerValue = Long.parseLong(odometerReading.getText().toString());
                double fuelLiters = Double.parseDouble(fuelInLiters.getText().toString());
                double totalCost = Double.parseDouble(TotalCost.getText().toString());

                try{
                    //Update the entry.
                    dataBase = openOrCreateDatabase("dataBase",MODE_PRIVATE,null);
                    dataBase.execSQL(
                            "UPDATE FUEL_DATA SET ODOMETER = "+odometerValue+"," +
                                                " FUEL_FILLED = " + fuelLiters + "," +
                                                "FUEL_COST =" + totalCost +" WHERE ID = " + id + ";"
                    );
                    Toast.makeText(UpdateActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                }catch (Exception ex){
                    Toast.makeText(UpdateActivity.this, "Something is wrong!!", Toast.LENGTH_SHORT).show();
                }

                // Clear the textbox after the updating.
                odometerReading.setText("");
                TotalCost.setText("");
                fuelInLiters.setText("");
            }
        });
    }
    /*
        A method to display the last entered values from the database to the user.
     */
    private void displayLastRowData() {
        try{
            dataBase = openOrCreateDatabase("dataBase",MODE_PRIVATE,null);

            Cursor cs = dataBase.rawQuery("SELECT * FROM FUEL_DATA ORDER BY ID DESC", null);
            cs.moveToFirst();

            //If the database is empty.
            if(cs.getCount() == 0){
                Toast.makeText(this, "Please insert the values through add Record.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Get the last entered details.
            long currentOdoValue = Long.parseLong(cs.getString(cs.getColumnIndex("ODOMETER")));
            double fuelFilled = Double.parseDouble(cs.getString(cs.getColumnIndex("FUEL_FILLED")));
            double fuelCost = Double.parseDouble(cs.getString(cs.getColumnIndex("FUEL_COST")));
            //Store this ID to the future reference to update.
            id = Integer.parseInt(cs.getString(cs.getColumnIndex("ID")));

            //Set the values.
            odometerReading.setText(String.valueOf(currentOdoValue));
            fuelInLiters.setText(String.valueOf(fuelFilled));
            TotalCost.setText(String.valueOf(fuelCost));
        }
        catch (Exception ex){
            //If DB is empty
            Toast.makeText(this, "Looks like you have not entered any values yet :(", Toast.LENGTH_SHORT).show();
            flag = true;
        }

        return;
    }
}