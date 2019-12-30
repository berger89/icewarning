package de.bergerapps.icewarning.ui.settings

import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.bergerapps.icewarning.R
import de.bergerapps.icewarning.util.SharedPrefUtil

class SettingsAdapter(val settingsFragment: SettingsFragment, private val list: ArrayList<String>) :
    RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(settingsFragment.context)
                .inflate(R.layout.item_settings, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            0 -> {
                holder.imageViewSettingsOption.visibility = View.VISIBLE
                holder.imageViewSettingsCafe.visibility = View.INVISIBLE
            }
            3 -> {
                holder.imageViewSettingsCafe.visibility = View.VISIBLE
                holder.imageViewSettingsOption.visibility = View.INVISIBLE
            }
            else -> {
                holder.imageViewSettings.visibility = View.INVISIBLE
            }
        }

        var texts = list[position].split("/n")

        holder.textView.text = texts[0]
        holder.textViewDesc.text = texts[1]

        if (position == 0) {
            val hour = SharedPrefUtil().getTimePrediction(settingsFragment.context!!)
            texts = settingsFragment.getString(R.string.notification_time, hour).split("/n")
            holder.textView.text = texts[0]
        }
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var textView: TextView
        var textViewDesc: TextView
        var imageViewSettings: ImageView
        var imageViewSettingsOption: ImageView
        var imageViewSettingsCafe: ImageView

        override fun onClick(view: View) {
            when (adapterPosition) {
                0 -> openTimePickerDialog()
                1 -> return
                2 -> {
                    settingsFragment.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.berger-apps.de")
                        )
                    )
                }
                3 -> settingsFragment.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://ko-fi.com/berger")
                    )
                )
                4 -> settingsFragment.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/berger89/icewarning")
                    )
                )
                5 -> settingsFragment.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.eiswarnung.de/rest-api/")
                    )
                )
                else -> settingsFragment.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.berger-apps.de")
                    )
                )

            }
        }

        init {
            itemView.setOnClickListener(this)
            textView = itemView.findViewById(R.id.tv_settings)
            textViewDesc = itemView.findViewById(R.id.tv_settings_desc)
            imageViewSettings = itemView.findViewById(R.id.imageView_settings_next)
            imageViewSettingsOption = itemView.findViewById(R.id.imageView_settings_option)
            imageViewSettingsCafe = itemView.findViewById(R.id.imageView_settings_cafe)
        }
    }

    private fun openTimePickerDialog() {
        val hour = SharedPrefUtil().getTimePrediction(settingsFragment.context!!)
        TimePickerDialog(
            settingsFragment.context,
            R.style.MyDialogTheme,
            settingsFragment,
            hour.toInt(),
            0,
            true
        ).show()
    }
}