package com.algorepublic.cityhistory.prayertimings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import java.io.IOException;

public class NetworkUtil {

    private static AlertDialog.Builder mAlertDialogBuilder = null;
    private static Dialog mDialog = null;

    public static boolean isInternetConnected(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {

                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    status = true;
                }
                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    status = true;
                }
                status = (activeNetwork != null &&
                        activeNetwork.isConnected());
            }
        } catch (Exception e) {
            status = false;
        }
        return status;
    }

    /**
     * @param context
     */
    public static void internetFailedDialog(final Context context) {
        try {
            if(mAlertDialogBuilder != null && mDialog != null && mDialog.isShowing()){
                mDialog.cancel();
                mDialog.dismiss();
                mDialog = null;
                mAlertDialogBuilder = null;
            }
            mAlertDialogBuilder = new AlertDialog.Builder(
                    context);
            mAlertDialogBuilder.setTitle("Internet Problem");
            mAlertDialogBuilder
                    .setMessage("Please check your internet connection and try again!");
            mAlertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
            mAlertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    dialog.dismiss();
                    dialog.cancel();
                    mAlertDialogBuilder = null;
                }
            });
            mDialog = mAlertDialogBuilder.create();
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static AlertDialog dialog;
    /**
     * @param act
     * @param message
     * @param
     */
    public static AlertDialog showStatusDialog(final Activity act, final String title, final String message, final boolean isToken) {

        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        act);
                alertDialogBuilder.setTitle(title);
                alertDialogBuilder.setMessage(message);
                alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialog.cancel();
                        if (isToken){
                            // in case you need to logout
                        }
                    }
                });
                try {
                    dialog = alertDialogBuilder.create();
                    dialog.show();
                } catch (Exception e){

                }

            }
        });
        return dialog;


    }

    /**
     * Checks if internet is connected/available by pinning google.com
     * @return
     * boolean i.e. true if internet is connected and false otherwise.
     */
    public static Boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;

    }

    /**
     *
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        try {
            final ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo mobile = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return mobile.isConnected();
        } catch (Exception e){
            return false;
        }

    }

    public static boolean isWifiConnected(Context context) {
        try {
            final ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo wifi = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return wifi.isConnected();
        } catch (Exception e){
            return false;
        }
    }
}