package com.bangkit2022.plantplanningyourplants.view.detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bangkit2022.plantplanningyourplants.MainActivity.Companion.CAMERA_RESULT
import com.bangkit2022.plantplanningyourplants.R
import com.bangkit2022.plantplanningyourplants.databinding.ActivityPlantDetailsBinding
import com.bangkit2022.plantplanningyourplants.ml.PlanTModel
import com.bangkit2022.plantplanningyourplants.response.PlantItems
import com.bangkit2022.plantplanningyourplants.uriToFile
import com.bangkit2022.plantplanningyourplants.view.CameraActivity
import org.tensorflow.lite.support.image.TensorImage
import java.io.File

class PlantDetails : AppCompatActivity() {
    private lateinit var plantDetailsViewModel : PlantDetailsViewModel
    private lateinit var activityPlantDetailsBinding : ActivityPlantDetailsBinding
    private lateinit var bitmap : Bitmap

    private var getPhotoImagePictureFile : File? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        activityPlantDetailsBinding = ActivityPlantDetailsBinding.inflate(layoutInflater)

        setContentView(activityPlantDetailsBinding.root)

        activityPlantDetailsBinding.identificationButton.isEnabled = false
        activityPlantDetailsBinding.identificationButton.setOnClickListener {
            setupViewModel()
        }

        activityPlantDetailsBinding.galleryButton.setOnClickListener {
            startGallery()
        }

        activityPlantDetailsBinding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)

            launcherIntentCameraActivityResultContracts.launch(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    private fun startGallery() {
        val intent = Intent()

        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"

        val chooser = Intent.createChooser(intent,"Choose A Picture")

        launcherIntentGalleryActivityResultContracts.launch(chooser)
    }

    private val launcherIntentGalleryActivityResultContracts = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            activityPlantDetailsBinding.identificationButton.isEnabled = true

            val selectedImageFileData : Uri = it.data?.data as Uri
            val myPhotoImagePictureFile = uriToFile(selectedImageFileData,this)

            getPhotoImagePictureFile = myPhotoImagePictureFile
            activityPlantDetailsBinding.imageViewDetails.setImageURI(selectedImageFileData)
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImageFileData)
        }
    }

    private val launcherIntentCameraActivityResultContracts = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == CAMERA_RESULT) {
            activityPlantDetailsBinding.identificationButton.isEnabled = true

            val myPhotoImagePictureFile = it.data?.getSerializableExtra("picture") as File

            getPhotoImagePictureFile = myPhotoImagePictureFile

            val photoImagePictureFileResult = BitmapFactory.decodeFile(myPhotoImagePictureFile.path)

            activityPlantDetailsBinding.imageViewDetails.setImageBitmap(photoImagePictureFileResult)
            bitmap = photoImagePictureFileResult
        }
    }

    private fun setupViewModel() {
        val modelInstance = PlanTModel.newInstance(this)

        val tensorImageBitmap = TensorImage.fromBitmap(bitmap)

        val photoImagePictureOutputResult = modelInstance.process(tensorImageBitmap).probabilityAsCategoryList.apply {
            sortByDescending {
                it.score
            }
        }

        val photoImagePictureOutputResultProbability = photoImagePictureOutputResult[0]

        plantDetailsViewModel = ViewModelProvider(this).get(PlantDetailsViewModel::class.java)
        plantDetailsViewModel.setPlant(photoImagePictureOutputResultProbability.label)

        activityPlantDetailsBinding.plantNameTextView.setText(photoImagePictureOutputResultProbability.label)

        activityPlantDetailsBinding.buyLocationMapsButton.setOnClickListener {
            val locationMapsUri = "http://maps.google.co.in/maps?q= ${photoImagePictureOutputResultProbability.label}"
            val intent = Intent(Intent.ACTION_VIEW,Uri.parse(locationMapsUri))

            startActivity(intent)
        }

        modelInstance.close()

        plantDetailsViewModel.plantMutableLiveData.observe(this, {
            setPlantData(it.plants)
        })
    }

    private fun setPlantData(data : List<PlantItems?>?) {
        activityPlantDetailsBinding.apply {
            if (data != null) {
                for (item in data) {
                    plantNameTextView.text = item?.name
                    plantDescriptionTextView.text = item?.description
                    plantLatinNameTextView.text = item?.latinname
                    howToCareOfPlantsTextView.text = item?.howtocare
                }
            }
        }
    }
}