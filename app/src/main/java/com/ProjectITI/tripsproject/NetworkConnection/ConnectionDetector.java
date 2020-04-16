package com.ProjectITI.tripsproject.NetworkConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;

import java.io.IOException;

/**
 * Created by khaled-pc on 10/28/2018.
 */

public class ConnectionDetector {

    private Network network;
    private Context _context;

    public ConnectionDetector(Context context) {
        this._context = context;
    }

    public boolean hasInternetConnection() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            network = connectivity.getActiveNetwork();
            capabilities = connectivity.getNetworkCapabilities(network);
            return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
   }

   else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            if (connectivity != null) {
                final String command = "ping -c 1 google.com";
                try {
                    return Runtime.getRuntime().exec(command).waitFor() == 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


}
