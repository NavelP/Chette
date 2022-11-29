package com.nvdeveloper.Chette;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.nvdeveloper.Chette.user.MainActivityUser;

public class InternetConnectionStatus extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );
            if(noConnectivity){
                MainActivityUser.INTERNET_CONNECTION_STATUS = "DISCONNECTED";

            }else{
                MainActivityUser.INTERNET_CONNECTION_STATUS = "CONNECTED";

            }
        }
    }
}
