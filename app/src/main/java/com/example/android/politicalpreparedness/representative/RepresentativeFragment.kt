package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.models.Address
import com.example.android.politicalpreparedness.models.UiMessage
import com.example.android.politicalpreparedness.models.resolveMessage
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.util.autoCleared
import com.example.android.politicalpreparedness.util.extensions.TAG
import com.example.android.politicalpreparedness.util.extensions.launchAndRepeatWithViewLifecycle
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

private const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION

@AndroidEntryPoint
class RepresentativeFragment : Fragment() {

    private var binding by autoCleared<FragmentRepresentativeBinding>()
    private var adapter by autoCleared<RepresentativeListAdapter>()

    val viewModel by viewModels<RepresentativeViewModel>()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLocation()
            } else {
                showLocationRequestPermissionRationale()
            }
        }

    private val resolutionLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                checkLocationPermissions()
            }
        }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepresentativeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupUi()
        observeActions()
        observeViewModel()
    }

    private fun initViewModel() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupUi() {
        setupList()
        setupStateDropdown()
        setupClickListeners()
    }

    private fun observeActions() {}

    private fun observeViewModel() {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.uiState.map { it.representatives }.collect { list ->
                    adapter.submitList(list)
                }
            }
            launch {
                viewModel.state.collect {
                    (binding.stateDropdownInputLayout.editText as? AutoCompleteTextView)?.setText(
                        it,
                        false
                    )
                }
            }
        }
    }

    private fun setupList() {
        adapter = RepresentativeListAdapter()
        binding.representativeRecyclerView.adapter = adapter
    }

    private fun setupStateDropdown() {
        val items = resources.getStringArray(R.array.states)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_list_item, items)
        val textview = (binding.stateDropdownInputLayout.editText as? AutoCompleteTextView)
        if (textview != null) {
            textview.setAdapter(adapter)
            textview.setOnItemClickListener { _, _, position, id ->
                val selectedItem = adapter.getItem(position)
                viewModel.state.value = selectedItem.orEmpty()
            }
        }

    }

    private fun setupClickListeners() {
        binding.searchButton.setOnClickListener {
            hideKeyboard()
            viewModel.search()
        }
        binding.useMyLocationButton.setOnClickListener {
            hideKeyboard()
            checkLocationPermissions()
        }
    }

    private fun checkLocationPermissions() {
        when {
            isPermissionGranted() -> {
                getLocation()
            }
            shouldShowLocationRequestPermissionRationale() -> {
                showLocationRequestPermissionRationale()
            }
            else -> {
                requestPermissionLauncher.launch(LOCATION_PERMISSION)
            }
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            LOCATION_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldShowLocationRequestPermissionRationale(): Boolean {
        return shouldShowRequestPermissionRationale(LOCATION_PERMISSION)
    }

    private fun showLocationRequestPermissionRationale() {
        Snackbar.make(
            binding.root,
            R.string.snackbar_text_permission_denied_explanation, Snackbar.LENGTH_LONG
        )
            .setAction(R.string.snackbar_action_settings) {
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }.show()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.d(TAG, "getLocation: $location")
                if (location != null) {
                    showCurrentLocation(location)
                } else {
                    startLocationUpdates()
                }
            }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.getRequestingLocationUpdates()) startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = createLocationRequest()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(requireContext())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            viewModel.setRequestingLocationUpdates(true)
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        viewModel.setRequestingLocationUpdates(false)
                        showCurrentLocation(locationResult.lastLocation)
                    }
                },
                Looper.getMainLooper()
            )
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    resolutionLauncher.launch(
                        IntentSenderRequest.Builder(exception.resolution).build()
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun showCurrentLocation(location: Location) {
        val address = geoCodeLocation(location)
        viewModel.setAddress(address)
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare.orEmpty(),
                    address.subThoroughfare.orEmpty(),
                    address.locality.orEmpty(),
                    address.adminArea.orEmpty(),
                    address.postalCode.orEmpty()
                )
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}