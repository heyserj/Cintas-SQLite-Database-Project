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

public class Display extends AppCompatActivity {

    Button goBack;
    TextView eventTime, hostId, userId, locationNbr, routeNbr, appId;
    TextView day, logger, eventNbr, addtDesc, addtNbr;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        initializeId();
        initializeTextViews();
        initializeReturnButton();
    }

    private void initializeId(){
        goBack = (Button) findViewById(R.id.return_btn);
        eventTime = (TextView) findViewById(R.id.tv_EventTime);
        hostId = (TextView) findViewById(R.id.tv_HostId);
        userId = (TextView) findViewById(R.id.tv_UserId);
        appId = (TextView) findViewById(R.id.tv_AppId);
        locationNbr = (TextView) findViewById(R.id.tv_LocationNbr);
        routeNbr = (TextView) findViewById(R.id.tv_RouteNbr);
        day = (TextView) findViewById(R.id.tv_Day);
        logger = (TextView) findViewById(R.id.tv_Logger);
        eventNbr = (TextView) findViewById(R.id.tv_EventNbr);
        addtDesc = (TextView) findViewById(R.id.tv_AddtDesc);
        addtNbr = (TextView) findViewById(R.id.tv_AddtNbr);
    }

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