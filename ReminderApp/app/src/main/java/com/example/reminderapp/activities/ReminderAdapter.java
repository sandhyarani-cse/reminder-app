package com.example.reminderapp.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminderapp.R;
import com.example.reminderapp.models.Reminder;

import java.util.List;

/**
 * RecyclerView adapter for the reminder list displayed in MainActivity.
 */
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    public interface OnDeleteClickListener {
        void onDelete(Reminder reminder, int position);
    }

    private final List<Reminder> reminders;
    private final OnDeleteClickListener deleteListener;

    public ReminderAdapter(List<Reminder> reminders, OnDeleteClickListener deleteListener) {
        this.reminders      = reminders;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder r = reminders.get(position);
        holder.tvTitle.setText(r.getTitle());
        holder.tvDateTime.setText(r.getDate() + "  " + r.getTime());
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(r, position));
    }

    @Override
    public int getItemCount() { return reminders.size(); }

    static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView    tvTitle, tvDateTime;
        ImageButton btnDelete;

        ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle    = itemView.findViewById(R.id.tv_reminder_title);
            tvDateTime = itemView.findViewById(R.id.tv_reminder_datetime);
            btnDelete  = itemView.findViewById(R.id.btn_delete_reminder);
        }
    }
}
