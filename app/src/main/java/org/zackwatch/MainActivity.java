package org.zackwatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    private TextView batteryTxt;
    private Handler mHandler = new Handler();
    private static final UUID APP_UUID = UUID.fromString("a10373ad-eca3-4666-80a3-f8069e2ae4eb");
    int level;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            batteryTxt.setText(String.valueOf(level) + "%");

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        batteryTxt = (TextView) this.findViewById(R.id.batteryTxt);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

