package com.bangkit2022.plantplanningyourplants.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PlantResponse(
	@field : SerializedName("data")
	val data : Data? = null,

	@field : SerializedName("status")
	val status : String? = null,

	@field : SerializedName("message")
	val message : String? = null
)

@Parcelize
data class PlantItems(
	@field : SerializedName("id")
	val id : String? = null,

	@field : SerializedName("name")
	val name : String? = null,

	@field : SerializedName("description")
	val description : String? = null,

	@field : SerializedName("latinname")
	val latinname : String? = null,

	@field : SerializedName("howtocare")
	val howtocare : String? = null,

	@field : SerializedName("first")
	val first : String? = null,

	@field : SerializedName("second")
	val second : String? = null,

	@field : SerializedName("third")
	val third : String? = null,

	@field : SerializedName("fourth")
	val fourth : String? = null,

	@field : SerializedName("fifth")
	val fifth : String? = null,

	@field : SerializedName("sixth")
	val sixth : String? = null,

	@field : SerializedName("seventh")
	val seventh : String? = null,

	@field : SerializedName("eighth")
	val eighth : String? = null,

	@field : SerializedName("ninth")
	val ninth : String? = null,

	@field : SerializedName("tenth")
	val tenth : String? = null
) : Parcelable

data class Data(
	@field : SerializedName("plants")
	val plants : List<PlantItems?>? = null
)