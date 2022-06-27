package com.example.mytestapp.screens.factory

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytestapp.repository.TelegramBotMessageRepositoryAppImpl
import com.example.mytestapp.screens.viewmodel.SettingsViewModel
import com.example.telegram.repository.TelegramBotRepositoryImpl
import com.example.telephoto.data.repository.DataBaseRepositoryImpl
import com.example.telephoto.data.repository.DescriptionSharedPreferencesRepositoryImpl
import com.example.telephoto.data.repository.TokenSharedPreferencesRepositoryImpl
import com.example.telephoto.data.storage.sharedprefs.SharedPreferenceRepositoryImpl
import com.example.telephoto.domain.usecase.*

class SettingsViewModelFactory(contextApp: Context, private val lifecycleOwner: LifecycleOwner): ViewModelProvider.Factory {

    private val descriptionSharedPreferencesRepository by lazy { DescriptionSharedPreferencesRepositoryImpl(contextApp) }
    private val getDescriptionFromSharedPreferencesUseCase by lazy { GetDescriptionFromSharedPreferencesUseCase(descriptionSharedPreferencesRepository) }
    private val tokenSharedPreferencesRepository by lazy { TokenSharedPreferencesRepositoryImpl(contextApp) }
    private val getTokenFromSharedPreferencesUseCase by lazy { GetTokenFromSharedPreferencesUseCase(tokenSharedPreferencesRepository) }
    private val saveTokenToSharedPreferencesUseCase by lazy { SaveTokenToSharedPreferencesUseCase(tokenSharedPreferencesRepository) }
    private val dataBaseRepository by lazy { DataBaseRepositoryImpl(contextApp, null) }
    private val getAllClientUseCase = GetAllClientUseCase(dataBaseRepository)
    private val sharedPreferencesRepository by lazy {SharedPreferenceRepositoryImpl(contextApp)}
    private val telegramBotRepository by lazy { TelegramBotRepositoryImpl(
        tokenSharedPreferencesRepository = tokenSharedPreferencesRepository,
        telegramBotMessageOnCommandPhotoRepository = TelegramBotMessageRepositoryAppImpl(contextApp),
        telegramBotMessageOnCommandAddRepository = TelegramBotMessageRepositoryAppImpl(contextApp)
    ) }
    private val telegramBotUseCase by lazy { TelegramBotUseCase(telegramBotRepository) }

    private val deleteClientByNicknameUseCase by lazy { DeleteClientByNicknameUseCase(dataBaseRepository) }
    private val updateClientUseCase by lazy { UpdateClientUseCase(dataBaseRepository) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            saveTokenToSharedPreferencesUseCase = saveTokenToSharedPreferencesUseCase,
            getAllClientUseCase = getAllClientUseCase,
            getTokenFromSharedPreferencesUseCase = getTokenFromSharedPreferencesUseCase,
            getDescriptionFromSharedPreferencesUseCase = getDescriptionFromSharedPreferencesUseCase,
            sharedPreferencesRepository = sharedPreferencesRepository,
            lifecycleOwner = lifecycleOwner,
            telegramBotUseCase = telegramBotUseCase,
            deleteClientByNicknameUseCase = deleteClientByNicknameUseCase,
            updateClientUseCase = updateClientUseCase
        ) as T
    }
}