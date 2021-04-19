package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.SettingsItem

class AdapterParam(private val deviceList: Array<SettingsItem>, private val onClick: ((selectedDevice: SettingsItem) -> Unit)? = null) : RecyclerView.Adapter<AdapterParam.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun showItem(
                item: SettingsItem,
                onClick: ((selectedDevice: SettingsItem) -> Unit)? = null
        ) {
            itemView.findViewById<TextView>(R.id.title).text = item.name
            itemView.findViewById<ImageView>(R.id.param).setImageResource(item.icon)
            itemView.setOnClickListener {
                item.onClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_param_histo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.showItem(deviceList[position], onClick)
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }
}