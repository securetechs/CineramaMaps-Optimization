package main.com.cineramamaps.Utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import main.com.cineramamaps.model.CurrencyModel;

public class AppConstant {

    public static String SAUDI_ARABIA = "Saudi Arabia";


    public static double getExchangeValue(Context mContext){
        try{
            String json = Preferences.get(mContext,Preferences.KEY_EXACHANGE_JSON);
            JSONObject mainJson = new JSONObject(json);
            JSONObject sarJson = mainJson.getJSONObject("sar");
            return sarJson.getDouble(Preferences.get(mContext,Preferences.KEY_CURRENCY).toLowerCase());
        }catch (Exception e){
            e.printStackTrace();
        }
        return 1;
    }


    public static double getUSDExchangeValue(Context mContext){
        try{
            String json = Preferences.get(mContext,Preferences.KEY_EXACHANGE_JSON);
            JSONObject mainJson = new JSONObject(json);
            JSONObject sarJson = mainJson.getJSONObject("sar");
            return sarJson.getDouble("usd");
        }catch (Exception e){
            e.printStackTrace();
        }
        return 1;
    }

    public static ArrayList<CurrencyModel> loadCurrencyFromAssets(Context mContext) {
        String json = null;
        ArrayList<CurrencyModel> mCurrencyList = new ArrayList<>();

        try {
            InputStream inputStream = mContext.getAssets().open("currency.json"); // Open the file
            int size = inputStream.available(); // Get the file size
            byte[] buffer = new byte[size]; // Create a buffer
            inputStream.read(buffer); // Read the file into the buffer
            inputStream.close(); // Close the stream
            json = new String(buffer, StandardCharsets.UTF_8); // Convert to a string

            JSONArray jsonArray = new JSONArray(json);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CurrencyModel currencyModel = new CurrencyModel();
                currencyModel.setCurrencyCode(jsonObject.getString("currencyCode"));
                currencyModel.setCountryCode(jsonObject.getString("countryCode"));
                currencyModel.setFlagUrl(jsonObject.getString("flagUrl"));
                mCurrencyList.add(currencyModel);
            }

            return  mCurrencyList;

        } catch (Exception e) {
            return  mCurrencyList;
        }
    }

}
