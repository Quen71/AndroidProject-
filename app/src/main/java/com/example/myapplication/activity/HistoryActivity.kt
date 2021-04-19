package com.example.myapplication.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.LocalPreferences
import com.example.myapplication.R
import com.example.myapplication.data.SettingsItem
import com.example.myapplication.adapter.AdapterParam
import com.example.myapplication.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    // bind pour l'activité main
    private lateinit var binding: ActivityHistoryBinding

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, HistoryActivity::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // implémentation du binding
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Paramétrage du bandeau de l'activité
        setContentView(R.layout.activity_history)
        val res = ArrayList<String>()
        supportActionBar?.apply {
            setTitle(getString(R.string.history))
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // initialisation du recycler view
        val rvItems = findViewById<RecyclerView>(R.id.historyDevices)
        // récupération des données de l'historique via le share preferences
        val historical = LocalPreferences.getInstance(this).getHistory()
        val historicalArray : MutableList<SettingsItem> = ArrayList()

        // vérifie que la donnée d'historique n'est pas nulle
        if (historical != null){
            // boucle for pour afficher tous les élément de l'hisorique dans les recyclers
            for (i in historical.indices){
                historicalArray.add(i, SettingsItem(historical.elementAt(i),
                        R.drawable.ic_baseline_map_24) {
                    LocalPreferences.getInstance(this).clearHistory()
                    finish()
                })
            }
        }

        rvItems.layoutManager = LinearLayoutManager(this)
        rvItems.adapter = AdapterParam(historicalArray.toTypedArray())
    }

    // permet un retour vers l'activité précédente
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
