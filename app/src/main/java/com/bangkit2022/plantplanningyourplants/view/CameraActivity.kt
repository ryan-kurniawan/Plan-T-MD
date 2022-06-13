package com.bangkit2022.plantplanningyourplants.view

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.bangkit2022.plantplanningyourplants.*
import com.bangkit2022.plantplanningyourplants.MainActivity.Companion.CAMERA_RESULT
import com.bangkit2022.plantplanningyourplants.databinding.ActivityCameraBinding
import com.bangkit2022.plantplanningyourplants.view.detail.PlantDetails
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity(){
    private lateinit var activityCameraBinding : ActivityCameraBinding
    private lateinit var cameraExecutorService : ExecutorService

    private var cameraSelector : CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture : ImageCapture? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        activityCameraBinding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(activityCameraBinding.root)

        cameraExecutorService = Executors.newSingleThreadExecutor()

        activityCameraBinding.switchCamera.setOnClickListener {
            cameraSelector = if (cameraSelector.equals(CameraSelector.DEFAULT_BACK_CAMERA)) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            }
            else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }

            startCamera()
        }

        activityCameraBinding.captureImage.setOnClickListener {
            takePhoto()
        }

        activityCameraBinding.closeCamera.setOnClickListener {
            val intent = Intent(this, PlantDetails::class.java)

            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        activityCameraBinding.resultImage.setOnClickListener {
            val photoImagePictureFile = createFile(application)
            val intent = Intent()

            intent.putExtra("picture", photoImagePictureFile)
            setResult(CAMERA_RESULT,intent)
            finish()
        }
    }

    public override fun onResume() {
        super.onResume()

        hideUserInterfaceUI()
        startCamera()
    }

    public override fun onDestroy() {
        super.onDestroy()

        cameraExecutorService.shutdown()
    }

    private fun hideUserInterfaceUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        supportActionBar?.hide()
    }

    private fun takePhoto() {
        val photoImagePictureFileCapture = imageCapture ?: return
        val photoImagePictureFile = createFile(application)
        val outputPhotoImagePictureFileCaptureOptions = ImageCapture.OutputFileOptions.Builder(photoImagePictureFile).build()

        photoImagePictureFileCapture.takePicture(outputPhotoImagePictureFileCaptureOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputPhotoImagePictureFileCaptureResults : ImageCapture.OutputFileResults) {
                val savedPhotoImagePictureFile = Uri.fromFile(photoImagePictureFile)

                Toast.makeText(this@CameraActivity,"Image Has Been Captured Successfully!", Toast.LENGTH_SHORT).show()

                val photoImagePictureFileResult = BitmapFactory.decodeFile(savedPhotoImagePictureFile.path)

                activityCameraBinding.resultImage.setImageBitmap(photoImagePictureFileResult)
            }

            override fun onError(imageCaptureException : ImageCaptureException) {
                Toast.makeText(this@CameraActivity,"Image Failed Or Not Taken Successfully!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startCamera() {
        val processCameraProviderFuture = ProcessCameraProvider.getInstance(this)

        processCameraProviderFuture.addListener({
            val processCameraProvider : ProcessCameraProvider = processCameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(activityCameraBinding.previewViewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            try {
                processCameraProvider.unbindAll()
                processCameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exception : Exception) {
                Toast.makeText(this,"Failed Or Unsuccessfully Bring Up The Camera!", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }
}