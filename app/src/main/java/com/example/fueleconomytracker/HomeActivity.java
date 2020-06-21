package com.example.fueleconomytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
/*
    This activity will handle the operation of the Home window.
 */
public class HomeActivity extends AppCompatActivity {
    Button addRecord;
    Button viewResult;
    Button updateRecord;
    Button deleteRecords;

    SQLiteDatabase dataBase; //DataBase object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Hide the action bar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //Initialize the components
        addRecord = (Button) findViewById(R.id.RecordAdd);
        viewResult = (Button) findViewById(R.id.viewResults);
        updateRecord = (Button) findViewById(R.id.updateRecord);
        deleteRecords = (Button) findViewById(R.id.deleteRecords);

        //On click of add record button go to Main activity
        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        //On click of view results go to statistics activity to display results.
        viewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, StatisticsActivity.class);
                startActivity(i);
            }
        });

        //On click Update Record go to update activity
        updateRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, UpdateActivity.class);
                startActivity(i);
            }
        });

        //On click of delete records delete all the entries present in the database.
        deleteRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create Alert Dialog and confirm if user wants to delete all  the data.
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Delete");
                builder.setMessage("Are sure you want to delete all the records? These cannot be recovered again.");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAllData();
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog deleteOrNot = builder.create();

                 deleteOrNot.show();
            }
        });
    }
    /*
        A method to delete all data from the database.
     */
    private void deleteAllData() {
        try{
            //Create a database if it not exists or open an existing database.
            dataBase = openOrCreateDatabase("dataBase",MODE_PRIVATE,null);
            //Create a table if not exists.
            dataBase.execSQL("DELETE FROM FUEL_DATA");
            Toast.makeText(this, "Deleted all records.", Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Toast.makeText(HomeActivity.this, "No records found!!", Toast.LENGTH_SHORT).show();
        }
    }
}