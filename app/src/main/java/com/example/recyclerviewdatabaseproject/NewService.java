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


/**
 * Class that represents the service that MainActivity connects to whenever the app is open. It's
 * used to help tell whenever the app is first opened or closed so that we can enter accurate
 * time stamps into the database for records signifying when the app is opened or closed.
 */
public class NewService extends Service {

    DBmain dBmain; // an object corresponding to the library database that we can call the library's methods on
    final Calendar myCalendar = Calendar.getInstance(); // a Calendar object to help us tell what the current time is
    private final IBinder binder = new LocalBinder(); // an IBinder object to help with the binding process

    /**
     * Class that's used to help with the binding process; the constructor returns the current
     * service object
     */
    public class LocalBinder extends Binder {
        NewService getService(){
            return NewService.this;
        }
    }


    /**
     * Method for the project that creates an object that has the library as its type so that we can
     * call the library's methods for inserting new records into the database
     */
    @Override
    public void onCreate(){
        dBmain = new DBmain(this);
    }


    /**
     * Lifecycle method that handles what happens when the service is closed out of
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * Method that starts the binding process by returning an IBinder object that we can use later
     *
     * @param intent the calling intent
     * @return the IBinder field we initialized earlier
     */
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    /**
     * Method that inserts a record for when the user first opened the app
     *
     * @return a boolean representing whether the insert was successful or not; true means the
     * insert was successful and false means it was not
     */
    public boolean insertStartupRecord(){
        String format = "M/d/yy h:mm:ss a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        String currentTime = dateFormat.format(myCalendar.getTime());
        return dBmain.insert(currentTime, "", "", -1, -1, -1, "", 0, "", -1);
    }


    /**
     * Method that inserts a record for when the user first closed out of the app
     *
     * @return a boolean representing whether the insert was successful or not; true means the
     * insert was successful and false means it was not
     */
    public boolean insertExitRecord(){
        String format = "M/d/yy h:mm:ss a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        String currentTime = dateFormat.format(myCalendar.getTime());
        return dBmain.insert(currentTime, "", "", -1, -1, -1, "", 1, "", -1);
    }
}
