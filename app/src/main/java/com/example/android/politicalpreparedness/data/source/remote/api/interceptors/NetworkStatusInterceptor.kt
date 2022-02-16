package com.example.android.politicalpreparedness.data.source.remote.api.interceptors

import android.content.Context
import com.example.android.politicalpreparedness.util.NetworkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class NetworkStatusInterceptor
@Inject
constructor(@ApplicationContext private val context: Context) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    if (NetworkManager.isOnline(context)) {
      return chain.proceed(chain.request())
    } else {
      throw NoConnectivityException()
    }
  }
}

class NoConnectivityException : IOException() {
  override val message: String
    get() = "No Internet Connection"
}
