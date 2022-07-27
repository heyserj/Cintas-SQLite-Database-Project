package com.example.recyclerviewdatabaseproject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.databaselibrary.DBmain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class NewService extends Service {

    DBmain dBmain;
    final Calendar myCalendar = Calendar.getInstance();
    private final IBinder binder = new LocalBinder();
    public static boolean isRunning = false;

    public class LocalBinder extends Binder {
        NewService getService(){
            return NewService.this;
        }
    }

    @Override
    public void onCreate(){
        dBmain = new DBmain(this);
        isRunning = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        //insertExitRecord();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public boolean insertStartupRecord(){
        String format = "M/d/yy h:mm:ss a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        String currentTime = dateFormat.format(myCalendar.getTime());
        return dBmain.insert(currentTime, "", "", -1, -1, -1, "", 0, "", -1);
    }

    public boolean insertExitRecord(){
        String format = "M/d/yy h:mm:ss a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        String currentTime = dateFormat.format(myCalendar.getTime());
        return dBmain.insert(currentTime, "", "", -1, -1, -1, "", 1, "", -1);
    }
}
