package main.com.cineramamaps.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import main.com.cineramamaps.Entity.Room.PlacesEntity;

@Dao
public interface PlacesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlacesEntity entity);

    @Query("SELECT * FROM places WHERE city_id = :cityId LIMIT 1")
    PlacesEntity getPlacesByCity(String cityId);

    @Query("DELETE FROM places")
    void clearAll();
}
