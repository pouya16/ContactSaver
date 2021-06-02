package com.example.contactsaver.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.contactsaver.api.RecieverApi;

public class ContactReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        RecieverApi api = new RecieverApi(context);
        api.getContact();
    }
}
