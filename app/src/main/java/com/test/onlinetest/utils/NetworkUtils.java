package com.test.onlinetest.utils;

import android.os.Build;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.net.NetworkCapabilities;
import android.net.Network;
import android.net.wifi.WifiNetworkSpecifier;

public class NetworkUtils {
    public static void bindNetworkToWifi(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return;
        }

        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new NetworkRequest.Builder();
            //set the transport type do WIFI
            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            connectivityManager.requestNetwork(builder.build(), new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //connectivityManager.bindProcessToNetwork(null);
                        connectivityManager.bindProcessToNetwork(network);
                    } else {
                        //This method was deprecated in API level 23
                        //ConnectivityManager.setProcessDefaultNetwork(null);
                        ConnectivityManager.setProcessDefaultNetwork(network);
                    }

                    connectivityManager.unregisterNetworkCallback(this);
                }
            });
        }
    }

    public static void bindNetworkToMobile(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return;
        }

        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new NetworkRequest.Builder();
            //set the transport type do WIFI
            builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
            connectivityManager.requestNetwork(builder.build(), new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        connectivityManager.bindProcessToNetwork(null);
                        connectivityManager.bindProcessToNetwork(network);
                    } else {
                        //This method was deprecated in API level 23
                        ConnectivityManager.setProcessDefaultNetwork(null);
                        ConnectivityManager.setProcessDefaultNetwork(network);
                    }

                    connectivityManager.unregisterNetworkCallback(this);
                }
            });
        }
    }

    //For Android Q and newer
    public static void connectAndBinding (Context context, final String ssid, final String password){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            final ConnectivityManager connectivityManager = (ConnectivityManager)
                    context.getSystemService( Context.CONNECTIVITY_SERVICE );

            ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                        connectivityManager.bindProcessToNetwork(network);   //Bind process to connected WIFI
                }

                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    connectivityManager.bindProcessToNetwork(null);
                    connectivityManager.unregisterNetworkCallback(this);
                }
            };

            WifiNetworkSpecifier specifier = new WifiNetworkSpecifier.Builder()
                    .setWpa2Passphrase( password )
                    .setSsid( ssid )
                    .build();

            final NetworkRequest request = new NetworkRequest.Builder()
                    .addTransportType( NetworkCapabilities.TRANSPORT_WIFI )
                    .setNetworkSpecifier( specifier )
                    .build();

            connectivityManager.requestNetwork( request, callback );
        }
    }
}