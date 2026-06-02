package main.com.cineramamaps.Utils;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonCacheUtils {

    // Save JSON to internal storage
    public static String saveJsonToFile(Context context, String cityId, String json) throws IOException {
        File file = new File(context.getFilesDir(), "places_" + cityId + ".json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        }
        return file.getAbsolutePath();
    }

    // Read JSON from file
    public static String readJsonFromFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }
}
