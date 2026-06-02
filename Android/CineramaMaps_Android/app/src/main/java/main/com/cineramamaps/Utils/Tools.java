package main.com.cineramamaps.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import main.com.cineramamaps.R;
import main.com.cineramamaps.Session.SessionManager;
import main.com.cineramamaps.interfaces.onDateSetListener;


public class Tools {
    public static Tools get() {
        return new Tools();
    }
    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "getting address...";
        if (context != null) {
            Geocoder geocoder = new Geocoder(context.getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
                if (addresses != null) {
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder("");

                    for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    }
                    strAdd = strReturnedAddress.toString();
                    Log.w("My Current address", strReturnedAddress.toString());
                } else {
                    strAdd = "No Address Found";
                    Log.w("My Current address", "No Address returned!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                strAdd = "Cant get Address";
                Log.w("My Current address", "Canont get Address!");
            }
        }
        return strAdd;
    }
    public static String getTimeZone(){
        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        String  time_zone = tz.getID();
        return time_zone;
    }
    public static String getDayName(){
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String dayname = outFormat.format(new Date());

        return dayname;
    }

    public static String getDayNameByDate(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Date mDate = null;
        try {
            mDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dayname=dayFormat.format(mDate.getTime());
        System.out.println("parseDay=====>"+dayname);

        return dayname;
    }


    public static String getDate(){
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dayname = outFormat.format(new Date());

        return dayname;
    }
    public static String getDateTime(){
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dayname = outFormat.format(new Date());

        return dayname;
    }
    public static String saveToInternalStorageMain(Bitmap bitmapImage,Activity activity) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        ContextWrapper cw = new ContextWrapper(activity);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile_"+dateToStr+".JPEG");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    public static void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        // ImagePath = saveToInternalStorage(bitmap);
        // Log.e("DECODE PATH PROF", "ff " + ImagePath);
        //lay_img.setImageBitmap(bitmap);



    }

    public void decodeFiles(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        //  ImagePath = saveToInternalStorage(bitmap);
        //  Log.e("DECODE PATH IDT", "ff " + ImagePath);
      //  lay_img.setImageBitmap(bitmap);

    }


    public static String getPath(Uri uri,Context context) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        if (cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //  Log.e("image_path.===..", "" + path);
        }
        cursor.close();
        return path;
    }
    public static String saveToInternalStorage(Bitmap bitmapImage,Context context) {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile_"+dateToStr+".JPEG");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static String ChangeDateFormat(String format, String date) {
        SimpleDateFormat old_format = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        SimpleDateFormat new_format = new SimpleDateFormat(format,Locale.US);
        Date newDate = null;
        try {
            newDate = old_format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new_format.format(newDate);
    }
    public static String ChangeDateTimeFormat(String date) {
        SimpleDateFormat old_format = new SimpleDateFormat("yyyy-MM-dd hh:mm a",Locale.US);
        SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
        Date newDate = null;
        try {
            newDate = old_format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new_format.format(newDate);
    }
    public static String ChangeDateTimeFormat2(String date) {
        SimpleDateFormat old_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
        SimpleDateFormat new_format = new SimpleDateFormat("hh:mm a",Locale.US);
        Date newDate = null;
        try {
            newDate = old_format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new_format.format(newDate);
    }public static String ChangeTimeFormat2(String date) {
        SimpleDateFormat old_format = new SimpleDateFormat("hh:mm a",Locale.US);
        SimpleDateFormat new_format = new SimpleDateFormat("hh:mm:ss",Locale.US);
        Date newDate = null;
        try {
            newDate = old_format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new_format.format(newDate);
    }

    public static void DatePicker(Context context, final onDateSetListener listener) {
        final Calendar myCalendar = Calendar.getInstance(Locale.US);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; // your format
              //  String myFormat = "MM-dd-yyyy"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                listener.SelectedDate(sdf.format(myCalendar.getTime()));
            }

        };
        DatePickerDialog dp = new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dp.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dp.show();

    }
    public static void BirhtDatePicker(Context context, final onDateSetListener listener) {
        final Calendar myCalendar = Calendar.getInstance(Locale.US);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                listener.SelectedDate(sdf.format(myCalendar.getTime()));
            }

        };
        DatePickerDialog dp = new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dp.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
       // dp.getDatePicker().set(context.getResources().getColor(R.color.colorPrimary));
        dp.show();

    }

    public static void MakeCall(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phone, null));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(intent);
    }
    public enum Type{
        DATE,TIME
    }
    public  String getCurrent(Type type){
        String cd="";
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat cf = new SimpleDateFormat("HH:mm a");
        cd=type== Type.DATE?df.format(c):cf.format(c);
        return cd;
    }
    public  String getCurrentDate(){
        String cd="";
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEEE, MMMM yyyy");
        cd= df.format(c);
        return cd;
    }
    public  String getDate(int i){
        String cd="";
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,i);
        SimpleDateFormat df = new SimpleDateFormat("EEEE, MMMM yyyy");
        cd= df.format(c.getTime());
        return cd;
    }
    public  String addDate(String date,int i){
        String dateInString = date;  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, i);
        sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        Date resultdate = new Date(c.getTimeInMillis());
        dateInString = sdf.format(resultdate);
        return dateInString;
    }

    public static void HideKeyboard(Context context, View view){
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public static String getTimeAgo(String crdate) {
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        String currenttime = dateFormat.format(cal.getTime());

        Date CreatedAt = null;
        Date current = null;
        try {
            CreatedAt = dateFormat.parse(crdate);
            current = dateFormat.parse(currenttime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Get msec from each, and subtract.
        long diff = current.getTime() - CreatedAt.getTime();
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String time = null;
        if (diffDays > 0) {
            if (diffDays == 1) {
                time = diffDays + " day ago ";
            } else {
                time = diffDays + " days ago ";
            }
        } else {
            if (diffHours > 0) {
                if (diffHours == 1) {
                    time = diffHours + " hr ago";
                } else {
                    time = diffHours + " hrs ago";
                }
            } else {
                if (diffMinutes > 0) {
                    if (diffMinutes == 1) {
                        time = diffMinutes + " min ago";
                    } else {
                        time = diffMinutes + " mins ago";
                    }
                } else {
                    if (diffSeconds > 0) {
                        time = diffSeconds + " secs ago";
                    }
                }

            }

        }
        return time;
    }

    public  static  void TimePicker(Context context, final onDateSetListener listene){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                int hours = selectedHour % 12;
                listene.SelectedDate(String.format("%02d:%02d %s", hours == 0 ? 12 : hours,
                        selectedMinute, selectedHour < 12 ? "am" : "pm"));

              //  listene.SelectedDate(selectedHour+":"+selectedMinute);
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Set time");
        mTimePicker.show();
    }
    public  static  void TimePicker(Context context, final onDateSetListener listene, final boolean is12hour, final boolean previous_time){
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
        Calendar mcurrentTime = Calendar.getInstance(Locale.US);
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (!previous_time) {
                    if (selectedHour>hour){
                        int hours = selectedHour % 12;
                        listene.SelectedDate(String.format("%02d:%02d %s", hours == 0 ? 12 : hours,
                                selectedMinute, selectedHour < 12 ? "am" : "pm"));
                    }else if (selectedHour==hour&&selectedMinute>=minute){
                        int hours = selectedHour % 12;
                        listene.SelectedDate(String.format("%02d:%02d %s", hours == 0 ? 12 : hours,
                                selectedMinute, selectedHour < 12 ? "am" : "pm"));
                    }else {
                        listene.SelectedDate(String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                minute, hour < 12 ? "am" : "pm"));
                    }
                }else {
                    if (!is12hour) {
                        int hours = selectedHour % 12;
                        listene.SelectedDate(String.format("%02d:%02d %s", hours == 0 ? 12 : hours,
                                selectedMinute, selectedHour < 12 ? "am" : "pm"));
                    }else {
                        listene.SelectedDate(String.format("%02d:%02d",selectedHour,selectedMinute));
                    }
                }
            }
        }, hour, minute, is12hour);
        mTimePicker.setTitle("Set arrival time");

        mTimePicker.show();
    }
    public long  getTimeInMillSec(String givenDateString){
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm",Locale.US);
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            Date mDate = null;
            try {
                mDate = sdf2.parse(givenDateString);
                System.out.println("Date in milli :: " + timeInMilliseconds);
            } catch (ParseException es) {
                es.printStackTrace();
            }
            timeInMilliseconds = mDate.getTime();
        }
        return timeInMilliseconds;
    }

    public long  getTimeInMillSec(String format,String givenDateString){
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    public String getMilliToTime(long millis){
        String cd="";
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a",Locale.US);
        cd= df.format(millis);
     return cd;
    }
    public int getDayNumber(String date){


        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dayOfTheWeek = (String) DateFormat.format("EEEE", date1); // Thursday
        String day = (String) DateFormat.format("dd", date1); // 20
        int d = Integer.parseInt(day);

     return d;
    }
    public int getMonthNumber(String date){


        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String monthString = (String) DateFormat.format("MMM", date1); // Jun
        String monthNumber = (String) DateFormat.format("MM", date1);
        int month = Integer.parseInt(monthNumber);

     return month;
    }
    public int getYearNumber(String date){


        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String year = (String) DateFormat.format("yyyy", date1);
        int year1 = Integer.parseInt(year);

     return year1;
    }



    public String ChangeTimeFormat(String givenDateString){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat new_for = new SimpleDateFormat("HH:mm a");
        try {
            Date mDate = sdf.parse(givenDateString);
            return new_for.format(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    public void ShareApp(Context context){
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Dahla");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + "BuildConfig.APPLICATION_ID" +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, "shareMessage");
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }
    public void LaunchMarket(Context context) {
        Uri uri = Uri.parse("market://details?id=" + "BuildConfig.APPLICATION_ID");
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }
    public void isLocationEnabled(final Context mContext, LocationManager locationManager) {
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
    }
    public void OpenGallery(Activity context, int code){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context.startActivityForResult(galleryIntent, code);
    }
    public ArrayList<String>getYear(){
        ArrayList<String>list=new ArrayList<>();
        for (int i=1990;i<2020;i++){
            list.add(""+i);
        }
        return list;
    }
    public void updateResources(Context context) {
        String lng= SessionManager.get(context).getSelectedLanguage();
        Locale locale = new Locale(lng);
        if (lng.equalsIgnoreCase("fr")){
            locale = new Locale(lng);
        }
        else {
            locale = new Locale("en");
        }
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());


    }
    public void updateView(Context context,View view) {
        String lng=SessionManager.get(context).getSelectedLanguage();
        if (lng.equals("ar")){
            view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }
    public int getDay(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Date mDate = null;
        try {
            mDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String day=dayFormat.format(mDate.getTime());
        System.out.println("parseDay=====>"+day);
        switch(day){
            case "Sunday":
                return 0;
            case "Monday":
                return 1;
            case "Tuesday":
                return 2;
            case "Wednesday":
                return 3;
            case "Thursday":
                return 4;
            case "Friday":
                return  5;
            case "Saturday":
                return 6;
        }
        return 0;

    }
    public void OpenTwitter(Context context){
        Intent intent = null;
        try {
            // get the Twitter app if possible
            context.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=@layanclinic"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/layanclinic?s=09"));
        }
        context.startActivity(intent);
    }
    public Intent getOpenFacebookIntent(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/layan.clinc"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/layan.clinc"));
        }
    }
    public void OpenInstagram(Context context){
        Uri uri = Uri.parse("http://instagram.com/layan.clinic");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.instagram.android");
        try {
            context.startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/layan.clinic/?igshid=12xo1zjxt2vwv")));
        }


    }
    public void OpenWebPage(Context context){
        String url = "http://www.rlm.com.sa";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }
    public void OpenWhatsApp(Context context,String contact){
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void OpenGmail(Context context){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "contac-us@rlm.com.sa", null));
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my subject text");
        context.startActivity(Intent.createChooser(emailIntent, null));
    }
    public  String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return removePrefix(new String(chars));
    }
    public String removePrefix(String number){
        if (number.startsWith("0")){
            return removePrefix(number.substring(1));
        }
        return number;
    }
    }
