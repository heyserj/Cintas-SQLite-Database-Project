package com.example.recyclerviewdatabaseproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.databaselibrary.DBmain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * @author Jake Heyser for Cintas Corporation
 * @version 1
 * @since 8/9/2022
 * <p>
 * Class that represents the homscreeen for the "RecyclerView Database Project." The project as a
 * whole is responsible for implementing the user interface that may be used as part of the
 * Rental Copilot rewrite. It calls on the SQLite database library's methods that were implemented
 * in a separate library class that is imported in this project.
 */
public class MainActivity extends AppCompatActivity {
    DBmain dBmain; // an object corresponding to the library database that we can call the library's methods on

    // the EditText fields on the homescreen
    EditText eventTime, hostId, userId, locationNbr, routeNbr;
    EditText day, logger, eventNbr, addtDesc, addtNbr;

    Button submit, display, edit; // the various buttons shown on the homescreen
    public static final String myPrefs = "MyPrefs"; // the name of the SharedPreferences that we will use
    final Calendar myCalendar = Calendar.getInstance(); // a Calendar object for the event time picker
    NewService mService; // an object representing the service that we will bind this activity to
    boolean mBound = false; // a boolean representing whether the app is currently bound to the service
    int id = 0; // the id of a specific entry to be edited or deleted
    static boolean startedFlag; // a boolean representing whether the app is currently open

    /**
     * Driver method for the project that creates an object that has the library as its type
     * and calls the necessary methods that manage data in the database based off user input
     *
     * @param savedInstanceState a bundle of the saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dBmain = new DBmain(this);

        //create object
        findId();
        setUpPickers();

        //disabling the edit button because the user hasn't chosen an entry
        //to edit yet
        edit.setEnabled(false);
        edit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

        insertData();
        clearData();
        editData();

        // if the app is not opened, call the method that handles inserting a record for when the app was last closed
        if (!startedFlag) {
            insertExitRecord();
        }
    }


    /**
     * Lifecycle method that checks to see if the activity is currently bound to the service;
     * if so, it unbinds it and updates SharedPreferences with the current time
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mBound) {
            unbindService(connection);
            mBound = false;
            updateTimeStamps();
        }
    }


    /**
     * Lifecycle method that begins the process of binding the activity to the service
     */
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, NewService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }


    /**
     * Lifecycle method that checks to see whether the app is calling onDestroy() because it is
     * changing configurations; if it is not, then the app is being closed out of by the user, so
     * we begin the process of inserting an exit record into the database and update time stamps
     * for SharedPreferences so we know when the user closed out of the app
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            startedFlag = false;
            mService.insertExitRecord();
            updateTimeStamps();
        }
    }


    /**
     * Method that handles what happens when the activity is first bound to the service or when
     * the activity is first unbound from the service
     */
    private ServiceConnection connection = new ServiceConnection() {

        /**
         * Method that handles what happens when the activity is first bound to the service; it
         * updates the necessary boolean flags and calls the necessary methods to connect the
         * activity to the service
         *
         * @param className the ComponentName object necessary to override the method
         * @param service the IBinder object necessary to override the method
         */
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            NewService.LocalBinder binder = (NewService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            if (!startedFlag) {
                mService.insertStartupRecord();
                startedFlag = true;
            }
        }

        /**
         * Method that handles what happens when the activity is first unbound from the service; it
         * updates the necessary boolean flags and calls the necessary methods to disconnect the
         * activity from the service
         *
         * @param arg0 the ComponentName object necessary to override the method
         */
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            updateTimeStamps();
        }
    };


    /**
     * Method that begins the process of updating a record from the database; it grabs the
     * input from the Bundle that was created in MainActivity2 when the user indicated they want to
     * edit a particular record in the database and then sets the EditText fields of this activity
     * (the homescreen) to the existing information in the database
     */
    private void editData() {
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

            // enabling the edit button and disabling the submit button
            edit.getBackground().setColorFilter(null);
            edit.setEnabled(true);
            submit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            submit.setEnabled(false);
        }
    }


    /**
     * Method that clears the existing data in the EditText fields
     */
    private void clearData() {
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


    /**
     * Method that handles inserting a record into the database; it grabs the user's input from the
     * EditText fields and then calls the insert method from the library class that handles inserting
     * the various details that the user inputted
     */
    private void insertData() {
        submit.setOnClickListener(new View.OnClickListener() {

            /**
             * Method that handles what happens when the user clicks the Submit button on the homescreen
             *
             * @param v the View object necessary to override the method
             */
            @Override
            public void onClick(View v) {
                int param5, param6, param7, param9, param11; // the numeric EditText fields

                // beginning the process of grabbing user input
                String param2 = eventTime.getText().toString().trim();
                String param3 = hostId.getText().toString().trim();
                String param4 = userId.getText().toString().trim();
                String temp = locationNbr.getText().toString();
                try {
                    param5 = Integer.parseInt(temp);
                } catch (NumberFormatException e) {
                    param5 = -1; // set the value to -1 if the user inputted no value
                }
                String temp2 = routeNbr.getText().toString();
                try {
                    param6 = Integer.parseInt(temp2);
                } catch (NumberFormatException e) {
                    param6 = -1; // set the value to -1 if the user inputted no value
                }
                String temp3 = day.getText().toString();
                try {
                    param7 = Integer.parseInt(temp3);
                } catch (NumberFormatException e) {
                    param7 = -1; // set the value to -1 if the user inputted no value
                }
                String param8 = logger.getText().toString().trim();
                String temp4 = eventNbr.getText().toString();
                try {
                    param9 = Integer.parseInt(temp4);
                } catch (NumberFormatException e) {
                    param9 = -1; // set the value to -1 if the user inputted no value
                }
                String param10 = addtDesc.getText().toString().trim();
                String temp5 = addtNbr.getText().toString();
                try {
                    param11 = Integer.parseInt(temp5);
                } catch (NumberFormatException e) {
                    param11 = -1; // set the value to -1 if the user inputted no value
                }

                dBmain.insert(param2, param3, param4, param5, param6, param7, param8,
                        param9, param10, param11); // inserting the various values into the database
                clearData(); // clearing the input from the EditText fields
            }
        });
        display.setOnClickListener(new View.OnClickListener() {

            /**
             * Method that takes the user to the activity that displays the entries in the database
             *
             * @param v the View object necessary to override the method
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {

            /**
             * Method that handles what happens the user wants to update an exiting entry in the
             * database with the information currently in the EditText fields
             *
             * @param v the View object necessary to override the method
             */
            @Override
            public void onClick(View v) {
                int param5, param6, param7, param9, param11; // the numeric EditText fields

                // beginning the process of grabbing user input
                String param2 = eventTime.getText().toString().trim();
                String param3 = hostId.getText().toString().trim();
                String param4 = userId.getText().toString().trim();
                String temp = locationNbr.getText().toString();
                try {
                    param5 = Integer.parseInt(temp);
                } catch (NumberFormatException e) {
                    param5 = -1; // set the value to -1 if the user inputted no value
                }
                String temp2 = routeNbr.getText().toString();
                try {
                    param6 = Integer.parseInt(temp2);
                } catch (NumberFormatException e) {
                    param6 = -1; // set the value to -1 if the user inputted no value
                }
                String temp3 = day.getText().toString();
                try {
                    param7 = Integer.parseInt(temp3);
                } catch (NumberFormatException e) {
                    param7 = -1; // set the value to -1 if the user inputted no value
                }
                String param8 = logger.getText().toString().trim();
                String temp4 = eventNbr.getText().toString();
                try {
                    param9 = Integer.parseInt(temp4);
                } catch (NumberFormatException e) {
                    param9 = -1; // set the value to -1 if the user inputted no value
                }
                String param10 = addtDesc.getText().toString().trim();
                String temp5 = addtNbr.getText().toString();
                try {
                    param11 = Integer.parseInt(temp5);
                } catch (NumberFormatException e) {
                    param11 = -1; // set the value to -1 if the user inputted no value
                }

                boolean result = dBmain.updateRecord(id, param2, param3, param4, param5, param6, param7, param8,
                        param9, param10, param11); // updating the appropriate database record with the new information
                if (result) { // if the update was successful, take the user back to the activity that displays all the records
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);

                } else { // otherwise, print a toast message saying the update was unsuccessful
                    Toast.makeText(MainActivity.this, "There was an error updating. Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * Method that sets up the date and time pickers used in the event time EditText field
     */
    private void setUpPickers() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            /**
             *
             * @param view the DatePicker object necessary for the override
             * @param year the year the user selected
             * @param month the month the user selected
             * @param dayOfMonth the day of the month the user selected
             */
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                new TimePickerDialog(MainActivity.this, (view1, hourOfDay, minute) -> {
                    myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalendar.set(Calendar.MINUTE, minute);
                    updateLabel();
                }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show();
            }
        };

        // updating the calendar once the user selects a date
        eventTime.setOnClickListener(v -> new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }


    /**
     * Method that formats a string based off the date on the Calendar and then displays that
     * string in the EditText field corresponding to the event time
     */
    private void updateLabel() {
        String format = "M/d/yy h:mm:00 a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        eventTime.setText(dateFormat.format(myCalendar.getTime()));
    }


    /**
     * Method that links the fields in this class to their id's in the XML file
     */
    private void findId() {
        eventTime = (EditText) findViewById(R.id.et_event_time);
        hostId = (EditText) findViewById(R.id.et_host_id);
        userId = (EditText) findViewById(R.id.et_user_id);
        locationNbr = (EditText) findViewById(R.id.et_location_nbr);
        routeNbr = (EditText) findViewById(R.id.et_route_nbr);
        day = (EditText) findViewById(R.id.et_day);
        logger = (EditText) findViewById(R.id.et_logger);
        eventNbr = (EditText) findViewById(R.id.et_event_nbr);
        addtDesc = (EditText) findViewById(R.id.et_addt_desc);
        addtNbr = (EditText) findViewById(R.id.et_addt_nbr);

        submit = (Button) findViewById(R.id.btn_submit);
        display = (Button) findViewById(R.id.btn_display);
        edit = (Button) findViewById(R.id.btn_edit);
    }


    /**
     * Method that updates SharedPreferences with the current time so we can keep track of when the
     * user opened and closed the app
     */
    private void updateTimeStamps() {
        SharedPreferences sharedPref = getSharedPreferences(myPrefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String format = "M/d/yy h:mm:ss a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        String currentTime = dateFormat.format(myCalendar.getTime());
        editor.putString("currentTime", currentTime);
        editor.apply();
    }


    /**
     * Method that inserts a record for when the user last closed out of the app
     */
    private void insertExitRecord() {
        SharedPreferences sharedPref = getSharedPreferences(myPrefs, MODE_PRIVATE);
        if (sharedPref.getString("currentTime", null) != null) {
            dBmain.insert(sharedPref.getString("currentTime", null), "", "",
                    -1, -1, -1, "", 1, "",
                    -1);
        }
    }
}