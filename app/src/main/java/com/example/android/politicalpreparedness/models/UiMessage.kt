package com.example.android.politicalpreparedness.models

import android.content.Context
import androidx.annotation.StringRes
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.source.remote.api.interceptors.NoConnectivityException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

sealed class UiMessage(open val id: Long) {
    data class StringMessage(
        override val id: Long = UUID.randomUUID().mostSignificantBits,
        val message: String
    ) : UiMessage(id)

    data class ResourceMessage(
        override val id: Long = UUID.randomUUID().mostSignificantBits,
        @StringRes val message: Int
    ) : UiMessage(id)
}

fun UiMessage.resolveMessage(context: Context): String {
    return when (this) {
        is UiMessage.StringMessage -> message
        is UiMessage.ResourceMessage -> context.getString(message)
    }
}

fun UiMessage(
    t: Throwable,
    id: Long = UUID.randomUUID().mostSignificantBits,
): UiMessage {
    return when (t) {
        is NoConnectivityException -> {
            UiMessage.ResourceMessage(id = id, message = R.string.error_internet_connection_short)
        }
        is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
            UiMessage.ResourceMessage(id = id, message = R.string.error_internet_connection)
        }
        else -> {
            if (BuildConfig.DEBUG) {
                UiMessage.ResourceMessage(
                    message = R.string.error_unknown,
                    id = id,
                )
//                UiMessage.StringMessage(
//                    message = t.message ?: "Error occurred: $t",
//                    id = id,
//                )
            } else {
                UiMessage.ResourceMessage(
                    message = R.string.error_unknown,
                    id = id,
                )
            }
        }
    }
}
