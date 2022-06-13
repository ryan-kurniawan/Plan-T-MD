package com.bangkit2022.plantplanningyourplants.view.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit2022.plantplanningyourplants.api.ApiConfiguration
import com.bangkit2022.plantplanningyourplants.response.Data
import com.bangkit2022.plantplanningyourplants.response.PlantResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantDetailsViewModel : ViewModel() {
    private val _plantMutableLiveData = MutableLiveData<Data>()
    val plantMutableLiveData : LiveData<Data> = _plantMutableLiveData

    fun setPlant(name : String) {
        val APIClientServiceConfiguration = ApiConfiguration.getApiService().getPlant(name)

        APIClientServiceConfiguration.enqueue(object : Callback<PlantResponse> {
            override fun onResponse(call : Call<PlantResponse>, response : Response<PlantResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        _plantMutableLiveData.postValue(responseBody.data)
                    }
                }
            }

            override fun onFailure(call : Call<PlantResponse>, throwable : Throwable) {
                Log.e(TAG.toString(),"onFailure : ${throwable.message}")
            }
        })
    }

    companion object {
        val TAG = PlantDetailsViewModel::class.java
    }
}