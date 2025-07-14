package ru.vafeen.data.network.service.impl

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import ru.vafeen.domain.network.ConnectivityChecker
import javax.inject.Inject

/**
 * Android implementation of network connectivity checking using system ConnectivityManager.
 * Provides real-time network availability status.
 *
 * @property connectivityManager Android system service for network state monitoring
 */
internal class AndroidConnectivityChecker @Inject constructor(
    private val connectivityManager: ConnectivityManager
) : ConnectivityChecker {

    /**
     * Checks current internet connectivity status by verifying:
     * 1. Active network existence
     * 2. INTERNET capability availability
     * 3. Valid network transport (WiFi, cellular, ethernet)
     *
     * @return Boolean indicating if internet connection is available and functional
     */
    override fun isInternetAvailable(): Boolean = connectivityManager.isInternetAvailable()

    /**
     * Extension function performing detailed network capability check
     */
    private fun ConnectivityManager.isInternetAvailable(): Boolean {
        return activeNetwork?.let { network ->
            getNetworkCapabilities(network)?.run {
                // Verify internet capability and valid transport
                hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && (
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                        )
            }
        } ?: false
    }
}