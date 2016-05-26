package org.zackwatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    public static MainActivity instance = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, SendPebble.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        instance = this;
    }
    @Override
    protected void onPause() {
        super.onPause();
        instance = null;
    }

}

