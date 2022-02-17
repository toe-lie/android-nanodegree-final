package com.example.android.politicalpreparedness.binding

import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.databinding.BindingAdapter


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