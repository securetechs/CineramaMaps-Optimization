package main.com.cineramamaps.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import main.com.cineramamaps.model.MovieBeanList;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String yearrelease, String distributor, String actors, String desc) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.TITLE, name);
        contentValue.put(DatabaseHelper.YEARRELEASE, yearrelease);
        contentValue.put(DatabaseHelper.DISTRIBUTOR, distributor);
        contentValue.put(DatabaseHelper.ACTORS, actors);
        contentValue.put(DatabaseHelper.DESC, desc);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch(String _id) {
        String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.TITLE, DatabaseHelper.YEARRELEASE, DatabaseHelper.DISTRIBUTOR, DatabaseHelper.ACTORS, DatabaseHelper.DESC};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public MovieBeanList getMovieDetail(String _id) {
        MovieBeanList movieBeanList = null;
        Cursor cursor =  database.rawQuery("select * from " + DatabaseHelper.TABLE_NAME + " where " + DatabaseHelper._ID + "='" + _id + "'" , null);

        if (cursor != null) {
            int id = cursor.getColumnIndex(DatabaseHelper._ID);
            int title = cursor.getColumnIndex(DatabaseHelper.TITLE);
            int yearrelease = cursor.getColumnIndex(DatabaseHelper.YEARRELEASE);
            int distributor = cursor.getColumnIndex(DatabaseHelper.DISTRIBUTOR);
            int actors = cursor.getColumnIndex(DatabaseHelper.ACTORS);
            int desc = cursor.getColumnIndex(DatabaseHelper.DESC);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String id_str = cursor.getString(id);
                String title_str = cursor.getString(title);
                String yearrelease_str = cursor.getString(yearrelease);
                String distributor_str = cursor.getString(distributor);
                String actors_str = cursor.getString(actors);
                String desc_str = cursor.getString(desc);


                movieBeanList = new MovieBeanList(id_str, title_str, yearrelease_str, distributor_str, actors_str, desc_str);

            }
        }
        return movieBeanList;
    }


    public List<MovieBeanList> getMovies() {

        List<MovieBeanList> movie_list = new ArrayList<MovieBeanList>();
        String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.TITLE, DatabaseHelper.YEARRELEASE, DatabaseHelper.DISTRIBUTOR, DatabaseHelper.ACTORS, DatabaseHelper.DESC};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {


            int id = cursor.getColumnIndex(DatabaseHelper._ID);
            int title = cursor.getColumnIndex(DatabaseHelper.TITLE);
            int yearrelease = cursor.getColumnIndex(DatabaseHelper.YEARRELEASE);
            int distributor = cursor.getColumnIndex(DatabaseHelper.DISTRIBUTOR);
            int actors = cursor.getColumnIndex(DatabaseHelper.ACTORS);
            int desc = cursor.getColumnIndex(DatabaseHelper.DESC);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String id_str = cursor.getString(id);
                String title_str = cursor.getString(title);
                String yearrelease_str = cursor.getString(yearrelease);
                String distributor_str = cursor.getString(distributor);
                String actors_str = cursor.getString(actors);
                String desc_str = cursor.getString(desc);


                movie_list.add(new MovieBeanList(id_str, title_str, yearrelease_str, distributor_str, actors_str, desc_str));

            }
        }
        return movie_list;
    }

    public int update(String _id,String name, String yearrelease, String distributor, String actors, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TITLE, name);
        contentValues.put(DatabaseHelper.YEARRELEASE, yearrelease);
        contentValues.put(DatabaseHelper.DISTRIBUTOR, distributor);
        contentValues.put(DatabaseHelper.ACTORS, actors);
        contentValues.put(DatabaseHelper.DESC, desc);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(String _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}
