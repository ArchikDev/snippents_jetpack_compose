package ru.archik.snippentsjetpackcompose.domain.entity

sealed class AuthState {
  object Authorized: AuthState()
  object NotAuthorized: AuthState()
  object Initial: AuthState()
}
