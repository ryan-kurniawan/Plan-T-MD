package com.bangkit2022.plantplanningyourplants

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit2022.plantplanningyourplants.databinding.ActivityMainBinding
import com.bangkit2022.plantplanningyourplants.response.PlantItems
import com.bangkit2022.plantplanningyourplants.view.detail.PlantDetails
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding : ActivityMainBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var plantItems : PlantItems

    override fun onRequestPermissionsResult(requestCode : Int, permissions : Array<out String>, grantResults : IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (!allRequiredPermissionGranted()) {
                Toast.makeText(this, "No Required Permission Granted!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allRequiredPermissionGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        plantItems = PlantItems()

        if (!allRequiredPermissionGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSION)

            firebaseAuth = FirebaseAuth.getInstance()
            checkUser()
        }

        activityMainBinding.letsStartIdentifyingYourPlantsButton.setOnClickListener {
            intentDetail(plantItems)
        }
    }

    private fun intentDetail(plantItems : PlantItems) {
        val intent = Intent(this, PlantDetails::class.java)

        intent.putExtra("plant", plantItems)
        startActivity(intent)
    }

    private fun checkUser() {
        val firebaseAuthCurrentUser = firebaseAuth.currentUser

        if (firebaseAuthCurrentUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        else {
            val firebaseAuthCurrentUser = firebaseAuthCurrentUser.email

            activityMainBinding.letsComeCheckAndIdentifyYourPlantsTextView.text = firebaseAuthCurrentUser
        }
    }

    companion object {
        const val CAMERA_RESULT = 200
        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
    }
}