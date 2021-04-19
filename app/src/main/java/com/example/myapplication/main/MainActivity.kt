package com.example.myapplication.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.*
import com.example.myapplication.activity.HistoryActivity
import com.example.myapplication.activity.LocationActivity
import com.example.myapplication.activity.ParameterActivity
import com.example.myapplication.data.LocalPreferences
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // bind pour l'activité main
    private lateinit var binding: ActivityMainBinding

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // implémentation du binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // lance l'activité des paramètres de l'app en appuyant sur la roue cranté
        binding.param.setOnClickListener {
            startActivity(ParameterActivity.getStartIntent(this))
        }

        // lance l'activité de localisation de l'app en appuyant sur le dragon radar
        binding.gps.setOnClickListener {
            startActivity(LocationActivity.getStartIntent(this))
        }

        // vérifie s'il faut lancer l'activité d'historique de localisation de l'app en appuyant sur le bouton historique
        binding.buttonHistory.setOnClickListener {
            val historical = LocalPreferences.getInstance(this).getHistory()

            // s'il n'y a aucun élément dans sharedPreference on affiche un toast
            if (historical != null && historical.size < 1){
                Toast.makeText(this, getString(R.string.toast_no_history), Toast.LENGTH_SHORT).show()
            }
            // sinon on lance l'activié d'historique des localisation
            else{
                startActivity(HistoryActivity.getStartIntent(this))
            }
        }
    }
}