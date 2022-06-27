package com.example.mytestapp.screens.viewmodel

import com.example.mytestapp.repository.TelegramBotMessageRepositoryAppImpl.Companion.tukTukClient
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.example.telephoto.data.repository.DescriptionSharedPreferencesRepositoryImpl.Companion.DESC_PREF_NAME
import com.example.telephoto.data.storage.sharedprefs.SharedPreferencesRepository
import com.example.telephoto.domain.models.Client
import com.example.telephoto.domain.models.Token
import com.example.telephoto.domain.usecase.*

@SuppressLint("MutableCollectionMutableState")
class SettingsViewModel(
    private val saveTokenToSharedPreferencesUseCase: SaveTokenToSharedPreferencesUseCase,
    private val getTokenFromSharedPreferencesUseCase: GetTokenFromSharedPreferencesUseCase,
    getAllClientUseCase: GetAllClientUseCase,
    getDescriptionFromSharedPreferencesUseCase: GetDescriptionFromSharedPreferencesUseCase,
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    lifecycleOwner: LifecycleOwner,
    private val telegramBotUseCase: TelegramBotUseCase,
    private val deleteClientByNicknameUseCase: DeleteClientByNicknameUseCase,
    private val updateClientUseCase: UpdateClientUseCase
): ViewModel() {

    var users by mutableStateOf(listOf<Client>())
    val tokenString = MutableLiveData<String>()
    private val booleanDescription = MutableLiveData<Boolean>()
    val showDescriptionBoolean: LiveData<Boolean> = booleanDescription

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when(key){
            DESC_PREF_NAME -> booleanDescription.value = getDescriptionFromSharedPreferencesUseCase.execute()
        }
    }

    init {
        Log.d("viewmodel", "create")
        users = getAllClientUseCase.execute().toList()
        tokenString.value = getTokenFromSharedPreferencesUseCase.execute()?.token?: "Insert Token!"
        booleanDescription.value = getDescriptionFromSharedPreferencesUseCase.execute()
        sharedPreferencesRepository.getSharedSharedPreferences().registerOnSharedPreferenceChangeListener(listener)

        tukTukClient.observe(lifecycleOwner) {
            val usersNicknameList = arrayListOf<String>()
            for (i in users.indices){
                usersNicknameList.add(users[i].nickname!!)
            }
            val client = Client(
                it.chatId,
                it.firstName,
                it.lastName,
                it.nickname,
                true
            )
            if (!usersNicknameList.contains(it.nickname)) {
                val _users = users.toMutableList()
                _users.add(
                    client
                )
                users = _users.toList()
            }
        }
    }

    fun saveToken(token: String){
        tokenString.value = token
        val tokenToSave = Token(token)
        saveTokenToSharedPreferencesUseCase.execute(tokenToSave)
        telegramBotUseCase.stop()
        telegramBotUseCase.start()
    }

    fun updateUser(user: Client): Boolean{
        return updateClientUseCase.execute(user)
    }

    fun deleteUser(user: Client): Boolean{
        return deleteClientByNicknameUseCase.execute(user)
    }


    override fun onCleared() {
        super.onCleared()
        Log.d("viewmodel", "cleared")
        sharedPreferencesRepository.getSharedSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener)
    }

}