package ru.vafeen.domain.network

/**
 * Provides capability to check internet connectivity status.
 * Implementations should handle platform-specific connectivity detection mechanisms.
 */
interface ConnectivityChecker {

    /**
     * Checks whether the device currently has active internet connectivity.
     *
     * @return `true` if the device is connected to a network with internet access,
     *         `false` otherwise.
     *
     * @implNote This method should:
     * - Handle all network types (WiFi, cellular, ethernet)
     * - Verify actual internet access (not just network connection)
     * - Be safe to call from any thread
     */
    fun isInternetAvailable(): Boolean
}