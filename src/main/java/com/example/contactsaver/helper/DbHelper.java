package com.example.contactsaver.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.contactsaver.classes.ContactsSituation;
import com.example.contactsaver.database.Database;

public class DbHelper {
    Context context;

    public static SQLiteDatabase database;
    private Database dbHelper;
    ContactsSituation contactsSituation;

    int isDataBaseCreated = 0;

    public DbHelper(Context context) {
        this.context = context;
        dbHelper = new Database(context);
        createOrOpenDataBase();
    }

    public void createOrOpenDataBase(){
        isDataBaseCreated = 1;
        if(database!=null){
            Log.i("Log1","Database was open");
            return;
        }
        Log.i("Log1","Data base is openning");
        database = dbHelper.getWritableDatabase();
    }

    public boolean isSaved(){
        return dbHelper.isUser(database);
    }

    public int getSituation(){
        contactsSituation = dbHelper.getUser(database);
        return contactsSituation.getSituation();
    }

    public void writeConnection(int situation,int id){
        ContactsSituation contactsSituation = new ContactsSituation(id,"connection",situation);
        dbHelper.saveUser(database,contactsSituation);
    }

    public void updateConnection(int situation){
        contactsSituation = dbHelper.getUser(database);
        contactsSituation.setSituation(situation);
        dbHelper.updateSituation(database,contactsSituation,contactsSituation.getAutoid());
    }



}
