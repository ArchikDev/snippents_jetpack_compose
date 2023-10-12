package ru.archik.snippentsjetpackcompose.data.repository

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.archik.snippentsjetpackcompose.data.mapper.NewsFeedMapper
import ru.archik.snippentsjetpackcompose.data.network.ApiFactory
import ru.archik.snippentsjetpackcompose.domain.*
import ru.archik.snippentsjetpackcompose.domain.entity.*
import ru.archik.snippentsjetpackcompose.domain.repository.NewsFeedRepository
import ru.archik.snippentsjetpackcompose.extensions.mergeWith

class NewsFeedRepositoryImpl(application: Application): NewsFeedRepository {

  private val storage = VKPreferencesKeyValueStorage(application)
  private val token
    get() = VKAccessToken.restore(storage)

  private val coroutineScope = CoroutineScope(Dispatchers.Default)
  private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
  private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()

  private val loadedListFlow = flow {
    nextDataNeededEvents.emit(Unit)
    nextDataNeededEvents.collect {
      val startFrom = nextFrom

      if (startFrom == null && feedPosts.isNotEmpty()) {
        emit(feedPosts)
        return@collect
      }

      val response = if (startFrom == null) {
        apiService.loadRecommendations(getAccessToken())
      } else {
        apiService.loadRecommendations(getAccessToken(), startFrom)
      }

      nextFrom = response.newsFeedContent.nextFrom

      val posts = mapper.mapResponseToPosts(response)

      _feedPosts.addAll(posts)

      emit(feedPosts)
    }
  }
//    .map { NewsFeedResult.Success(posts = it) as NewsFeedResult}
    .retry(2) { // если ошибка (2) - кол-во повторений
      delay(RETRY_TIMEOUT_MILLIS) // задержка перед повтором
      true // Чтобы при любой ошибки повторялся снова запрос
    }
//    .catch { emit(NewsFeedResult.Error) }

  private val apiService = ApiFactory.apiService
  private val mapper = NewsFeedMapper()

  private val _feedPosts = mutableListOf<FeedPost>()
  private val feedPosts: List<FeedPost>
    get() = _feedPosts.toList() // копия коллекции

  private var nextFrom: String? = null

  private val checkAuthStateEvents = MutableSharedFlow<Unit>(replay = 1)

  private val authStateFlow = flow {
    checkAuthStateEvents.emit(Unit)
    checkAuthStateEvents.collect {
      val currentToken = token
      val loggedIn = currentToken != null && currentToken.isValid

      val authState = if (loggedIn) AuthState.Authorized else AuthState.NotAuthorized

      emit(authState)
    }
  }.stateIn(
    scope = coroutineScope,
    started = SharingStarted.Lazily,
    initialValue = AuthState.Initial
  )

  override fun getAuthStateFlow(): StateFlow<AuthState> = authStateFlow

  override fun getRecommendations(): StateFlow<List<FeedPost>> = recommendations

  private val recommendations: StateFlow<List<FeedPost>> = loadedListFlow
    .mergeWith(refreshedListFlow)
    .stateIn(
      scope = coroutineScope,
      started = SharingStarted.Lazily,
      initialValue = feedPosts
    )

  override suspend fun loadNextData() {
    nextDataNeededEvents.emit(Unit)
  }

  override suspend fun checkAuthState() {
    checkAuthStateEvents.emit(Unit)
  }

  override fun getComments(feedPost: FeedPost): StateFlow<List<PostComment>> = flow {
    val comments = apiService.getComments(
      token = getAccessToken(),
      ownerId = feedPost.communityId,
      postId = feedPost.id
    )

    emit(mapper.mapResponseToComments(comments))
  }.retry {
    delay(RETRY_TIMEOUT_MILLIS)
    true
  }.stateIn(
    scope = coroutineScope,
    started = SharingStarted.Lazily,
    initialValue = listOf()
  )

  private fun getAccessToken(): String {
    return token?.accessToken ?: throw IllegalStateException("Token is null")
  }

  @RequiresApi(Build.VERSION_CODES.N)
  override suspend fun changeLikeStatus(feedPost: FeedPost) {
    val response = if (feedPost.isLiked) {
      apiService.deleteLike(
        token = getAccessToken(),
        ownerId = feedPost.communityId,
        postId = feedPost.id
      )
    } else {
      apiService.addLike(
        token = getAccessToken(),
        ownerId = feedPost.communityId,
        postId = feedPost.id
      )
    }

    val newLikesCount = response.likes.count

    val newStatistics = feedPost.statistics.toMutableList().apply {
      removeIf { it.type == StatisticType.LIKES }
      add(StatisticItem(type = StatisticType.LIKES, newLikesCount))
    }

    val newPost = feedPost.copy(statistics =  newStatistics, isLiked = !feedPost.isLiked)
    val postIndex = _feedPosts.indexOf(feedPost)

    _feedPosts[postIndex] = newPost

    refreshedListFlow.emit(feedPosts)

  }

  override suspend fun deletePost(feedPost: FeedPost) {
    apiService.ignorePost(
      token = getAccessToken(),
      ownerId = feedPost.communityId,
      postId = feedPost.id
    )

    _feedPosts.remove(feedPost)

    refreshedListFlow.emit(feedPosts)
  }

  companion object {
    private const val RETRY_TIMEOUT_MILLIS = 3000L
  }
}