package com.example.sumsubtesttask.presentation.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sumsubtesttask.R
import com.example.sumsubtesttask.databinding.FragmentCameraBinding

private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

class CameraFragment : Fragment(R.layout.fragment_camera) {

    private val viewBinding by viewBinding(FragmentCameraBinding::bind)

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        handlePermissionsRequestResult(permissions)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request camera permissions
        if (hasPermissions()) {
            startCamera()
        } else {
            requestPermissions()
        }
    }

    private fun startCamera() {
        // TODO
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun hasPermissions(): Boolean {
        val context = requireContext()

        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun handlePermissionsRequestResult(permissions: Map<String, Boolean>) {
        val permissionsGranted = permissions.all { it.value }

        if (permissionsGranted) {
            startCamera()
        } else {
            // TODO Refactor
            Toast.makeText(
                requireContext(),
                "Permission request denied",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }
}
