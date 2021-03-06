package com.thiagoalexb.dev.clockin.broadcasts;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.thiagoalexb.dev.clockin.MainActivity;
import com.thiagoalexb.dev.clockin.R;
import com.thiagoalexb.dev.clockin.service.ScheduleService;

import javax.inject.Inject;

import dagger.android.DaggerBroadcastReceiver;
import io.reactivex.disposables.CompositeDisposable;

public class GeofenceBroadcastReceiver extends DaggerBroadcastReceiver {

    public static final String TAG = GeofenceBroadcastReceiver.class.getSimpleName();
    private CompositeDisposable mDisposable;

    @Inject
    ScheduleService scheduleService;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
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
        NotificationChannel notificationChannel = new NotificationChannel(context.getString(R.string.id_channel), context.getString(R.string.name_channel), importance);
        notificationManager.createNotificationChannel(notificationChannel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationChannel.getId());

        if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
            builder.setSmallIcon(R.drawable.ic_entry)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_entry))
                    .setContentTitle(context.getString(R.string.entry_company));

            scheduleService.saveEntry();
        } else if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
            builder.setSmallIcon(R.drawable.ic_departure)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_departure))
                    .setContentTitle(context.getString(R.string.departure_company));

            scheduleService.saveDeparture();
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
}

