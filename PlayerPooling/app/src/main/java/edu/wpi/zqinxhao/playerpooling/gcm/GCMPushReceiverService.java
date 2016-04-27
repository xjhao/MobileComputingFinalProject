package edu.wpi.zqinxhao.playerpooling.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmListenerService;

import edu.wpi.zqinxhao.playerpooling.HostGameActivity;
import edu.wpi.zqinxhao.playerpooling.LoginActivity;
import edu.wpi.zqinxhao.playerpooling.R;
import edu.wpi.zqinxhao.playerpooling.LoginActivity;

/**
 * Created by xhao on 4/24/16.
 */
public class GCMPushReceiverService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        sendNotification(message);

    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, HostGameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0; //Your request code
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);

        //Setup notificaiton
        if (HostGameActivity.isFront) {
            Intent requestIntent = new Intent("suibian");
            requestIntent.setAction("REQUEST");
            requestIntent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(requestIntent);
        } else {
            //Build notification
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("My GCM message :X:X")
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noBuilder.build());

        }

    }
}
