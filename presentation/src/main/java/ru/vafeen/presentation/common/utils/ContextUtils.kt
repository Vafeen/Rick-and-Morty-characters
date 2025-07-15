package ru.vafeen.presentation.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import ru.vafeen.presentation.R

/**
 * Opens the given URL link in the default web browser.
 *
 * @receiver Context to use for starting the activity.
 * @param link The URL string to open.
 */
fun Context.openLink(link: String) {
    startActivity(Intent(Intent.ACTION_VIEW, link.toUri()))
}

/**
 * Opens the email client to send an email to the specified address.
 * Automatically populates the subject line with "BugReport <AppName>".
 *
 * @receiver Context to use for starting the activity.
 * @param email Email address to send the message to.
 */
fun Context.sendEmail(email: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data =
            "mailto:$email?subject=${Uri.encode("BugReport ${getString(R.string.app_name)}")}".toUri()
    }
    startActivity(intent)
}
