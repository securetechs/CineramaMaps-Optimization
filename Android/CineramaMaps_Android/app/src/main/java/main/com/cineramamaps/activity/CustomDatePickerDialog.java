package main.com.cineramamaps.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;

import main.com.cineramamaps.R;
import main.com.cineramamaps.databinding.ActivityUpdateProfileBinding;

public class CustomDatePickerDialog extends AppCompatDialogFragment {

    private Context mContext;

    public CustomDatePickerDialog(Context context) {
        this.mContext = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Inflate the custom view for date selection
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View customView = inflater.inflate(R.layout.custom_date_picker, null);

        // Get references to the spinners
        Spinner yearSpinner = customView.findViewById(R.id.year_spinner);
        Spinner monthSpinner = customView.findViewById(R.id.month_spinner);
        Spinner daySpinner = customView.findViewById(R.id.day_spinner);

        // Populate Year Spinner
        ArrayList<String> years = new ArrayList<>();
        for (int i = currentYear - 45; i <= currentYear; i++) {
            years.add(String.valueOf(String.format("%02d", i)));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        yearSpinner.setSelection(years.indexOf(String.valueOf(currentYear)));

        // Populate Month Spinner
        ArrayList<String> months = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            months.add(String.valueOf(String.format("%02d", i + 1))); // Format month as "01", "02", etc.
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        monthSpinner.setSelection(currentMonth);

        // Populate Day Spinner
        ArrayList<String> days = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            days.add(String.valueOf(String.format("%02d", i))); // Format days as "01", "02", etc.
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);
        daySpinner.setSelection(currentDay - 1);

        // Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(customView)
                .setPositiveButton(""+getResources().getString(R.string.ok), (dialog, which) -> {
                    // Get selected date from spinners
                    String selectedYear = (String) yearSpinner.getSelectedItem();
                    String selectedMonth = (String) monthSpinner.getSelectedItem();
                    String selectedDay = (String) daySpinner.getSelectedItem();

                    // Display selected date as a Toast
                 //   Toast.makeText(mContext, "Selected Date: " + selectedYear + "-" + selectedMonth + "-" + selectedDay, Toast.LENGTH_SHORT).show();
                    if(UpdateProfileActivity.binding!=null && UpdateProfileActivity.binding.dobEt !=null ) {
                        UpdateProfileActivity.binding.dobEt.setText("" + selectedYear + "-" + selectedMonth + "-" + selectedDay);
                    }
                   if(CreateProfileActivity.binding!=null && CreateProfileActivity.binding.dobEt !=null ) {
                       CreateProfileActivity.binding.dobEt.setText("" + selectedYear + "-" + selectedMonth + "-" + selectedDay);
                   }
                })
                .setNegativeButton(""+getResources().getString(R.string.cancell), (dialog, which) -> dismiss());

        return builder.create();
    }
}
