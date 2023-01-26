package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.*
import java.util.Locale


class RepresentativesFragment : Fragment() {



    val viewModel: RepresentativeViewModel by viewModels()
    private lateinit var requestLocationPermissionLauncher : ActivityResultLauncher<String>
    private lateinit var enableLocationSettingLauncher : ActivityResultLauncher<IntentSenderRequest>



    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

       val binding = FragmentRepresentativeBinding.inflate(inflater)


        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = RepresentativeListAdapter()

        binding.lifecycleOwner = viewLifecycleOwner
        binding.representativesRecyclerView.adapter = adapter

        viewModel.representatives.observe(viewLifecycleOwner, {representatives ->
            adapter.submitList(representatives)
        })


        binding.buttonSearch.setOnClickListener {

            hideKeyboard()
            viewModel.onSearchButtonClick()
        }

        registerLocationPermissionsCallback()
        registerEnableLocationCallback()

        binding.buttonLocation.setOnClickListener {

            checkLocationPermissions()
        }

        return binding.root

    }



    private fun registerLocationPermissionsCallback() {

        requestLocationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->

            if (isGranted) {
                checkDeviceLocationSettingsAndGetLocation()
            }
        }
    }

    private fun registerEnableLocationCallback() {

        enableLocationSettingLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK)
                getLocation()
        }
    }



    private fun checkLocationPermissions() {


        if (isPermissionGranted()) {
            checkDeviceLocationSettingsAndGetLocation()
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun isPermissionGranted() : Boolean {

        var granted = false
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            granted = true
        }

        return granted

    }


    private fun checkDeviceLocationSettingsAndGetLocation(resolve:Boolean = true) {

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity())
        val locationSettingsResponseTask = settingsClient.checkLocationSettings(builder.build())


        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve){
                try {

                    val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                    enableLocationSettingLauncher.launch(intentSenderRequest)

                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }

        locationSettingsResponseTask.addOnCompleteListener {

            if ( it.isSuccessful ) {
                getLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {

        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                locationResult.let {

                    val address = geoCodeLocation(it.lastLocation)
                    viewModel.refreshByCurrentLocation(address)

                    fusedLocationProviderClient.removeLocationUpdates(this)
                }
            }
        }

        val locationRequest = LocationRequest.create()

        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        Looper.myLooper()?.let {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback,
                it
            )
        }


    }

    private fun geoCodeLocation(location: Location): Address {

        val geocoder = Geocoder(context, Locale.getDefault())

        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}