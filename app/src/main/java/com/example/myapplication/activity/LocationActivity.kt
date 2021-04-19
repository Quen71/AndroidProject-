package com.example.myapplication.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.myapplication.data.LocalPreferences
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.*

class LocationActivity : AppCompatActivity() {

    // bind pour l'activité main
    private lateinit var binding: ActivityLocationBinding
    // string builder pour sauvegarder la localisation dans l'historique
    private var saveAddr = StringBuilder()

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, LocationActivity::class.java)
        }
        const val  PERMISSION_REQUEST_LOCATION = 9999
        private lateinit var fusedLocationClient : FusedLocationProviderClient
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // implémentation du binding
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // bouton qui permet une localisation avec un appui
        binding.gps.setOnClickListener {
            requestPermission()
        }

        // bouton de sauvegarde de la localisation dans l'historique
        binding.buttonHistory.setOnClickListener {
            LocalPreferences.getInstance(this).addToHistory(saveAddr.toString())
        }
    }

    // vérifie si la permission de localisation est acquérie
    private fun hasPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // Localisation et calcul de la distance avec l'eseo
    private fun geoCode(location: Location){
        val geocode = Geocoder(this, Locale.getDefault())
        val results = geocode.getFromLocation(location.latitude, location.longitude, 1)

        // emplacement de l'eseo
        val locationESEO = Location("ESEO")
        locationESEO.latitude = 47.493204943721736
        locationESEO.longitude = -0.5513548233241482

        if (results.isNotEmpty()) {
            val dist: Float = location.distanceTo(locationESEO)/1000
            // modifie le texte de l'activité avec l'addresse localisée
            binding.textLoc.text = results[0].getAddressLine(0)
            // modifie le texte de l'activité avec la distance de l'eseo
            binding.textDist.text = getString(R.string.bulma_speech_1) + dist.toString() + getString(R.string.bulma_speech_2)
            // sauvegarde de la localisation pour la shared preferences (ou historique)
            saveAddr.append(results[0].getAddressLine(0))
        }
    }

    // vérification et demande d'autorisation de localisation de l'app
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission obtenue, Nous continuons la suite de la logique.
                    getLocation()
                } else {
                    // TODO
                    requestPermission()
                    if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        // Permission obtenue, Nous continuons la suite de la logique.
                        getLocation()
                    }
                    else{

                    }
                }
                return
            }
        }
    }

    // demande la permission
    private fun requestPermission() {
        if (!hasPermission()) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION)
        } else {
            getLocation()
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (hasPermission()) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token)
                .addOnSuccessListener { geoCode(it) }
                .addOnFailureListener {
                    // Remplacer par un vrai bon message
                    Toast.makeText(this, getString(R.string.no_location), Toast.LENGTH_SHORT).show()
                }
        }
    }

    // permet un retour vers l'activité précédente
    override fun onSupportNavigateUp(): Boolean {
        super.finish()
        return true
    }

}