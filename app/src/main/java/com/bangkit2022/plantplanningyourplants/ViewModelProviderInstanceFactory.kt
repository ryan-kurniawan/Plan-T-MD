package com.bangkit2022.plantplanningyourplants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit2022.plantplanningyourplants.view.detail.PlantDetailsViewModel

class ViewModelProviderInstanceFactory : ViewModelProvider.NewInstanceFactory() {
    override fun<viewModel : ViewModel?> create(modelClass : Class<viewModel>) : viewModel {
        return when {
            modelClass.isAssignableFrom(PlantDetailsViewModel::class.java) -> {
                PlantDetailsViewModel() as viewModel
            }

            else -> throw IllegalArgumentException("Unknown Model Class : " + modelClass.name)
        }
    }
}