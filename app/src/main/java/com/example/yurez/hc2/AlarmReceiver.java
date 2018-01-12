package com.example.yurez.hc2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class AlarmReceiver extends BroadcastReceiver
{
    final public static String TAG_ALARM_TIME = "tag_alarm_time";
    //Long msTime;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    public AlarmReceiver(Context context)
    {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public AlarmReceiver()
    {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TAG_WL");
        wakeLock.acquire(60 * 1000L /*1 minute*/);


//        Intent notificationIntent = new Intent(context, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//                notificationIntent, 0);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "chanel0");
//        builder.setContentIntent(contentIntent)
//                .setSmallIcon(R.drawable.ic_pill_notify)
//                .setContentTitle(medInfo.name)
//                .setContentText(medInfo.getDoseTyped)
//                .setShowWhen(true)
//                .setWhen(msTime) // can remove
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setPriority(Notification.PRIORITY_MAX);
//
//                .setAutoCancel(true);
//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(reqCode, builder.build());

//        Notification notification = new Notification
        //msTime = intent.getLongExtra(TAG_ALARM_TIME, 0);


        wakeLock.release();
    }

    public void setAlarm(Context context, Long msTime, int timerID)
    {
        //AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, timerID, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, msTime, pendingIntent);
    }

    public void cancelAlarm(Context context, int timerID)
    {
        Intent intent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, timerID, intent, 0);
        //AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }


    interface onNotifyReceived
    {
        void onAlarm(Integer indexTodayMeds);
    }
}
