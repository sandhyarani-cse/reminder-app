package com.example.reminderapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.reminderapp.models.Reminder;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite database helper — full CRUD for reminders and users.
 * Implemented with SQLiteOpenHelper as specified in README.
 * (Original APK used Room; this aligns with README's stated tech stack.)
 */
public class ReminderDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "reminder_db";
    private static final int    DB_VERSION = 1;

    // Reminders table
    private static final String TABLE_REMINDERS  = "reminders";
    private static final String COL_ID           = "id";
    private static final String COL_TITLE        = "title";
    private static final String COL_DATE         = "date";
    private static final String COL_TIME         = "time";

    // Users table
    private static final String TABLE_USERS      = "users";
    private static final String COL_USERNAME     = "username";   // fixed: original APK had typo 'usename'
    private static final String COL_PASSWORD     = "password";
    private static final String COL_IS_LOGGED_IN = "is_logged_in";

    public ReminderDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_REMINDERS + " (" +
                COL_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT NOT NULL, " +
                COL_DATE  + " TEXT NOT NULL, " +
                COL_TIME  + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME     + " TEXT NOT NULL UNIQUE, " +
                COL_PASSWORD     + " TEXT NOT NULL, " +
                COL_IS_LOGGED_IN + " INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ── Reminder CRUD ──────────────────────────────────────────────

    public long insertReminder(Reminder r) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, r.getTitle());
        cv.put(COL_DATE,  r.getDate());
        cv.put(COL_TIME,  r.getTime());
        return db.insert(TABLE_REMINDERS, null, cv);
    }

    public List<Reminder> getAllReminders() {
        List<Reminder> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_REMINDERS, null, null, null, null, null,
                COL_DATE + " ASC, " + COL_TIME + " ASC");
        while (cursor.moveToNext()) {
            Reminder r = new Reminder();
            r.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
            r.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)));
            r.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)));
            r.setTime(cursor.getString(cursor.getColumnIndexOrThrow(COL_TIME)));
            list.add(r);
        }
        cursor.close();
        return list;
    }

    public int deleteReminder(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_REMINDERS, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int updateReminder(Reminder r) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, r.getTitle());
        cv.put(COL_DATE,  r.getDate());
        cv.put(COL_TIME,  r.getTime());
        return db.update(TABLE_REMINDERS, cv, COL_ID + "=?",
                new String[]{String.valueOf(r.getId())});
    }

    // ── User CRUD ──────────────────────────────────────────────────

    public boolean registerUser(String username, String password) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COL_USERNAME, username);
            cv.put(COL_PASSWORD, password);
            return db.insert(TABLE_USERS, null, cv) != -1;
        } catch (Exception e) {
            return false; // username already exists
        }
    }

    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        boolean valid = cursor.getCount() > 0;
        cursor.close();
        return valid;
    }

    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_ID},
                COL_USERNAME + "=?", new String[]{username}, null, null, null);
        boolean taken = cursor.getCount() > 0;
        cursor.close();
        return taken;
    }
}
