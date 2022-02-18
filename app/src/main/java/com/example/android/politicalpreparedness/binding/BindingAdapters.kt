package com.example.android.politicalpreparedness.binding

import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.databinding.BindingAdapter
import com.example.android.politicalpreparedness.models.UiMessage
import com.example.android.politicalpreparedness.models.resolveMessage


@BindingAdapter("visibleGone")
fun showHide(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}

@BindingAdapter("html")
fun html(view: TextView, htmlText: String?) {
    htmlText?.let {
        view.text = HtmlCompat.fromHtml(htmlText, FROM_HTML_MODE_LEGACY)
        view.movementMethod = LinkMovementMethod.getInstance()
    }
}

@BindingAdapter("uiMessage")
fun uiMessage(view: TextView, uiMessage: UiMessage?) {
    uiMessage?.let {
        view.text = uiMessage.resolveMessage(view.context)
    }
}

@BindingAdapter("isLoading")
fun isLoading(view: ImageView, isLoading: Boolean? = false) {
    val animatable = view.drawable as
            android.graphics.drawable.Animatable
    if (isLoading == true) {
        animatable.stop()
    } else {
        animatable.stop()
    }
}