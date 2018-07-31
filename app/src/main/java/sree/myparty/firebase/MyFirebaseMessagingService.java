package sree.myparty.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Random;

import sree.myparty.R;
import sree.myparty.chat.ParticularChat;
import sree.myparty.chat.UserListActicity;
import sree.myparty.chat.VideoCallActivity;

/**
 * Created by srikanthk on 6/29/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);





        if (remoteMessage.getData() != null && remoteMessage.getData().get("message") != null) {
            if (remoteMessage.getData().get("username") != null) {
                Intent intent = new Intent(getApplicationContext(), UserListActicity.class);
                /*intent.putExtra("name",remoteMessage.getData().get("username"));
                intent.putExtra("key",remoteMessage.getData().get("key"));
                intent.putExtra("profile_pic",remoteMessage.getData().get("profile_pic"));
                intent.putExtra("fcm",remoteMessage.getData().get("fcmkey"));
                intent.putExtra("uid",remoteMessage.getData().get("uid"));*/


                showNotification(getApplicationContext(), remoteMessage.getData().get("message"), intent, remoteMessage.getData().get("username"));
            } else if (remoteMessage.getData().get("purpose") != null && (remoteMessage.getData().get("purpose").equalsIgnoreCase("Incoming Video Call"))) {
                showCallNotification(getApplicationContext(), remoteMessage.getData().get("message"), new Intent(getApplicationContext(), VideoCallActivity.class), remoteMessage.getData().get("purpose"));

            }

        } else {

            if (remoteMessage.getData().get("key").equalsIgnoreCase("Sinch")) {
                //connectToService();
            } else {
                showNotification(getApplicationContext(), remoteMessage.getData().get("key"), new Intent(), getResources().getString(R.string.app_name));
            }

        }
    }



    public void showNotification(Context context, String body, Intent intent, String username) {
        String channelId = "a3";
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_logo)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(username + ":" + body))
                        .setContentText(body)
                        .setAutoCancel(false)
                        .setSound(defaultSoundUri)

                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "a3",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        try {
            notificationManager.notify(getNumber(), notificationBuilder.build());
        } catch (Exception e) {

        }
    }

    public int getNumber() {
        Random random = new Random();
        return random.nextInt(9999);
    }


    public void showCallNotification(Context context, String body, Intent intent, String username) {
        String channelId = "a3";
        intent.putExtra("TOKENS", body);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_logo)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Incoming Call"))
                        .setContentText(body)
                        .setAutoCancel(false)
                        .setSound(defaultSoundUri)

                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "a3",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        try {
            notificationManager.notify(getNumber(), notificationBuilder.build());
        } catch (Exception e) {

        }
    }

}
