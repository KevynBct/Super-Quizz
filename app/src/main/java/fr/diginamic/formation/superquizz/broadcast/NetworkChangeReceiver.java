package fr.diginamic.formation.superquizz.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkChangeReceiver  extends BroadcastReceiver {

    public static final String NETWORK_CHANGE_ACTION = "com.androiderstack.broadcastreceiverdemo.NetworkChangeReceiver";
    public static final String NETWORK_STATUS = "networkStatus";

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (isOnline(context))
        {
            sendInternalBroadcast(context, true);
        }
        else
        {
            sendInternalBroadcast(context, false);
        }
    }

    private void sendInternalBroadcast(Context context, Boolean networkStatus)
    {
        try
        {
            Intent intent = new Intent();
            intent.putExtra(NETWORK_STATUS, networkStatus);
            intent.setAction(NETWORK_CHANGE_ACTION);
            context.sendBroadcast(intent);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean isOnline(Context context)
    {
        boolean isOnline = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            isOnline = (netInfo != null && netInfo.isConnected());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return isOnline;
    }
}