package com.amimobenja.www.gtext;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by User on 16 June 2015.
 */
public class DBAdapter {
    static final String KEY_ROWID = "_id";
    static final String KEY_GROUP_NAME = "group_name";
    static final String KEY_GROUP_DESCRIPTION = "group_description";

    static final String DATABASE_NAME = "GroupSMS";
    static final String DATABASE_TABLE = "groups_tbl";
    static final int DATABASE_VERSION = 3;

    static final String DATABASE_CREATE_GSMS = "CREATE TABLE "+DATABASE_TABLE+" ("+KEY_ROWID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ""+KEY_GROUP_NAME+" TEXT NOT NULL, "+KEY_GROUP_DESCRIPTION+" TEXT NOT NULL);";

    Context context = null;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_GSMS);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            System.out.println("Upgrading "+DATABASE_NAME+" from "+oldVersion+" to "+newVersion+", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE+";");
            onCreate(db);
        }

    }

    //--- Opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //--- Closes the database---
    public void close() {
        DBHelper.close();
    }

    //--- Insert a Group into the database---
    public long insertContact(String grp_name, String grp_descrp) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_GROUP_NAME, grp_name);
        initialValues.put(KEY_GROUP_DESCRIPTION, grp_descrp);
        return  db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---Deletes a particular value---
    public boolean deleteValue(long rowId) {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" +rowId, null) > 0;
    }

    //---Retrieves all the Groups ---
    public Cursor getAllGroups() {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_GROUP_NAME, KEY_GROUP_DESCRIPTION}, null, null, null, null, null);
    }

    //---Retrieves a particular contact---
    public Cursor getGroup(long rowId) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_GROUP_NAME, KEY_GROUP_DESCRIPTION}
                , KEY_ROWID +"="+rowId, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return  mCursor;
    }

    //--- Update a Group---
    public boolean updateContact(long rowId, String grp_name, String group_descript) {
        ContentValues args = new ContentValues();
        args.put(KEY_GROUP_NAME, grp_name);
        args.put(KEY_GROUP_DESCRIPTION, group_descript);
        return db.update(DATABASE_TABLE, args, KEY_ROWID +"="+ rowId, null) > 0;
    }

}
