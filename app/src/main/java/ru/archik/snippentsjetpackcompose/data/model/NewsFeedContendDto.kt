package ru.archik.snippentsjetpackcompose.data.model

import com.google.gson.annotations.SerializedName

data class NewsFeedContendDto(
  @SerializedName("items") val posts: List<PostDto>,
  @SerializedName("groups") val groups: List<GroupDto>,
  @SerializedName("next_from") val nextFrom: String?
)
