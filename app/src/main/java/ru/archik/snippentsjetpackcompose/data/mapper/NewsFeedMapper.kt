package ru.archik.snippentsjetpackcompose.data.mapper

import ru.archik.snippentsjetpackcompose.data.model.CommentsResponseDto
import ru.archik.snippentsjetpackcompose.data.model.NewsFeedResponseDto
import ru.archik.snippentsjetpackcompose.domain.FeedPost
import ru.archik.snippentsjetpackcompose.domain.PostComment
import ru.archik.snippentsjetpackcompose.domain.StatisticItem
import ru.archik.snippentsjetpackcompose.domain.StatisticType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

class NewsFeedMapper {

  fun mapResponseToPosts(responseDto: NewsFeedResponseDto): List<FeedPost> {
    val result = mutableListOf<FeedPost>()

    val posts = responseDto.newsFeedContent.posts
    val groups = responseDto.newsFeedContent.groups

    for (post in posts) {
      val group = groups.find { it.id == post.communityId.absoluteValue } ?: break
      val feedPost = FeedPost(
        id = post.id,
        communityId = post.communityId,
        communityName = group.name,
        publicationDate = mapTimestampToDate(post.date),
        communityImageUrl = group.imageUrl,
        contentText = post.text,
        contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
        statistics = listOf(
          StatisticItem(type= StatisticType.LIKES, post.likes.count),
          StatisticItem(type= StatisticType.VIEWS, post.views.count),
          StatisticItem(type= StatisticType.SHARES, post.reposts.count),
          StatisticItem(type= StatisticType.COMMENTS, post.comments.count)
        ),
        isLiked = post.likes.userLikes > 0
      )
      result.add(feedPost)
    }

    return result
  }

  fun mapResponseToComments(responseDto: CommentsResponseDto): List<PostComment> {
    val result = mutableListOf<PostComment>()

    val comments = responseDto.content.comments
    val profiles = responseDto.content.profiles

    for (comment in comments) {
      val author = profiles.firstOrNull { it.id == comment.authorId } ?: continue

      if (comment.text.isBlank()) continue

      val postComment = PostComment(
        id = comment.id,
        authorName = "${author.firstName} ${author.lastName}",
        authorAvatarUrl = author.avatarUrl,
        commentText = comment.text,
        publicDate = mapTimestampToDate(comment.date)
      )
      result.add(postComment)
    }

    return result
  }

  private fun mapTimestampToDate(timestamp: Long): String {
    val date = Date(timestamp * 1000)

    return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
  }
}