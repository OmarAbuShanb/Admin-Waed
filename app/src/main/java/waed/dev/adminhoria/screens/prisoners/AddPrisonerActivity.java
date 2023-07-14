package waed.dev.adminhoria.screens.prisoners;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import waed.dev.adminhoria.R;
import waed.dev.adminhoria.databinding.ActivityAddPrisonerBinding;

public class AddPrisonerActivity extends AppCompatActivity {
    private ActivityAddPrisonerBinding binding;

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPrisonerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupListeners();
    }

    private void setupListeners() {
        binding.selectArrestedDate.setOnClickListener(view -> showDatePickerDialog());
    }

    public void showDatePickerDialog() {
        calendar = Calendar.getInstance();
        DatePickerDialog pickerDialog = DatePickerDialog.newInstance((view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(year, monthOfYear, dayOfMonth);
            binding.selectArrestedDate.setText(toStringSimpleDate(calendar.getTime()));
            binding.selectArrestedDate.setTextColor(getResources().getColor(R.color.black));
        }, calendar);
        pickerDialog.setAccentColor(getResources().getColor(R.color.app_color));
        pickerDialog.setMaxDate(calendar);
        calendar.set(Calendar.YEAR, 1950);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        pickerDialog.setMinDate(calendar);
        pickerDialog.show(getSupportFragmentManager(), "");
    }

    public static String toStringSimpleDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return dateFormat.format(date);
    }

}