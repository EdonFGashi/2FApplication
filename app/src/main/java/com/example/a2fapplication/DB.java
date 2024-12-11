package com.example.a2fapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import org.mindrot.jbcrypt.BCrypt;
public class DB extends SQLiteOpenHelper {

    public static final String DBNAME="2FADB.db";

    public DB(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table users(email TEXT PRIMARY KEY, name TEXT, lastName TEXT, phone TEXT, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists users");
        onCreate(db);
    }

    public Boolean insertUser(String email, String firstName, String lastName, String phone, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        contentValues.put("email", email);
        contentValues.put("name", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("phone", phone);
        contentValues.put("password", hashedPassword);

        long result = db.insert("users", null, contentValues);
        return result != -1;
    }

    public Boolean updatePassword(String email, String newPassword){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        contentValues.put("password", hashedPassword);

        // Use the email to find the specific user and update the password
        int result = db.update("users", contentValues, "email = ?", new String[]{email});
        return result > 0;
    }

    public Boolean checkEmail(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=?", new String[]{email});
        boolean exists =cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Boolean validateUser(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select password from users where email=?", new String[]{email});

        if(cursor.moveToFirst()){
            String hashedPassword =cursor.getString(0);
            cursor.close();

            return BCrypt.checkpw(password, hashedPassword);
        }

        cursor.close();
        return false;
    }
}
