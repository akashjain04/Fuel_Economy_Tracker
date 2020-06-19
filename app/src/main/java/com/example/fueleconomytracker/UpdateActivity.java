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

public class UpdateActivity extends AppCompatActivity {
    EditText odometerReading;
    EditText fuelInLiters;
    EditText TotalCost;
    Button update;
    SQLiteDatabase dataBase;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        odometerReading = (EditText) findViewById(R.id.odometerReading2);
        fuelInLiters = (EditText) findViewById(R.id.fuelInLiters);
        TotalCost = (EditText) findViewById(R.id.totalCost);
        update = (Button) findViewById(R.id.update);

        displayLastRowData();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long odometerValue = Long.parseLong(odometerReading.getText().toString());
                double fuelLiters = Double.parseDouble(fuelInLiters.getText().toString());
                double totalCost = Double.parseDouble(TotalCost.getText().toString());

                try{
                    dataBase = openOrCreateDatabase("dataBase",MODE_PRIVATE,null);
                    dataBase.execSQL(
                            "UPDATE FUEL_DATA SET ODOMETER = "+odometerValue+"," +
                                                " FUEL_FILLED = " + fuelLiters + "," +
                                                "FUEL_COST =" + totalCost +" WHERE ID = " + id + ";"
                    );
                    Toast.makeText(UpdateActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                }catch (Exception ex){
                    Toast.makeText(UpdateActivity.this, "Error : "+ ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                odometerReading.setText("");
                TotalCost.setText("");
                fuelInLiters.setText("");
            }
        });
    }

    private void displayLastRowData() {
        try{
            dataBase = openOrCreateDatabase("dataBase",MODE_PRIVATE,null);

            Cursor cs = dataBase.rawQuery("SELECT * FROM FUEL_DATA ORDER BY ID DESC", null);
            cs.moveToFirst();
            if(cs.getCount() == 0){
                Toast.makeText(this, "Please insert the values through add Record.", Toast.LENGTH_SHORT).show();
                return;
            }
            long currentOdoValue = Long.parseLong(cs.getString(cs.getColumnIndex("ODOMETER")));
            double fuelFilled = Double.parseDouble(cs.getString(cs.getColumnIndex("FUEL_FILLED")));
            double fuelCost = Double.parseDouble(cs.getString(cs.getColumnIndex("FUEL_COST")));
            id = Integer.parseInt(cs.getString(cs.getColumnIndex("ID")));

            odometerReading.setText(String.valueOf(currentOdoValue));
            fuelInLiters.setText(String.valueOf(fuelFilled));
            TotalCost.setText(String.valueOf(fuelCost));
        }
        catch (Exception ex){
            Toast.makeText(this, "Error :" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return;
    }
}