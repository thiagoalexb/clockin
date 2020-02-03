package com.thiagoalexb.dev.clockin.broadcasts;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;


import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.thiagoalexb.dev.clockin.MainActivity;
import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.data.AppDatabase;
import com.thiagoalexb.dev.clockin.data.models.Schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = GeofenceBroadcastReceiver.class.getSimpleName();
    private CompositeDisposable mDisposable;

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, String.format("Error code : %d", geofencingEvent.getErrorCode()));
            return;
        }

        mDisposable = new CompositeDisposable();


        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        sendNotification(context, geofenceTransition);
    }


    private void sendNotification(Context context, int transitionType) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
        notificationManager.createNotificationChannel(notificationChannel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationChannel.getId());

        if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
            builder.setSmallIcon(R.drawable.ic_volume_off_white_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_volume_off_white_24dp))
                    .setContentTitle(context.getString(R.string.silent_mode_activated));

            saveEntrySchedule(context);
        } else if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
            builder.setSmallIcon(R.drawable.ic_volume_up_white_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_volume_up_white_24dp))
                    .setContentTitle(context.getString(R.string.back_to_normal));

            saveDepartureSchedule(context);
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        builder = builder
                .setContentIntent(pendingIntent)
                .setContentText(context.getString(R.string.touch_to_relaunch))
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        notificationManager.notify(0, builder.build());
    }

    private void saveEntrySchedule(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);

        Schedule scheduleDb = new Schedule();
        LocalDateTime now = LocalDateTime.now();
        scheduleDb.date = now;
        scheduleDb.entryTime = now;
        scheduleDb.day = now.getDayOfMonth();
        scheduleDb.month = now.getMonthValue();
        scheduleDb.year = now.getYear();

        mDisposable.add(db.scheduleDao().getByDay(scheduleDb.year, scheduleDb.month, scheduleDb.day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(schedule -> {
                }, throwable -> {
                    mDisposable.add(db.scheduleDao().insert(scheduleDb)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                mDisposable.clear();
                            }, throwable1 -> {
                                mDisposable.clear();
                            }));
                }));
    }

    private void saveDepartureSchedule(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        LocalDate now = LocalDate.now();

        mDisposable.add(db.scheduleDao().getByDay(now.getYear(), now.getMonthValue(), now.getDayOfMonth())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(schedule -> {
                    if(schedule.entryTime == null) return;
                    if(schedule.departureTime != null) return;
                    schedule.departureTime = LocalDateTime.now();
                    mDisposable.add(db.scheduleDao().update(schedule)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                mDisposable.clear();
                            }, throwable1 -> {
                                mDisposable.clear();
                            }));
                }, throwable -> {

                }));
    }

}

