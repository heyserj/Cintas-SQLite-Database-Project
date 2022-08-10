package com.example.recyclerviewdatabaseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.databaselibrary.DBmain;


/**
 * Class that represents the activity in the app that displays all of the data fields for all of the
 * records in the database in XML format
 */
public class DisplayXml extends AppCompatActivity {

    DBmain dBmain; // an object corresponding to the library database that we can call the library's methods on
    Button returnBtn; // the button that the user clicks to return back to the main display activity
    TextView xmlTv; // the TextView that displays the information from the database in XML format

    /**
     * Driver method for the project that creates an object that has the library as its type
     * and calls the necessary methods that retrieve and display data in the database
     *
     * @param savedInstanceState a bundle of the saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_xml);

        dBmain = new DBmain(this);

        findId();
        displayData();
        initializeButton();
    }


    /**
     * Method that links the fields in this class to their id's in the XML file
     */
    private void findId() {
        returnBtn = (Button) findViewById(R.id.btn_xml_display_return);
        xmlTv = (TextView) findViewById(R.id.tv_display_xml);
    }


    /**
     * Method that sets the TextView's text in this activity to all of the information in the
     * database in XML format
     */
    private void displayData() {
        Cursor cursor = dBmain.getAllData(); // retrieving the information from the database
        StringBuilder builder = new StringBuilder("CPRNT;");
        if (cursor.getCount() > 0) { // if there's at least one entry in the database

            while (cursor.moveToNext()) {
                String eventTime = cursor.getString(1);
                String hostId = cursor.getString(2);
                String userId = cursor.getString(4);
                int locationNbr = cursor.getInt(5);
                int routeNbr = cursor.getInt(6);
                int day = cursor.getInt(7);
                String logger = cursor.getString(8);
                int eventNbr = cursor.getInt(9);
                String addtlDesc = cursor.getString(10);
                int addtlNbr = cursor.getInt(11);
                builder.append("<LogEntry EventTime=\"" + eventTime + "\" HostId=\"" + hostId
                        + "\" UserId=\"" + userId + "\" LocationNbr=\"" + locationNbr
                        + "\" RouteNbr=\"" + routeNbr + "\" Day=\"" + day + "\" Logger=\""
                        + logger + "\" EventNbr=\"" + eventNbr + "\" AddtlDesc=\"" + addtlDesc
                        + "\" AddtlNbr=\"" + addtlNbr + "\" />");
            }
        }
        xmlTv.setText(builder.toString()); // setting the text
    }


    /**
     * Method that initializes the return button so that, when clicked, it takes the user back
     * to the main activity displaying all of the information in a RecyclerView
     */
    private void initializeButton() {
        returnBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayXml.this, MainActivity2.class);
            startActivity(intent);
        });
    }
}
