package com.example.contactsaver;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.contactsaver.broadcast.ContactReceiver;
import com.example.contactsaver.classes.RequestHelper;
import com.example.contactsaver.helper.DbHelper;
import com.example.contactsaver.layout.ValueSelector;

import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    int hasContactPermision;
    ValueSelector valueSelector;
    ImageButton btnConnect;
    PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    int situation = 0;
    final int BROADCAST_ID = 100;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        valueSelector = findViewById(R.id.value_selector);
        btnConnect = findViewById(R.id.power_btn);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(this, ContactReceiver.class);

        hasContactPermision = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);

        if(hasContactPermision == 0){
            Log.i("Log","permission is ready");

        }else{
            Log.i("Log","going for permission");
            requestForWriteContact();
        }


        final DbHelper dbHelper = new DbHelper(this);
        dbHelper.createOrOpenDataBase();

        if(dbHelper.isSaved()){
            situation = dbHelper.getSituation();
            if(situation == 1){
                setBtnConnect(btnConnect);
            }else{
                setBtnNotConnect(btnConnect);
            }
        }else{
            dbHelper.writeConnection(0,BROADCAST_ID);
            setBtnNotConnect(btnConnect);
        }


        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(situation==1){
                        pendingIntent = PendingIntent.getBroadcast(HomeActivity.this,BROADCAST_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.cancel(pendingIntent);
                        setBtnNotConnect(btnConnect);
                        dbHelper.updateConnection(0);
                        situation = 0;

                }else {
                    Log.i("Log","value is : " + valueSelector.getValue());
                    if(valueSelector.getValue()>0){

                        Date date = new Date();
                        Calendar cal_now = Calendar.getInstance();
                        cal_now.setTime(date);
                        int intervals = valueSelector.getValue() * 1000* 60;
                        pendingIntent =PendingIntent.getBroadcast(HomeActivity.this,BROADCAST_ID,intent,0);
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,cal_now.getTimeInMillis(),intervals,pendingIntent);
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,cal_now.getTimeInMillis(),intervals,pendingIntent);
                        }
                        situation = 1;
                        dbHelper.updateConnection(1);
                        setBtnConnect(btnConnect);

                    } else{
                        Toast.makeText(HomeActivity.this,"Should be greater than 0",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });





    }
    public void requestForWriteContact() {
        RequestHelper request = new RequestHelper(this);
        RequestHelper.OnGrantedListener grantedListenerListener = new RequestHelper.OnGrantedListener() {
            @Override
            public void onGranted() {

            }
        };

        RequestHelper.OnAlreadyGrantedListener onAlreadyGrantedListener = new RequestHelper.OnAlreadyGrantedListener() {
            @Override
            public void onAlreadyGranted() {

            }
        };

        RequestHelper.OnDeniedListener deniedListener = new RequestHelper.OnDeniedListener() {
            @Override
            public void onDenied() {
                new AlertDialog.Builder(HomeActivity.this)
                        .setMessage(R.string.ask_contact)
                        .setPositiveButton(R.string.ask_again, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestForWriteContact();
                            }
                        })
                        .create()
                        .show();
            }
        };
        request.request(Manifest.permission.WRITE_CONTACTS, grantedListenerListener, onAlreadyGrantedListener, deniedListener);
    }

    private void setBtnNotConnect(ImageButton imageButton){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageButton.setImageDrawable(getDrawable(R.drawable.ic_power_off));

        }else{
            imageButton.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this,R.drawable.ic_power_off));

        }
        imageButton.setBackground(null);

    }
    private void setBtnConnect(ImageButton imageButton){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageButton.setImageDrawable(getDrawable(R.drawable.ic_power_on_24dp));
            imageButton.setBackground(getDrawable(R.drawable.background));

        }else{
            imageButton.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this,R.drawable.ic_power_off));
            imageButton.setBackground(ContextCompat.getDrawable(HomeActivity.this,R.drawable.background));

        }

    }
}
