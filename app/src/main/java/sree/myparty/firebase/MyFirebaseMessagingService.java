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
import android.widget.Switch;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Map;
import java.util.Random;

import sree.myparty.R;
import sree.myparty.RegistartionActivity;
import sree.myparty.chat.ParticularChat;
import sree.myparty.chat.UserListActicity;
import sree.myparty.chat.VideoCallActivity;
import sree.myparty.misc.NewsList;

/**
 * Created by srikanthk on 6/29/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        if (remoteMessage.getData() != null) {
            Map<String, String> data = remoteMessage.getData();
            if (data.get("purpose") != null) {
                String task = data.get("purpose");
                notifyTask(task, data);
            }
        }


    }

    private void notifyTask(String task, Map<String, String> data) {
        Intent intent = null;
        switch (task) {

            case "News":

                //new post
                intent = new Intent(getApplicationContext(), NewsList.class);
                showNotification(getApplicationContext(), data.get("news"), intent, data.get("username"));

                break;

            case "Chat":
                //chat message
                intent = new Intent(getApplicationContext(), UserListActicity.class);
                showNotification(getApplicationContext(), data.get("message"), intent, data.get("username"));
                break;

            case "Incoming Video Call":
                showCallNotification(getApplicationContext(), data.get("message"), new Intent(getApplicationContext(), VideoCallActivity.class), data.get("purpose"));

                break;

            default:
                //not et decide
                intent = new Intent(getApplicationContext(), RegistartionActivity.class);
                showNotification(getApplicationContext(), "", intent, "");
                break;
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

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
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
