package ru.archik.snippentsjetpackcompose

sealed class AuthState {
  object Authorized: AuthState()
  object NotAuthorized: AuthState()
  object Initial: AuthState()
}
