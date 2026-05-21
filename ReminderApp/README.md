# ⏰ Reminder Application — Android

![Java](https://img.shields.io/badge/Java-Android-ED8B00)
![Android Studio](https://img.shields.io/badge/IDE-Android%20Studio-3DDC84)
![SQLite](https://img.shields.io/badge/Database-SQLite-003B57)
![Min SDK](https://img.shields.io/badge/Min%20SDK-21-brightgreen)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

> Mini Project — B.Eng. CSE, Guru Nanak Dev Engineering College, Bidar (VTU)

---

## 📌 Overview

An Android task-reminder application that helps users track important scheduled events — appointments, medicine intakes, bill payments, meetings — and delivers **push notifications** at the right time so nothing is missed.

---

## ✨ Features

- 📝 Create reminders with title, date, and time
- 🔔 Automatic push notifications via **AlarmManager + NotificationManager**
- 📋 View all reminders in a scrollable **RecyclerView** list
- 🗄️ Persistent local storage using **SQLite** (full CRUD via SQLiteOpenHelper)
- ➕ Add new reminders via a **FloatingActionButton**
- 👤 User registration and login
- 🗑️ Delete reminders after completion

---

## 🏗️ App Architecture

```
MainActivity (RecyclerView — reminder list)
      │
      ├── FloatingActionButton ──► AddReminderActivity
      │                               (Title + Date + Time picker)
      │                                        │
      │                                   SQLiteDatabase
      │                                   (ReminderDBHelper)
      │                                        │
      │                              AlarmManager.setExactAndAllowWhileIdle()
      │                                        │
      │                              ReminderReceiver (BroadcastReceiver)
      │                                        │
      │                              NotificationManager.notify()
      │
      └── LoginActivity ──► RegisterActivity
```

---

## 🧰 Tech Stack

| Component          | Technology                         |
|--------------------|------------------------------------|
| Language           | Java                               |
| IDE                | Android Studio                     |
| Database           | SQLite (via SQLiteOpenHelper)      |
| Scheduling         | AlarmManager                       |
| Notifications      | NotificationManager + PendingIntent |
| UI Components      | RecyclerView, FloatingActionButton |
| Min Android SDK    | API 21 (Android 5.0 Lollipop)      |

---

## 📁 Project Structure

```
ReminderApp/
│
├── app/src/main/
│   ├── java/com/example/reminderapp/
│   │   ├── activities/
│   │   │   ├── MainActivity.java         # Home — reminder list
│   │   │   ├── AddReminderActivity.java  # Add new reminder
│   │   │   ├── LoginActivity.java        # User login
│   │   │   ├── RegisterActivity.java     # User registration
│   │   │   └── ReminderAdapter.java      # RecyclerView adapter
│   │   │
│   │   ├── database/
│   │   │   └── ReminderDBHelper.java     # SQLite CRUD operations
│   │   │
│   │   ├── models/
│   │   │   └── Reminder.java             # Reminder data model
│   │   │
│   │   └── receivers/
│   │       └── ReminderReceiver.java     # BroadcastReceiver → notification
│   │
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml
│   │   │   ├── activity_add_reminder.xml
│   │   │   ├── activity_login.xml
│   │   │   ├── activity_register.xml
│   │   │   └── item_reminder.xml
│   │   ├── drawable/
│   │   │   ├── ic_notification.xml
│   │   │   └── ic_launcher_foreground.xml
│   │   └── values/
│   │       ├── strings.xml
│   │       ├── colors.xml
│   │       └── themes.xml
│   │
│   └── AndroidManifest.xml
│
├── build.gradle
├── settings.gradle
├── gradle.properties
└── README.md
```

---

## ⚙️ Setup & Installation

### Prerequisites
- Android Studio (Hedgehog 2023.1.1 or later)
- JDK 11+
- Android device or emulator (API 21+)

```bash
# 1. Clone or extract the project
git clone https://github.com/sandhyarani-cse/reminder-app.git
# OR unzip ReminderApp.zip

# 2. Open in Android Studio
#    File → Open → select the ReminderApp folder

# 3. Let Gradle sync complete (it will download dependencies)

# 4. Run on emulator or physical device
#    Run → Run 'app'
```

---

## 🔑 Key Android Components Used

| Component             | Purpose                                      |
|-----------------------|----------------------------------------------|
| `AlarmManager`        | Schedule reminder triggers at exact time     |
| `NotificationManager` | Display notification in status bar           |
| `PendingIntent`       | Carry notification action across processes   |
| `BroadcastReceiver`   | Receive alarm and fire notification          |
| `SQLiteOpenHelper`    | Manage local database creation & upgrades    |
| `RecyclerView`        | Efficiently display scrollable reminder list |
| `FloatingActionButton`| Quick access to add new reminder             |

---

## 📋 Required Permissions (AndroidManifest.xml)

```xml
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM"/>
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
```

---

## 🐛 Fixes vs Original APK

| # | Issue | Fix |
|---|-------|-----|
| 1 | `usename` typo in DB column | Fixed to `username` in `ReminderDBHelper` |
| 2 | `SCHEDULE_EXACT_ALARM` permission missing | Added to manifest |
| 3 | `POST_NOTIFICATIONS` permission missing | Added to manifest |
| 4 | `RECEIVE_BOOT_COMPLETED` permission missing | Added to manifest |
| 5 | `title` field missing from Reminder entity | Added `title` column |
| 6 | `AddReminderActivity` not a separate Activity | Extracted from inline dialog |
| 7 | Class names didn't match README | Renamed throughout |
| 8 | Original used Room instead of SQLiteOpenHelper | Rebuilt with `SQLiteOpenHelper` |

---

## 🔭 Future Improvements

- Voice input for adding reminders
- Location-based reminders
- Sync reminders across devices via cloud
- Recurring reminders (daily/weekly)

---

## 👩‍💻 Author

**Sandhya Rani**  
B.Eng. Computer Science & Engineering — VTU, Belagavi  
🔗 [LinkedIn](https://www.linkedin.com/in/sandhya-rani-digge-b1207b36a) | [GitHub](https://github.com/sandhyarani-cse)

---

## 📜 License
MIT License
