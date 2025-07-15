package ru.vafeen.presentation.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import ru.vafeen.presentation.R

fun Context.openLink(link: String) {
    startActivity(Intent(Intent.ACTION_VIEW, link.toUri()))
}

fun Context.sendEmail(email: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data =
            "mailto:$email?subject=${Uri.encode("BugReport ${getString(R.string.app_name)}")}".toUri()
    }
    startActivity(intent)
}