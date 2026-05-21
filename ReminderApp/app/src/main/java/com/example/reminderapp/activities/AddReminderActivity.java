package com.example.reminderapp.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reminderapp.R;
import com.example.reminderapp.database.ReminderDBHelper;
import com.example.reminderapp.models.Reminder;
import com.example.reminderapp.receivers.ReminderReceiver;

import java.util.Calendar;

/**
 * AddReminderActivity — add new reminder with title, date, and time picker.
 * Extracted from MainPage FAB dialog in original APK to a dedicated Activity
 * as specified in README architecture.
 */
public class AddReminderActivity extends AppCompatActivity {

    private EditText etTitle;
    private TextView tvDate, tvTime;
    private int selectedYear, selectedMonth, selectedDay;
    private int selectedHour, selectedMinute;
    private boolean dateSet = false, timeSet = false;
    private ReminderDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        dbHelper = new ReminderDBHelper(this);
        etTitle  = findViewById(R.id.et_reminder_title);
        tvDate   = findViewById(R.id.tv_selected_date);
        tvTime   = findViewById(R.id.tv_selected_time);

        Button btnPickDate = findViewById(R.id.btn_pick_date);
        Button btnPickTime = findViewById(R.id.btn_pick_time);
        Button btnAdd      = findViewById(R.id.btn_add_reminder);

        btnPickDate.setOnClickListener(v -> openDatePicker());
        btnPickTime.setOnClickListener(v -> openTimePicker());
        btnAdd.setOnClickListener(v -> saveReminder());
    }

    private void openDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            selectedYear  = year;
            selectedMonth = month;
            selectedDay   = day;
            dateSet = true;
            tvDate.setText(String.format("%02d/%02d/%04d", day, month + 1, year));
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void openTimePicker() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this, (view, hour, minute) -> {
            selectedHour   = hour;
            selectedMinute = minute;
            timeSet = true;
            tvTime.setText(String.format("%02d:%02d", hour, minute));
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void saveReminder() {
        String title = etTitle.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!dateSet) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!timeSet) {
            Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
            return;
        }

        String dateStr = tvDate.getText().toString();
        String timeStr = tvTime.getText().toString();

        Reminder reminder = new Reminder(title, dateStr, timeStr);
        long rowId = dbHelper.insertReminder(reminder);

        if (rowId != -1) {
            scheduleAlarm((int) rowId, title);
            Toast.makeText(this, "Reminder added!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Failed to save reminder", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Schedules an exact alarm via AlarmManager.
     * Uses setExactAndAllowWhileIdle for reliable delivery on Doze mode devices.
     */
    private void scheduleAlarm(int reminderId, String title) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("reminder_id", reminderId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, reminderId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent);
            } else {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent);
            }
        }
    }
}
