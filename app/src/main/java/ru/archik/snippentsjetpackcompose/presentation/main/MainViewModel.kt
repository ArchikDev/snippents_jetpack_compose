package ru.archik.snippentsjetpackcompose.presentation.main

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.archik.snippentsjetpackcompose.data.repository.NewsFeedRepositoryImpl
import ru.archik.snippentsjetpackcompose.domain.usecases.CheckAuthStateUseCase
import ru.archik.snippentsjetpackcompose.domain.usecases.GetAuthStateFlowUseCase

class MainViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = NewsFeedRepositoryImpl(application)

  private val getAuthStateFlowUseCase = GetAuthStateFlowUseCase(repository)
  private val checkAuthStateUseCase = CheckAuthStateUseCase(repository)

  val authState = getAuthStateFlowUseCase()

  fun performAuthResult() {
    viewModelScope.launch {
      checkAuthStateUseCase()
    }
  }
}