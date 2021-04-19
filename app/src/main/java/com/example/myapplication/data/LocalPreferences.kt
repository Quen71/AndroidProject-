package com.example.myapplication.data

import android.content.Context
import android.content.SharedPreferences

class LocalPreferences private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)

    fun saveStringValue(yourValue: String?) {
        sharedPreferences.edit().putString("saveStringValue", yourValue).apply()
    }

    fun getSaveStringValue(): String? {
        return sharedPreferences.getString("saveStringValue", null)
    }

    // ajout de données dans l'historique
    fun addToHistory(newEntry: String){
        // récupération des données du shared preferences
        val set = sharedPreferences.getStringSet("histories", HashSet<String>())
        val history = HashSet<String>(set)
        // calcul du numéro du nouvel élément
        val size = history.size + 1
        // si la valeur entrée n'est pas vide on l'ajoute à l'historique
        if (newEntry.isNotEmpty()){
            history.add("$size - $newEntry")
        }
        // ajout de la valeur dans le shared preferences
        sharedPreferences.edit().putStringSet("histories", history).apply()
    }

    // récupération des données de l'historique
    fun getHistory(): MutableSet<String>? {
        return sharedPreferences.getStringSet("histories", emptySet())
    }

    // efface les données contenu dans shared preferences (historique)
    fun clearHistory(){
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private var INSTANCE: LocalPreferences? = null

        fun getInstance(context: Context): LocalPreferences {
            return INSTANCE?.let {
                INSTANCE
            } ?: run {
                INSTANCE = LocalPreferences(context)
                return INSTANCE!!
            }
        }
    }

}
