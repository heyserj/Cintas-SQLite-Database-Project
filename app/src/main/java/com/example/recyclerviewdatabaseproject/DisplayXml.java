package com.example.recyclerviewdatabaseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.databaselibrary.DBmain;

public class DisplayXml extends AppCompatActivity {

    DBmain dBmain;
    Button return_btn;
    TextView xml_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_xml);

        dBmain = new DBmain(this);

        findId();
        displayData();
        initializeButton();
    }

    private void findId(){
        return_btn = (Button) findViewById(R.id.xml_display_return_btn);
        xml_tv = (TextView) findViewById(R.id.tv_display_xml);
    }

    private void displayData(){
        Cursor cursor = dBmain.getAllData();
        StringBuilder builder = new StringBuilder("CPRNT;");
        if (cursor.getCount() > 0) { //if there's at least one entry in the database

            while (cursor.moveToNext()) {
                int param1 = cursor.getInt(0); //getting the first entry that the Cursor is pointing to
                String eventTime = cursor.getString(1); //getting the second entry that the Cursor is pointing to
                String hostId = cursor.getString(2); //getting the third entry that the Cursor is pointing to
                String appId = cursor.getString(3); //AppId
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
        xml_tv.setText(builder.toString());
    }

    private void initializeButton(){
        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayXml.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }
}