/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.romide.terminal.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.romide.terminal.BuildConfig;
import com.romide.terminal.R;
import com.romide.terminal.activity.Term;
import com.romide.terminal.compat.ServiceForegroundCompat;
import com.romide.terminal.emulatorview.TermSession;
import com.romide.terminal.session.SessionList;

public class TermService extends Service implements TermSession.FinishCallback {
    /* Parallels the value of START_STICKY on API Level >= 5 */
    private static final int RUNNING_NOTIFICATION = 1;
    private final IBinder mTSBinder = new TSBinder();
    private ServiceForegroundCompat compat;
    private SessionList mTermSessions;

    /* This should be @Override if building with API Level >=5 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("TermService", "TermService::onBind()");
        return mTSBinder;
    }

    @Override
    public void onCreate() {
        compat = new ServiceForegroundCompat(this);
        mTermSessions = new SessionList();

        /* Put the service in the foreground. */
        Intent notifyIntent = Term.createTermIntent(this);

        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = createNotification(pendingIntent);

        compat.startForeground(RUNNING_NOTIFICATION, notification);
    }

    private Notification createNotification(PendingIntent pendingIntent) {
        final String channelId = String.valueOf(RUNNING_NOTIFICATION);
        final String channelName = getText(R.string.application_terminal).toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(BuildConfig.APPLICATION_ID);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setShowBadge(true);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0});
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setOngoing(true);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_stat_service_notification_icon);
        builder.setWhen(System.currentTimeMillis());
        builder.setTicker(getText(R.string.service_notify_text));
        builder.setContentTitle(channelName);
        builder.setContentText(getText(R.string.service_notify_text));
        return builder.build();
    }

    @Override
    public void onDestroy() {
        compat.stopForeground(true);
        for (TermSession session : mTermSessions) {
            session.setFinishCallback(null);
            session.finish();
        }
        mTermSessions.clear();
    }

    public SessionList getSessions() {
        return mTermSessions;
    }

    @Override
    public void onSessionFinish(TermSession session) {
        mTermSessions.remove(session);
    }

    public class TSBinder extends Binder {
        public TermService getService() {
            return TermService.this;
        }
    }
}
