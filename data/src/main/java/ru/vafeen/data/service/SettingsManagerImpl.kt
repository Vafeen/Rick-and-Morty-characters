package ru.vafeen.data.service

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.vafeen.domain.model.Settings
import ru.vafeen.domain.service.SettingsManager
import javax.inject.Inject

/**
 * Implementation of [SettingsManager] that manages application settings using [SharedPreferences].
 * This class provides a reactive way to observe changes in settings via [StateFlow].
 *
 * @property sharedPreferences Instance of [SharedPreferences] used for storing and loading settings.
 */
internal class SettingsManagerImpl @Inject constructor(private val sharedPreferences: SharedPreferences) :
    SettingsManager {

    /**
     * The current settings loaded from SharedPreferences.
     */
    private var settings = sharedPreferences.getSettingsOrCreateIfNull()

    /**
     * Internal [MutableStateFlow] to track changes in settings.
     */
    private val _settingsFlow = MutableStateFlow(settings)

    /**
     * Public [StateFlow] to subscribe to settings changes.
     * Observers receive updates when the settings change.
     */
    override val settingsFlow: StateFlow<Settings> = _settingsFlow.asStateFlow()

    /**
     * Registers a listener to monitor changes in [SharedPreferences].
     * When a change is detected, the updated settings are emitted to the [StateFlow].
     */
    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            // Emit new settings when SharedPreferences are updated
            Log.d("sp", "callback")
            _settingsFlow.value = sharedPreferences.getSettingsOrCreateIfNull()
        }
    }

    /**
     * Saves the settings by applying a transformation function to the current settings.
     * The updated settings are stored in [SharedPreferences] and automatically emitted
     * to subscribers via the [StateFlow].
     *
     * @param saving A function that takes the current settings and returns the updated settings.
     */
    @Synchronized
    override fun save(saving: (Settings) -> Settings) {
        // Update settings in memory
        settings = saving(settings)
        // Save updated settings to SharedPreferences
        sharedPreferences.save(settings)
        // Update flow with new settings
        // Attention: i read data after writing because OnSharedPreferenceChangeListener work not always
        _settingsFlow.value = sharedPreferences.getSettingsOrCreateIfNull()
    }

    /**
     * Saves settings to SharedPreferences.
     *
     * @param settings The [Settings] object to be saved.
     */
    private fun SharedPreferences.save(settings: Settings) = saveInOrRemoveFromSharedPreferences {
        putString(SETTINGS_KEY, settings.toJsonString())
    }

    /**
     * Retrieves settings from SharedPreferences or creates new ones if none exist.
     * This function checks for the presence of settings in SharedPreferences and returns them.
     * If the settings are not found, new settings are created and saved.
     *
     * @return The [Settings] object retrieved from SharedPreferences or newly created.
     */
    private fun SharedPreferences.getSettingsOrCreateIfNull(): Settings {
        val settings = getFromSharedPreferences {
            getString(SETTINGS_KEY, "").let {
                if (it != "") Gson().fromJson(it, Settings::class.java)
                else null
            }
        }
        return if (settings != null) settings
        else {
            val newSettings = Settings() // Create default new settings.
            saveInOrRemoveFromSharedPreferences {
                putString(SETTINGS_KEY, newSettings.toJsonString())
            }
            newSettings
        }
    }

    /**
     * Extension function to save data into SharedPreferences using the provided save block.
     *
     * @param save A lambda extending [SharedPreferences.Editor] to perform saving operations.
     */
    fun SharedPreferences.saveInOrRemoveFromSharedPreferences(save: SharedPreferences.Editor.() -> Unit) {
        edit().apply {
            save()
            apply()
        }
    }

    /**
     * Extension function to retrieve data from SharedPreferences using the provided get block.
     *
     * @param get A lambda extending [SharedPreferences] to perform retrieval operations.
     * @return The retrieved data of type [T].
     */
    fun <T> SharedPreferences.getFromSharedPreferences(get: SharedPreferences.() -> T): T = get()

    /**
     * Converts [Settings] object to its JSON string representation.
     *
     * @return JSON string representing the settings.
     */
    fun Settings.toJsonString(): String = Gson().toJson(this)

    companion object {
        private const val SETTINGS_KEY = "SETTINGS_KEY"
        const val SHARED_PREFERENCES_NAME = "SHARED_PREFERENCES_NAME"
    }
}
