package com.example.myapplication.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.LocalPreferences
import com.example.myapplication.R
import com.example.myapplication.data.SettingsItem
import com.example.myapplication.adapter.AdapterParam
import kotlinx.android.synthetic.main.activity_parameter.*

@SuppressLint("WrongViewCast")
class ParameterActivity : AppCompatActivity() {
    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, ParameterActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parameter)

        // Paramétrage du bandeau de l'activité
        supportActionBar?.apply {
            setTitle(getString(R.string.parameter_up))
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // initialisation du recycler view de l'activité
        paramDevice.layoutManager = LinearLayoutManager(this)
        paramDevice.adapter = AdapterParam(arrayOf(
                // recycler menant vers les paramètres de l'app avec un appui
                SettingsItem(getString(R.string.parameter_low), R.drawable.ic_baseline_settings_24) {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.data = Uri.fromParts("package", this.packageName, null)
                    startActivity(intent)
                },
                // recycler menant vers les paramètres de localisation de l'app avec un appui
                SettingsItem(getString(R.string.informations), R.drawable.ic_baseline_location_on_24) {
                    val targetIntent = Intent().apply {
                        action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    }
                    startActivity(targetIntent);
                },
                // recycler menant vers l'emplacement de l'eseo via google maps avec un appui
                SettingsItem(getString(R.string.eseo_map), R.drawable.ic_baseline_map_24) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.coord_eseo))));
                },
                // recycler menant vers le site internet de l'eseo un appui
                SettingsItem(getString(R.string.eseo_website), R.drawable.eseo) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.eseo_url))));
                },
                // recycler menant un mail pré-édité avec un appui
                SettingsItem(getString(R.string.contact), R.drawable.ic_baseline_mail_outline_24) {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse(getString(R.string.mail_adr))
                    intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.mail_extra))
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject))
                    startActivity(intent)
                },
                // recycler effaçant l'historique de localisation de l'app avec un appui
                SettingsItem(getString(R.string.clear_history), R.drawable.ic_baseline_delete_outline_24) {
                    LocalPreferences.getInstance(this).clearHistory()
                    Toast.makeText(this, getString(R.string.history_cleared), Toast.LENGTH_SHORT).show()
                }
        )) { item ->
        }
    }

    // permet un retour vers l'activité précédente
    override fun onSupportNavigateUp(): Boolean {
        super.finish()
        return true
    }
}