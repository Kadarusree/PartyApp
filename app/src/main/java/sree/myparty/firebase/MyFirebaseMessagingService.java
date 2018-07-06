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
import com.sinch.android.rtc.NotificationResult;
import com.sinch.android.rtc.SinchHelpers;

import java.util.Random;

import sree.myparty.R;
import sree.myparty.RegistartionActivity;
import sree.myparty.videocalling.SinchService;

/**
 * Created by srikanthk on 6/29/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService implements ServiceConnection{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);




        if (remoteMessage.getData().get("key").equalsIgnoreCase("Sinch")) {
            connectToService();
        } else {
            showNotification(getApplicationContext(), remoteMessage.getData().get("key"), new Intent());

        }

    }

    public void showNotification(Context context, String body, Intent intent) {
        String channelId = "a3";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_logo)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                        .setContentText(body)
                        .setAutoCancel(false)
                        .setSound(defaultSoundUri)
                        .setContentIntent(PendingIntent.getActivity(this,
                                0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// Since android Oreo notification channel is needed.
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

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
       /* if (mIntent == null) {
            return;
        }

        if (SinchHelpers.isSinchPushIntent(mIntent)) {
            SinchService.SinchServiceInterface sinchService = (SinchService.SinchServiceInterface) service;
            if (sinchService != null) {
                NotificationResult result = sinchService.relayRemotePushNotificationPayload(mIntent);
            }
        }*/

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    private void connectToService() {
        getApplicationContext().bindService(new Intent(this, SinchService.class), this, Context.BIND_AUTO_CREATE);
    }
}
