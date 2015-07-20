package com.amimobenja.www.gtext;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16 June 2015.
 */
public class DBAdapterTwo {
    static final String KEY_ROW_ID = "_id";
    static final String KEY_PHONE_NUMBER = "phone_number";
    static final String KEY_FIRST_NAME = "first_name";
    static final String KEY_SECOND_NAME = "second_name";
    static final String KEY_NICK_NAME = "nick_name";
    static final String KEY_GROUP_ASSIGN = "group_assign";

    static final String DATABASE_NAME = "Registration";
    static final String DATABASE_TABLE = "registration_tbl";
    static final int DATABASE_VERSION = 6;

    static final String DATABASE_CREATE_REG = "CREATE TABLE "+DATABASE_TABLE+" ("+KEY_ROW_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_PHONE_NUMBER+" INTEGER, " +
            ""+KEY_FIRST_NAME+" TEXT NOT NULL, "+KEY_SECOND_NAME+" TEXT NOT NULL, " +
            ""+KEY_NICK_NAME+" TEXT NOT NULL, "+KEY_GROUP_ASSIGN+" TEXT NOT NULL);";

    Context context = null;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapterTwo(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_REG);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            System.out.println("Upgrading "+DATABASE_NAME+" from "+oldVersion+" to "+newVersion+", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE+";");
            onCreate(db);
        }

    }

    //--- Opens the database---
    public DBAdapterTwo open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //--- Closes the database---
    public void close() {
        DBHelper.close();
    }


    //--- Insert a Group into the database---
    public long insertContact(String phone_no, String f_name, String s_name, String n_name, String grp) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PHONE_NUMBER, phone_no);
        initialValues.put(KEY_FIRST_NAME, f_name);
        initialValues.put(KEY_SECOND_NAME, s_name);
        initialValues.put(KEY_NICK_NAME, n_name);
        initialValues.put(KEY_GROUP_ASSIGN, grp);
        return  db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---Deletes a particular value---
    public boolean deleteValue(String p_no, String groupAssgn) {
        return db.delete(DATABASE_TABLE, KEY_PHONE_NUMBER + "='"+p_no+"' AND "+KEY_GROUP_ASSIGN +"='"+groupAssgn+"'", null) > 0;
    }

    //---Retrieves all the Groups ---
    public Cursor getAllRegPeople(String groupName) {
        return db.query(DATABASE_TABLE, new String[] {KEY_PHONE_NUMBER, KEY_FIRST_NAME, KEY_SECOND_NAME,
                KEY_NICK_NAME, KEY_GROUP_ASSIGN}, KEY_GROUP_ASSIGN +"=?", new String[] {groupName}, null, null, KEY_FIRST_NAME);


    }

    //---Retrieves a particular contact---
    public Cursor getPerson(String p_no, String group) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {KEY_PHONE_NUMBER, KEY_FIRST_NAME, KEY_SECOND_NAME,
                KEY_NICK_NAME, KEY_GROUP_ASSIGN}
                , KEY_PHONE_NUMBER +"=? AND "+KEY_GROUP_ASSIGN+"=?",  new String[] {p_no, group}, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return  mCursor;
    }


    //--- Update a Group---
    public boolean updateContact(String phone_no, String f_name, String s_name, String n_name, String grp) {
        ContentValues args = new ContentValues();
        args.put(KEY_PHONE_NUMBER, phone_no);
        args.put(KEY_FIRST_NAME, f_name);
        args.put(KEY_SECOND_NAME, s_name);
        args.put(KEY_NICK_NAME, n_name);
        args.put(KEY_GROUP_ASSIGN, grp);
        return db.update(DATABASE_TABLE, args, KEY_PHONE_NUMBER +"="+ phone_no, null) > 0;
    }

    public List<String> getPhoneNumbers(String grp){
        List<String> labels = new ArrayList<String>();
        System.out.println("ENTERED getPhoneNumber");

        // Select All Query
        Cursor cursor = db.query(DATABASE_TABLE, new String[] {KEY_PHONE_NUMBER},
                KEY_GROUP_ASSIGN+"=? ",  new String[] {grp}, null, null, null);
        System.out.println("ENTERED cursor");

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            System.out.println("ENTERED list");
            do {
                System.out.println("GET PHONE NO - "+cursor.getString(0));
                labels.add("0"+cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();

        // returning lables
        return labels;
    }

}
