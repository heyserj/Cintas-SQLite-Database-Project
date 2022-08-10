package com.example.recyclerviewdatabaseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Class that represents the activity in the app that displays all of the data fields for a
 * particular record
 */
public class Display extends AppCompatActivity {

    Button goBack; // Button that takes the user back to the main activity displaying all of the records
    TextView eventTime, hostId, userId, locationNbr, routeNbr, appId; // the TextViews for the different fields in the record
    TextView day, logger, eventNbr, addtDesc, addtNbr; // the TextViews for the different fields in the record
    int id = 0; // the id of a specific entry


    /**
     * Driver method for the project that calls the necessary methods that retrieve and display all
     * the data in the database for the desired record
     *
     * @param savedInstanceState a bundle of the saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        initializeId();
        initializeTextViews();
        initializeReturnButton();
    }


    /**
     * Method that links the fields in this class to their id's in the XML file
     */
    private void initializeId(){
        goBack = (Button) findViewById(R.id.btn_return);
        eventTime = (TextView) findViewById(R.id.tv_event_time);
        hostId = (TextView) findViewById(R.id.tv_host_id);
        userId = (TextView) findViewById(R.id.tv_user_id);
        appId = (TextView) findViewById(R.id.tv_app_id);
        locationNbr = (TextView) findViewById(R.id.tv_location_nbr);
        routeNbr = (TextView) findViewById(R.id.tv_route_nbr);
        day = (TextView) findViewById(R.id.tv_day);
        logger = (TextView) findViewById(R.id.tv_logger);
        eventNbr = (TextView) findViewById(R.id.tv_event_nbr);
        addtDesc = (TextView) findViewById(R.id.tv_addt_desc);
        addtNbr = (TextView) findViewById(R.id.tv_addt_nbr);
    }


    /**
     * Method that sets the text in the TextViews to the corresponding fields in the database
     */
    private void initializeTextViews(){
        Bundle bundle = getIntent().getBundleExtra("record");
        id = bundle.getInt("id");

        SpannableString sb = new SpannableString("Event Time: " + bundle.getString("eventTime"));
        sb.setSpan(new StyleSpan(Typeface.BOLD), 0, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        eventTime.setText(sb);

        SpannableString sb2 = new SpannableString("Serial Number: " + bundle.getString("hostId"));
        sb2.setSpan(new StyleSpan(Typeface.BOLD), 0, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        hostId.setText(sb2);

        SpannableString sb3 = new SpannableString("Employee ID: " + bundle.getString("userId"));
        sb3.setSpan(new StyleSpan(Typeface.BOLD), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        userId.setText(sb3);

        SpannableString sb4 = new SpannableString("Location: " + bundle.getInt("locationNbr"));
        sb4.setSpan(new StyleSpan(Typeface.BOLD), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        locationNbr.setText(sb4);

        SpannableString sb5 = new SpannableString("Route Number: " + bundle.getInt("routeNbr"));
        sb5.setSpan(new StyleSpan(Typeface.BOLD), 0, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        routeNbr.setText(sb5);

        SpannableString sb6 = new SpannableString("Day: " + bundle.getInt("day"));
        sb6.setSpan(new StyleSpan(Typeface.BOLD), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        day.setText(sb6);

        SpannableString sb7 = new SpannableString("Logger: " + bundle.getString("logger"));
        sb7.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        logger.setText(sb7);

        SpannableString sb8 = new SpannableString("Event Number: " + bundle.getInt("eventNbr"));
        sb8.setSpan(new StyleSpan(Typeface.BOLD), 0, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        eventNbr.setText(sb8);

        SpannableString sb9 = new SpannableString("Additional Event Description: " + bundle.getString("addtDesc"));
        sb9.setSpan(new StyleSpan(Typeface.BOLD), 0, 29, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        addtDesc.setText(sb9);

        SpannableString sb10 = new SpannableString("Additional Event Numbers: " + bundle.getInt("addtNbr"));
        sb10.setSpan(new StyleSpan(Typeface.BOLD), 0, 25, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        addtNbr.setText(sb10);

        SpannableString sb11 = new SpannableString("App ID: CPRNT");
        sb11.setSpan(new StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        appId.setText(sb11);
    }


    /**
     * Method that initializes the return button so that it takes the user back to the main
     * activity that displays the event times from all of the records in the database
     */
    private void initializeReturnButton(){
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Display.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }
}
