package de.bergerapps.icewarning.ui.main


import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.bergerapps.icewarning.BuildConfig
import de.bergerapps.icewarning.R
import de.bergerapps.icewarning.worker.MyDailyJob
import kotlinx.android.synthetic.main.fragment_settings.*


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment(), TimePickerDialog.OnTimeSetListener {

    private lateinit var adapter: SettingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = ArrayList<String>()
        val hour = context!!.getSharedPreferences("frostwarning", 0).getLong(
            "start",
            15
        )
        list.add(getString(R.string.notification_time, hour))
        list.add("About Frost Warning/nVersion " + BuildConfig.VERSION_NAME)
        list.add("Created by Alexander Berger/nVisit www.berger-apps.de")
        list.add("Open Source/nSource Code is available on GitHub")
        list.add("Eiswarnung API/nUsing the Eiswarnung API")

        rv_settings.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = SettingsAdapter(context!!, list)
        rv_settings.adapter = adapter
    }

    inner class SettingsAdapter(val context: Context, val list: ArrayList<String>) :
        RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.item_settings, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (position == 1) {
                holder.imageViewSettings.visibility = View.INVISIBLE
            } else if (position == 0) {
                holder.imageViewSettingsOption.visibility = View.VISIBLE
            }
            val texts = list[position].split("/n")

            holder.textView.text = texts[0]
            holder.textViewDesc.text = texts[1]

            if (position == 0) {
                val hour = context.getSharedPreferences("frostwarning", 0).getLong(
                    "start",
                    15
                )
                val texts = getString(R.string.notification_time, hour).split("/n")

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

            override fun onClick(view: View) {
                when (adapterPosition) {
                    0 -> openTimePickerDialog()
                    1 -> return
                    2 -> {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.berger-apps.de")
                            )
                        )
                    }
                    3 -> startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/berger89/icewarning")
                        )
                    )
                    4 -> startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://www.eiswarnung.de/rest-api/")
                        )
                    )
                    else -> startActivity(
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
            }
        }

    }

    private fun openTimePickerDialog() {
        val hour = context!!.getSharedPreferences("frostwarning", 0).getLong(
            "start",
            15
        )
        TimePickerDialog(context, R.style.MyDialogTheme, this, hour.toInt(), 0, true).show()
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        context!!.getSharedPreferences("frostwarning", 0).edit().putLong("start", p1.toLong())
            .apply()
        adapter.notifyDataSetChanged()
        MyDailyJob.schedule(activity!!.applicationContext)
    }

}
