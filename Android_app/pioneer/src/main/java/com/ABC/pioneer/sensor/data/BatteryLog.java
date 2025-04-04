package com.ABC.pioneer.sensor.data;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import com.ABC.pioneer.sensor.datatype.TimeInterval;
import java.text.SimpleDateFormat;
import java.util.Date;

// Andorid, CSV电池日志 ,用于事后分析和可视化
public class BatteryLog {
    private final static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static TimeInterval updateInterval = TimeInterval.seconds(30);
    private final Context context;
    private final TextFile textFile;

    // battery
    public BatteryLog(final Context context, final String filename) {
        this.context = context;
        textFile = new TextFile(context, filename);
        if (textFile.empty()) {
            textFile.write("time,source,level");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        update();
                    } catch (Throwable e) {
                    }
                    try {
                        Thread.sleep(updateInterval.millis());
                    } catch (Throwable e) {
                    }
                }
            }
        }).start();
    }
    
    // update 
    private void update() {
        final IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        final Intent batteryStatus = context.registerReceiver(null, intentFilter);

        if (batteryStatus == null) {
            return;
        }

        final int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        final boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
        final int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        final int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        final float batteryLevel = level * 100 / (float) scale;

        final String powerSource = (isCharging ? "external" : "battery");
        final String timestamp = dateFormatter.format(new Date());
        textFile.write(timestamp + "," + powerSource + "," + batteryLevel);
        System.out.println(timestamp + "," + powerSource + "," + batteryLevel);

    }
}
