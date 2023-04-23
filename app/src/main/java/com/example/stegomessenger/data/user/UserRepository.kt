package com.example.stegomessenger.data.user

import com.example.stegomessenger.arch.util.Prefs
import com.example.stegomessenger.common.PrefsContract
import com.example.stegomessenger.common.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val prefs: Prefs
) {
    suspend fun logIn(login: String, password: String): User =
        withContext(Dispatchers.IO) {
            val token = apiService.logIn(login, password).value
            prefs.saveString(PrefsContract.TOKEN, token)

            User(token)
        }

    suspend fun register(id: String, password: String, name: String): User =
        withContext(Dispatchers.IO) {
            val token = apiService.register(id, password, name).value

            prefs.saveString(PrefsContract.TOKEN, token)
            User(token)
        }
}