package de.bergerapps.icewarning.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.*
import de.bergerapps.icewarning.R
import kotlinx.android.synthetic.main.main_fragment.*
import java.text.SimpleDateFormat


class MainFragment : Fragment() {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    var PERMISSION_ID = 44

    private val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            val mCurrentLocation = locationResult!!.lastLocation
            progress.visibility = View.VISIBLE
            viewModel.getEiswarnung(
                context!!,
                mCurrentLocation!!.latitude.toString(),
                mCurrentLocation.longitude.toString()
            )

        }
    }


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getLastLocation()

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.eiswarnungLiveData.observe(this, Observer {
            progress.visibility = View.GONE
            if (it?.result == null) {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                return@Observer
            }
            // Forecast Text
            forecast.text = it.result?.forecastText
            // Forecast City
            city.text = it.result?.forecastCity

            // Forecast Date
            val dateSdf = SimpleDateFormat("yyyy-MM-dd").parse(it.result?.forecastDate!!)
            val dateDay = DateFormat.format("EEEE, dd.MM.yyyy", dateSdf)
            date.text = "$dateDay"

            // Forecast Image
            when (it.result?.forecastId) {
                0 -> imageView2.setImageDrawable(resources.getDrawable(R.drawable.not_ice))
                1 -> imageView2.setImageDrawable(resources.getDrawable(R.drawable.ice))
                else -> { // Note the block
                    imageView2.setImageDrawable(resources.getDrawable(R.drawable.not_ice))
                }
            }
            Toast.makeText(context!!, "${it.callsLeft}", Toast.LENGTH_SHORT).show()
        })

    }


    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
                getLastLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        progress.visibility = View.VISIBLE

                        viewModel.getEiswarnung(

                            context!!,
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                        //latTextView.setText(location!!.getLatitude() + "")
                        //lonTextView.setText(location!!.getLongitude() + "")
                    }
                }
            } else {
                Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, locationCallBack,
            Looper.myLooper()
        )

    }

}
