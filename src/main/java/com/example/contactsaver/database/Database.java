package com.example.contactsaver.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.contactsaver.classes.ContactsSituation;


public class Database extends SQLiteOpenHelper {
    private static final String DB_NAME = "contact_saver";
    private static final int DB_VERSION = 1;
    private Context context;

    public Database(Context context){
        super(context,context.getFilesDir() + "/" + DB_NAME ,null,DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createUserTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }



    public void createUserTable(SQLiteDatabase db) {
        String query =
                "CREATE TABLE 'user' (" +
                        //0:
                        "'autoid' INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL , " +
                        "'id' TEXT , " +
                        "'name' TEXT , " +
                        "'situation' INTEGER " +
                        ")";

        db.execSQL(query);
    }


    public boolean saveUser(SQLiteDatabase db, ContactsSituation contactsSituation){
        boolean success = false;
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",contactsSituation.getId());
        contentValues.put("name",contactsSituation.getName());
        contentValues.put("situation",contactsSituation.getSituation());
        try{
            db.insert("user",null,contentValues);
            success = true;
            Log.i("Log1", "data is successfully added to database");
        }catch (Exception e){
            Log.i("Log1", "error writing data to database: " + e.toString());
            success = false;
        }
        return success;
    }

    public boolean isUser(SQLiteDatabase db){
        boolean isUSer = false;
        Cursor c = db.rawQuery("SELECT * FROM user", null);
        if(c.getCount()>0){
            isUSer = true;
        }
        return isUSer;
    }

    public ContactsSituation getUser(SQLiteDatabase db){
        ContactsSituation userClass = new ContactsSituation();
        Cursor c = db.rawQuery("SELECT * FROM user", null);
        if(c.getCount()>0) {
            c.moveToFirst();
            Log.i("Log1","stored token inside database is: " + c.getString(1));
            Log.i("Log1","stored id inside database is: " + c.getString(2));
            Log.i("Log1","stored name inside database is: " + c.getString(3));
            userClass.setId(c.getInt(1));
            userClass.setAutoid(c.getInt(0));
            userClass.setName(c.getString(2));
            userClass.setSituation(c.getInt(3));
        }else{

        }
        return userClass;
    }

    public boolean DeleteAllUsers(SQLiteDatabase db){
        boolean isSuccess = false;
        try {
            db.execSQL("delete from user");
            isSuccess = true;
        }catch (Exception e){
            Log.i("Log1","failed to delete table");
        }
        return isSuccess;
    }

    public  boolean updateSituation(SQLiteDatabase db,ContactsSituation contactsSituation,int id){
        boolean isSuccess = false;
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",contactsSituation.getId());
        contentValues.put("name",contactsSituation.getName());
        contentValues.put("situation",contactsSituation.getSituation());
        try {
            db.update("user",contentValues,"autoid = ?",new String[]{"" + id});
            isSuccess = true;
        }catch (Exception e){

        }
        return isSuccess;
    }


}
