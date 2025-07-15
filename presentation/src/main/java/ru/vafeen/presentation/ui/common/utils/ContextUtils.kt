package ru.vafeen.presentation.ui.common.utils

import android.content.Context

/**
 * Retrieves the version name of the application from the package info.
 *
 * @receiver The [Context] to access package manager.
 * @return The app version name as defined in the app manifest, or null if unavailable.
 */
fun Context.getVersionName(): String? =
    packageManager.getPackageInfo(packageName, 0).versionName
