package com.example.reminderapp.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminderapp.R;
import com.example.reminderapp.database.ReminderDBHelper;
import com.example.reminderapp.models.Reminder;
import com.example.reminderapp.receivers.ReminderReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * MainActivity — home screen showing the scrollable reminder list.
 * Renamed from 'MainPage' in original APK to match README spec.
 * FAB opens AddReminderActivity (extracted from inline dialog in original APK).
 */
public class MainActivity extends AppCompatActivity {

    private ReminderDBHelper dbHelper;
    private ReminderAdapter  adapter;
    private List<Reminder>   reminderList;
    private TextView         tvEmpty;

    private final ActivityResultLauncher<Intent> addReminderLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadReminders();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ReminderDBHelper(this);
        tvEmpty  = findViewById(R.id.tv_empty);

        RecyclerView recyclerView = findViewById(R.id.recycler_reminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reminderList = dbHelper.getAllReminders();
        adapter = new ReminderAdapter(reminderList, (reminder, position) -> {
            // Cancel scheduled alarm before deleting
            cancelAlarm(reminder.getId());
            dbHelper.deleteReminder(reminder.getId());
            reminderList.remove(position);
            adapter.notifyItemRemoved(position);
            updateEmptyState();
        });
        recyclerView.setAdapter(adapter);
        updateEmptyState();

        FloatingActionButton fab = findViewById(R.id.fab_add_reminder);
        fab.setOnClickListener(v ->
                addReminderLauncher.launch(new Intent(this, AddReminderActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReminders();
    }

    private void loadReminders() {
        reminderList.clear();
        reminderList.addAll(dbHelper.getAllReminders());
        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        tvEmpty.setVisibility(reminderList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void cancelAlarm(int reminderId) {
        Intent intent = new Intent(this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, reminderId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
