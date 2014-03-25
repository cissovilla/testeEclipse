package com.villa.festasufba;




import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


// The class has to extend the BroadcastReceiver to get the notification from the system
public class TimeAlarm extends BroadcastReceiver {
public String nota,subNota;



@Override
 public void onReceive(Context context, Intent paramIntent ) {
	nota=paramIntent.getStringExtra("nota");
	subNota=paramIntent.getStringExtra("subNota");

 // Request the notification manager
 NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

 // Create a new intent which will be fired if you click on the notification
 //Intent intent = new Intent("android.intent.action.VIEW");
 //intent.setData(Uri.parse("http://www.papers.ch"));
 Intent intent = new Intent(context,MainActivity.class);

 // Attach the intent to a pending intent
 PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

 // Create the notification
 Notification notification = new Notification(R.drawable.nota, nota, System.currentTimeMillis());
 notification.setLatestEventInfo(context, nota, subNota ,pendingIntent);
 long[] vibrate = { 0, 100, 200, 300 };
 notification.vibrate = vibrate;
// notification.defaults |= Notification.DEFAULT_SOUND;
 //notification.defaults |= Notification.DEFAULT_VIBRATE;
 notification.defaults = Notification.DEFAULT_ALL;
 // Fire the notification
 notificationManager.notify(1, notification);
 }


}
