package main.com.cineramamaps.Entity.Room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "places")
public class PlacesEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "city_id")
    private String cityId;

    @ColumnInfo(name = "file_path")
    private String filePath;

    @ColumnInfo(name = "icon_path")
    private String iconPath;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    // Getters & Setters
    @NonNull
    public String getCityId() { return cityId; }
    public void setCityId(@NonNull String cityId) { this.cityId = cityId; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getIconPath() { return iconPath; }
    public void setIconPath(String iconPath) { this.iconPath = iconPath; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
