package com.example.recyclerviewdatabaseproject;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.databaselibrary.DBmain;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    DBmain dBmain;
    Button backBtn, insertBtn, deleteAllBtn, displayXmlBtn;
    RecyclerView rv;
    private ArrayList<MyListData> myListData;
    private String serialNum;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        dBmain = new DBmain(this);
        findId();
        displayData();
        initializeButtons();

        if (ContextCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity2.this, new String[] {Manifest.permission.READ_PHONE_STATE}, 100);
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                if (Build.getRadioVersion() != null){
                    serialNum = Build.getRadioVersion();
                }
            }
            else{
                serialNum = Build.SERIAL;
            }
        }
    }

    private void displayData() {
        Cursor cursor = dBmain.getAllData();
        myListData = new ArrayList<>();
        if (cursor.getCount() > 0) { //if there's at least one entry in the database

            while (cursor.moveToNext()) {
                int param1 = cursor.getInt(0); //getting the first entry that the Cursor is pointing to
                String param2 = cursor.getString(1); //getting the second entry that the Cursor is pointing to
                String param3 = cursor.getString(2); //getting the third entry that the Cursor is pointing to
                String param4 = cursor.getString(3); //AppId
                String param5 = cursor.getString(4);
                int param6 = cursor.getInt(5);
                int param7 = cursor.getInt(6);
                int param8 = cursor.getInt(7);
                String param9 = cursor.getString(8);
                int param10 = cursor.getInt(9);
                String param11 = cursor.getString(10);
                int param12 = cursor.getInt(11);
                MyListData temp = new MyListData(param1, param2, param3, param4, param5, param6,
                        param7, param8, param9, param10, param11, param12,
                        android.R.drawable.ic_dialog_info, R.drawable.edit_logo, android.R.drawable.ic_delete);
                myListData.add(temp);
            }
        }
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        MyListAdapter adapter = new MyListAdapter(myListData, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void findId() {
        rv = findViewById(R.id.rv);
        backBtn = (Button) findViewById(R.id.btn_return_to_home);
        insertBtn = (Button) findViewById(R.id.btn_insert_device_info);
        deleteAllBtn = (Button) findViewById(R.id.btn_delete_all);
        displayXmlBtn = (Button) findViewById(R.id.btn_display_xml);
    }

    private void initializeButtons() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMemoryStatus();
                insertBatteryStatus();
                insertIpAddress();
                try {
                    insertNetworkAdapters();
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                insertSerialNumber();
                insertAppInfo();
                displayData();
            }
        });

        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dBmain.removeAll();
                displayData();
            }
        });
        displayXmlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, DisplayXml.class);
                startActivity(intent);
            }
        });
    }

    private String getCurrentTime(){
        String format = "M/d/yy h:mm:ss a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        return dateFormat.format(myCalendar.getTime());
    }

    private void insertMemoryStatus() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);
        double percentMemAvail = (memoryInfo.availMem / (double) memoryInfo.totalMem) * 100;
        int param = (int) percentMemAvail;
        String curTime = getCurrentTime();
        dBmain.insert(curTime, "", "", -1, -1, -1, "", 6, "", param);
    }

    private void insertBatteryStatus() {
        BatteryManager batteryManager = (BatteryManager) getApplicationContext().getSystemService(BATTERY_SERVICE);
        int batLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        String curTime = getCurrentTime();
        dBmain.insert(curTime, "", "", -1, -1, -1, "", 7, "", batLevel);
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    private void insertIpAddress() {
        String ipAddress = getIPAddress(true);
        String curTime = getCurrentTime();
        dBmain.insert(curTime, "", "", -1, -1, -1, "", 24, ipAddress, -1);
    }

    private void insertNetworkAdapters() throws SocketException {
        String connectivityType = "";
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = connectivityManager.getActiveNetworkInfo().isConnected();
        int netInfo = connectivityManager.getActiveNetworkInfo().getType();

        if (netInfo == ConnectivityManager.TYPE_ETHERNET && isConnected) {
            connectivityType = "Ethernet";
        }
        if (netInfo == ConnectivityManager.TYPE_WIFI && isConnected) {
            connectivityType = "Wifi";
        }
        if (netInfo == ConnectivityManager.TYPE_BLUETOOTH && isConnected) {
            connectivityType = "Bluetooth";
        }
        if (netInfo == ConnectivityManager.TYPE_MOBILE || !isConnected){
            connectivityType = "Cellular";
        }

        String curTime = getCurrentTime();
        dBmain.insert(curTime, "", "", -1, -1, -1, "", 23, connectivityType, -1);
    }

    private void insertSerialNumber(){
        //String serialNum = getSerialNumber();
        String curTime = getCurrentTime();
        dBmain.insert(curTime, serialNum, "", -1, -1, -1, "", -1, "", -1);
    }

    private String getSerialNumber() {
        String serialNum = "";
        if (ContextCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity2.this, new String[] {Manifest.permission.READ_PHONE_STATE}, 100);
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                if (Build.getRadioVersion() != null){
                    serialNum = Build.getRadioVersion();
                }
            }
            else{
                serialNum = Build.SERIAL;
            }
        }
        return serialNum;
    }

    private void insertAppInfo(){
        int versionNum = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        String curTime = getCurrentTime();
        dBmain.insert(curTime, "", "", -1, -1, -1, "",
                21, versionName, versionNum);
    }
}
