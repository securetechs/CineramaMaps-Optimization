package main.com.cineramamaps.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import main.com.cineramamaps.Entity.Room.PlacesEntity;
import main.com.cineramamaps.interfaces.PlacesDao;

@Database(entities = {PlacesEntity.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PlacesDao placesDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "places_database"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
