package org.zackwatch;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
import android.widget.TextView;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.UUID;

public class SendPebble extends Service {

    private Handler mHandler = new Handler();
    private static final UUID APP_UUID = UUID.fromString("a10373ad-eca3-4666-80a3-f8069e2ae4eb");
    int level;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return Service.START_NOT_STICKY;
    }


    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
             if (MainActivity.instance != null) {
                 TextView batteryTxt = (TextView) MainActivity.instance.findViewById(R.id.batteryTxt);
                 String bp = String.valueOf(level) + "%";
                 batteryTxt.setText(bp);

             }

            boolean isConnected = PebbleKit.isWatchConnected(ctxt);
            if (isConnected) {
                //boolean appMessageSupported = PebbleKit.areAppMessagesSupported(this);
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // Send a time and distance to the sports app
                        PebbleDictionary outgoing = new PebbleDictionary();
                        outgoing.addString(Keys.KEY_BATT, String.valueOf(level));
                        PebbleKit.sendDataToPebble(getApplicationContext(), APP_UUID, outgoing);
                    }

                }, 1000L);
            }

        }
    };

    public SendPebble() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
