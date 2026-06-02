package main.com.cineramamaps.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "MOVIES";

    // Table columns
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String YEARRELEASE = "year_release";
    public static final String DISTRIBUTOR = "distributor";
    public static final String ACTORS = "actors";
    public static final String DESC = "description";

    // Database Information
    static final String DB_NAME = "POCKET_FILMS.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE + " TEXT NOT NULL, " + YEARRELEASE + " TEXT, " + DISTRIBUTOR + " TEXT, " + ACTORS + " TEXT, " + DESC + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}