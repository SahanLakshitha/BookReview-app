package com.what_to_read;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseFactory extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database1.db";

    public DatabaseFactory(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user(userName TEXT PRIMARY KEY, password TEXT, email TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS blog(blog_id TEXT PRIMARY KEY, part TEXT, brand TEXT, description TEXT, email TEXT, name TEXT, blog_img BLOB, date TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS currentUser(name TEXT, email text, user_img BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor get_by_Part_posted_blog(String part){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor2 = database.rawQuery("SELECT * FROM blog WHERE part = \"" + part + "\"", null);

        return cursor2;
    }

    public boolean save_user_data(String userName, String password, String email){
        try {
            ContentValues sql_values = new ContentValues();
            sql_values.put("userName", userName);
            sql_values.put("password", password);
            sql_values.put("email", email);
            SQLiteDatabase db = this.getWritableDatabase();
            long result = db.insert("user", null, sql_values);
            if(result == -1){
                return  false;
            }else {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    public Cursor verifyUser(String userName){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM user WHERE userName = \"" + userName + "\"", null);
        
        return cursor;
    }

    public boolean add_posts_data(String post_id, String part, String brand, String description, byte[] blog_img, String email, String name, String date){
        try {
            ContentValues sql_values = new ContentValues();
            sql_values.put("blog_id", post_id);
            sql_values.put("part", part);
            sql_values.put("brand", brand);
            sql_values.put("description", description);
            sql_values.put("email", email);
            sql_values.put("name", name);
            sql_values.put("blog_img", blog_img);
            sql_values.put("date", date);
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert("blog", null, sql_values);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Cursor delete_post (String postId){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("DELETE FROM blog WHERE blog_id = \"" + postId + "\"", null);
//        database.execSQL("delete from "+ "messageData" + postId);
        return cursor;
    }

    public boolean save_Current_user(String user_name, String email, byte[] user_img){
        try {
            ContentValues sql_values = new ContentValues();
            sql_values.put("email", email);
            sql_values.put("name", user_name);
            sql_values.put("user_img", user_img);
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert("currentUser", null, sql_values);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void delete_curent_user(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("delete from "+ "currentUser");
    }

    public Cursor get_current_user(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM currentUser", null);
        return cursor;
    }


    public Cursor get_all_blog(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM blog ORDER BY date DESC", null);
        return cursor;
    }

    public Cursor get_messages(Blog_Details data){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("CREATE TABLE IF NOT EXISTS messageData" + data.getPost_id() + "(message_id TEXT PRIMARY KEY, sender_email TEXT, sender_name TEXT, message_ TEXT );");

        Cursor cursor = database.rawQuery("SELECT * FROM messageData" + data.getPost_id(), null);
        return cursor;
    }

    public boolean add_Comment(Blog_Details data, String message, String message_id, String email, String name){
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            ContentValues sql_values = new ContentValues();
            sql_values.put("message_id", message_id);
            sql_values.put("sender_email", email);
            sql_values.put("sender_name", name);
            sql_values.put("message_", message);
            database.insert("messageData" + data.getPost_id(), null, sql_values);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void delete_msg(Blog_Details data, String message_id){
        SQLiteDatabase database = this.getWritableDatabase();
        String strSQL = "UPDATE messageData" + data.getPost_id() + " SET message_ = \"" + "message deleted" + "\" WHERE message_id = \""+ message_id + "\"";
        database.execSQL(strSQL);
    }
}
