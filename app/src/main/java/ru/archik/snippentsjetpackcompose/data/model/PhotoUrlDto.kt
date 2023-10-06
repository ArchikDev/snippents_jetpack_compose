package ru.archik.snippentsjetpackcompose.data.model

import com.google.gson.annotations.SerializedName

data class PhotoUrlDto(
  @SerializedName("url")
  val url: String
)
