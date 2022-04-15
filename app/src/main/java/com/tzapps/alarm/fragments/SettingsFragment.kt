package com.tzapps.alarm.fragments

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.tzapps.alarm.R
import com.tzapps.alarm.utils.Prefs

class SettingsFragment: PreferenceFragmentCompat() {

    companion object {
        private var f: SettingsFragment? = null
        fun getInstance() : SettingsFragment {
            if (f==null)
                f= SettingsFragment()
            return f!!
        }
    }

    private lateinit var snoozePreference: ListPreference
    private lateinit var timeoutPreference: ListPreference
    private lateinit var crescendoPreference: ListPreference
    private lateinit var ringtonePreference: Preference
    private lateinit var nightModePreference: SwitchPreferenceCompat
    private lateinit var vibrationPreference: SwitchPreferenceCompat

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_settings,rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nightModePreference = findPreference("nightMode")!!
        snoozePreference = findPreference("snoozeLength")!!
        timeoutPreference = findPreference("silenceTimeout")!!
        crescendoPreference = findPreference("crescendoTime")!!
        ringtonePreference = findPreference("ringtone")!!
        vibrationPreference = findPreference("vibrateEnabled")!!
        nightModePreference.setOnPreferenceChangeListener { _, newValue ->
            Prefs.setNightMode(nightModePreference.isChecked)
            AppCompatDelegate.setDefaultNightMode(if(nightModePreference.isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            false
        }
        vibrationPreference.setOnPreferenceChangeListener { preference, newValue ->
            Prefs.setVibrationEnabled(vibrationPreference.isChecked)
            false
        }
        snoozePreference.setOnPreferenceChangeListener { preference, newValue ->
            Prefs.setSnoozeTime(snoozePreference.value.toInt())
            false
        }
        timeoutPreference.setOnPreferenceChangeListener { preference, newValue ->
            Prefs.setTimeout(timeoutPreference.value.toInt())
            false
        }
        crescendoPreference.setOnPreferenceChangeListener { preference, newValue ->
            Prefs.setCrescendo(crescendoPreference.value.toInt())
            false
        }
        ringtonePreference.setOnPreferenceClickListener {
            val ringtoneResult = requireActivity().registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {res: ActivityResult->
                if (res.resultCode==Activity.RESULT_OK) {
                    val ringtoneUri = res.data?.getParcelableExtra<Uri?>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI) ?: return@registerForActivityResult
                    Prefs.setRingtoneUri(ringtoneUri.toString())
                }
            }
            ringtoneResult.launch(Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
                putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALARM)
                putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT,true)
                putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT,false)
                putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,Prefs.getRingtoneUri())
            })
            true
        }
    }


}