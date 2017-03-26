package com.example.asus.makanyab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnBoot extends BroadcastReceiver {
    public OnBoot() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
            context.startService(new Intent(context,MainService.class));
        }
    }
}
