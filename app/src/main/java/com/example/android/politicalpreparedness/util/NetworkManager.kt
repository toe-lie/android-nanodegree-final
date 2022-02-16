package com.example.android.politicalpreparedness.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build

object NetworkManager {

  fun isOnline(context: Context): Boolean {
    return (context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as
        ConnectivityManager)
      .run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          getNetworkCapabilities(activeNetwork)?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
              hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
              hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
              hasTransport(NetworkCapabilities.TRANSPORT_VPN)
          }
            ?: false
        } else {
          val activeNetwork: NetworkInfo? = activeNetworkInfo
          activeNetwork?.isConnectedOrConnecting == true
        }
      }
  }
}
