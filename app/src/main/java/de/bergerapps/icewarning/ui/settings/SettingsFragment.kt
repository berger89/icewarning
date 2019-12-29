package de.bergerapps.icewarning.ui.settings


import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import de.bergerapps.icewarning.BuildConfig
import de.bergerapps.icewarning.R
import de.bergerapps.icewarning.prediction.PredictionDailyJob
import de.bergerapps.icewarning.util.SharedPrefUtil
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
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = ArrayList<String>()
        val hour = SharedPrefUtil().getTimePrediction(context!!)
        list.add(getString(R.string.notification_time, hour))
        list.add("About Frost Warning/nVersion " + BuildConfig.VERSION_NAME)
        list.add("Created by Alexander Berger/nVisit www.berger-apps.de")
        list.add("Open Source/nSource Code is available on GitHub")
        list.add("Eiswarnung API/nUsing the Eiswarnung API")

        rv_settings.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = SettingsAdapter(this, list)
        rv_settings.adapter = adapter
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        SharedPrefUtil().setTimePrediction(p1.toLong(), context!!)
        adapter.notifyDataSetChanged()
        PredictionDailyJob.schedule(activity!!.applicationContext)
    }

}
