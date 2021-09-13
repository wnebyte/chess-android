package com.github.wnebyte.chess

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.core.content.ContextCompat
import androidx.core.view.marginEnd
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import java.lang.StringBuilder
import java.util.*

/** LogCat Tag */
private const val TAG = "MPConfigActivity"

/** Bundle key for a boolean */
private const val REQUESTING_LOCATION_UPDATES_KEY = "REQUESTING_LOCATION_UPDATES_KEY"


class MultiPlayerConfigActivity : AppCompatActivity()
{
    private lateinit var joinButton: Button

    private lateinit var locationTextView: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private lateinit var locationCallback: LocationCallback

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private var requestingLocationUpdates = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_player_config)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        joinButton = findViewById(R.id.join_button)
        locationTextView = findViewById(R.id.location_text_view)

        // initializes the FusedLocationProviderClient.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // creates the LocationRequest object.
        locationRequest = createLocationRequest()
        // creates the LocationCallback object.
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null) {
                    Log.i(TAG, "LocationCallback.onLocationResult()")
                    updateUI(locationResult.lastLocation)
                }
            }

            override fun onLocationAvailability(p0: LocationAvailability) {
                Log.i(TAG, "${p0.isLocationAvailable}")
                super.onLocationAvailability(p0)
            }
        }

        // initializes the ActivityResultLauncher.
        requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                            if (location != null) {
                                updateUI(location)
                            }
                        }
                    }
                }
        when {
            ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // if the proper location permissions are granted
                Log.i(TAG, "ACCESS_FINE_LOCATION GRANTED!")
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        Log.i(TAG, "getLastLocation()")
                        updateUI(location)
                    }
                }
            }
            else -> {
                // if the proper location permissions are not granted
                Log.i(TAG, "else")
                requestPermissionLauncher.launch(
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }

        requestingLocationUpdates = getPreferences(Context.MODE_PRIVATE)
                .getBoolean(getString(R.string.requesting_location_updates_key), true)

        // if the user has allowed for location updates
        if (requestingLocationUpdates) startLocationUpdates()
    }

    override fun onStop() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean(getString(R.string.requesting_location_updates_key), requestingLocationUpdates)
                    .apply()
        }
        super.onStop()
    }

    /**
     * OnPause() stops any location updates.
     */
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    /**
     * OnResume() location updates are started if the user has allowed for it.
     */
    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }

    /**
     * Creates a LocationRequest object.
     * @return the LocationRequest object.
     */
    fun createLocationRequest() : LocationRequest {
        val locationRequest = LocationRequest.create().apply {
            interval = 20000
            fastestInterval = 10000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { _ ->
            // location settings are appropriate
        }

        return locationRequest
    }

    /**
     * Starts location updates.
     */
    fun startLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback, Looper.getMainLooper())

        } catch (ex: SecurityException) {
            Log.i(TAG, "SecurityException")
        }
    }

    /**
     * Stops location updates.
     */
    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    /**
     * Updates the UI.
     */
    fun updateUI(location: Location) {
        val address = Geocoder(this, Locale.getDefault())
                .getFromLocation(location.latitude, location.longitude, 1)[0]
        val stringBuilder = StringBuilder()

        for (i in 0..address.maxAddressLineIndex) {
            if (i != address.maxAddressLineIndex - 1) {
                stringBuilder.append(address.getAddressLine(i)).append(", ")
            }
        }

        locationTextView.text = stringBuilder.toString()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_multi_player_config, menu)
        val checkBox: CheckBox = menu.findItem(R.id.requesting_location_updates).actionView as CheckBox
        checkBox.setText(R.string.requesting_location_updates_text)
        checkBox.isChecked = requestingLocationUpdates
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            requestingLocationUpdates = isChecked
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object
    {
        /**
         * @return a new instance.
         */
        fun newIntent(packageContext: Context) : Intent =
                Intent(packageContext, MultiPlayerConfigActivity::class.java)
    }
}