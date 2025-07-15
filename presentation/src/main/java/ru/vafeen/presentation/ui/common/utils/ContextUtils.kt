package ru.vafeen.presentation.ui.common.utils

import android.content.Context

fun Context.getVersionName(): String? =
    packageManager.getPackageInfo(packageName, 0).versionName