package com.example.stegomessenger.data.user

import com.example.stegomessenger.arch.util.Prefs
import com.example.stegomessenger.common.PrefsContract
import com.example.stegomessenger.common.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val prefs: Prefs
) : UserRepository{
    override suspend fun logIn(login: String, password: String): Unit =
        withContext(Dispatchers.IO) {
            val token = apiService.logIn(login, password).value
            prefs.saveString(PrefsContract.TOKEN, token)
        }

    override suspend fun register(id: String, password: String, name: String): Unit =
        withContext(Dispatchers.IO) {
            val token = apiService.register(id, password, name).value

            prefs.saveString(PrefsContract.TOKEN, token)
        }
}