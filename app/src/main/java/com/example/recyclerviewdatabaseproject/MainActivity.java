package com.example.recyclerviewdatabaseproject;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.databaselibrary.DBmain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{
    DBmain dBmain;
    EditText eventTime, hostId, userId, locationNbr, routeNbr;
    EditText day, logger, eventNbr, addtDesc, addtNbr;
    Button submit, display, edit;
    public static final String myPrefs = "MyPrefs";
    final Calendar myCalendar = Calendar.getInstance();
    NewService mService;
    boolean mBound = false;
    int id = 0;
    static boolean startedFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dBmain = new DBmain(this);

        //create object
        findid();
        setUpPickers();

        //disabling the edit button because the user hasn't chosen an entry
        //to edit yet
        edit.setEnabled(false);
        edit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

        insertData();
        cleardata();
        editdata();

        if (!startedFlag){
            insertExitRecord();
        }

        //startedFlag = true;
        //hostId.setText(UUID.randomUUID().toString();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (mBound){
            unbindService(connection);
            mBound = false;
            updateTimeStamps();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        //mBound = true;
        Intent intent = new Intent(this, NewService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        //startedFlag = true;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (! isChangingConfigurations()) {
            startedFlag = false;
            mService.insertExitRecord();
            updateTimeStamps();
        }
    }

    private ServiceConnection connection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName className, IBinder service){
            NewService.LocalBinder binder = (NewService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            if (!startedFlag){
                mService.insertStartupRecord();
                startedFlag = true;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            updateTimeStamps();
        }
    };

    private void editdata() {
        if (getIntent().getBundleExtra("record") != null) {
            Bundle bundle = getIntent().getBundleExtra("record");
            id = bundle.getInt("id");
            eventTime.setText(bundle.getString("eventTime"));
            hostId.setText(bundle.getString("hostId"));
            userId.setText(bundle.getString("userId"));
            locationNbr.setText(Integer.toString(bundle.getInt("locationNbr")));
            routeNbr.setText(Integer.toString(bundle.getInt("routeNbr")));
            day.setText(Integer.toString(bundle.getInt("day")));
            logger.setText(bundle.getString("logger"));
            eventNbr.setText(Integer.toString(bundle.getInt("eventNbr")));
            addtDesc.setText(bundle.getString("addtDesc"));
            addtNbr.setText(Integer.toString(bundle.getInt("addtNbr")));

            edit.getBackground().setColorFilter(null);
            edit.setEnabled(true);
            submit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            submit.setEnabled(false);
        }
    }

    private void cleardata() {
        eventTime.setText("");
        hostId.setText("");
        userId.setText("");
        locationNbr.setText("");
        routeNbr.setText("");
        day.setText("");
        logger.setText("");
        eventNbr.setText("");
        addtDesc.setText("");
        addtNbr.setText("");
    }

    private void insertData() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int param5, param6, param7, param9, param11;

                String param2 = eventTime.getText().toString().trim();
                String param3 = hostId.getText().toString().trim();
                String param4 = userId.getText().toString().trim();
                String temp = locationNbr.getText().toString();
                try {
                    param5 = Integer.parseInt(temp);
                } catch (NumberFormatException e) {
                    param5 = -1;
                }
                String temp2 = routeNbr.getText().toString();
                try {
                    param6 = Integer.parseInt(temp2);
                } catch (NumberFormatException e) {
                    param6 = -1;
                }
                String temp3 = day.getText().toString();
                try {
                    param7 = Integer.parseInt(temp3);
                } catch (NumberFormatException e) {
                    param7 = -1;
                }
                String param8 = logger.getText().toString().trim();
                String temp4 = eventNbr.getText().toString();
                try {
                    param9 = Integer.parseInt(temp4);
                } catch (NumberFormatException e) {
                    param9 = -1;
                }
                String param10 = addtDesc.getText().toString().trim();
                String temp5 = addtNbr.getText().toString();
                try {
                    param11 = Integer.parseInt(temp5);
                } catch (NumberFormatException e) {
                    param11 = -1;
                }

                boolean result = dBmain.insert(param2, param3, param4, param5, param6, param7, param8,
                        param9, param10, param11);
                if (result) {
                    Toast.makeText(MainActivity.this, "The record was successfully inserted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "There was an error inserting the record. Try again.", Toast.LENGTH_SHORT).show();
                }
                cleardata();
            }
        });
        display.setOnClickListener(new View.OnClickListener() { //want to display the entries in the database
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int param5, param6, param7, param9, param11;

                String param2 = eventTime.getText().toString().trim();
                String param3 = hostId.getText().toString().trim();
                String param4 = userId.getText().toString().trim();
                String temp = locationNbr.getText().toString();
                try {
                    param5 = Integer.parseInt(temp);
                } catch (NumberFormatException e) {
                    param5 = -1;
                }
                String temp2 = routeNbr.getText().toString();
                try {
                    param6 = Integer.parseInt(temp2);
                } catch (NumberFormatException e) {
                    param6 = -1;
                }
                String temp3 = day.getText().toString();
                try {
                    param7 = Integer.parseInt(temp3);
                } catch (NumberFormatException e) {
                    param7 = -1;
                }
                String param8 = logger.getText().toString().trim();
                String temp4 = eventNbr.getText().toString();
                try {
                    param9 = Integer.parseInt(temp4);
                } catch (NumberFormatException e) {
                    param9 = -1;
                }
                String param10 = addtDesc.getText().toString().trim();
                String temp5 = addtNbr.getText().toString();
                try {
                    param11 = Integer.parseInt(temp5);
                } catch (NumberFormatException e) {
                    param11 = -1;
                }

                boolean result = dBmain.updateRecord(id, param2, param3, param4, param5, param6, param7, param8,
                        param9, param10, param11);
                if (result) {
                    Toast.makeText(MainActivity.this, "The update was successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, "There was an error updating. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUpPickers() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        myCalendar.set(Calendar.MINUTE, minute);
                        updateLabel();
                    }
                }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show();
            }
        };
        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String format = "M/d/yy h:mm:00 a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        eventTime.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void findid() {
        eventTime = (EditText) findViewById(R.id.EventTime);
        hostId = (EditText) findViewById(R.id.HostId);
        userId = (EditText) findViewById(R.id.UserId);
        locationNbr = (EditText) findViewById(R.id.LocationNbr);
        routeNbr = (EditText) findViewById(R.id.RouteNbr);
        day = (EditText) findViewById(R.id.Day);
        logger = (EditText) findViewById(R.id.Logger);
        eventNbr = (EditText) findViewById(R.id.EventNbr);
        addtDesc = (EditText) findViewById(R.id.AddtDesc);
        addtNbr = (EditText) findViewById(R.id.AddtNbr);

        submit = (Button) findViewById(R.id.submit_btn);
        display = (Button) findViewById(R.id.display_btn);
        edit = (Button) findViewById(R.id.edit_btn);
    }

    private void updateTimeStamps(){
        SharedPreferences sharedPref = getSharedPreferences(myPrefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String format = "M/d/yy h:mm:ss a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        String currentTime = dateFormat.format(myCalendar.getTime());
        editor.putString("currentTime", currentTime);
        editor.commit();
    }

    private void insertExitRecord(){
        SharedPreferences sharedPref = getSharedPreferences(myPrefs, MODE_PRIVATE);
        if (sharedPref.getString("currentTime", null) != null){
            dBmain.insert(sharedPref.getString("currentTime", null), "", "",
                    -1, -1, -1, "", 1, "",
                    -1);
        }
    }
}