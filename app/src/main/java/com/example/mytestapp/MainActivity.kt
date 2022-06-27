package com.example.mytestapp

import android.os.Bundle
import android.view.OrientationEventListener
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mytestapp.repository.TelegramBotMessageRepositoryAppImpl
import com.example.mytestapp.screens.MainScreen
import com.example.mytestapp.ui.theme.MyTestAppTheme
import com.example.telegram.repository.TelegramBotRepositoryImpl
import com.example.telephoto.data.repository.TokenSharedPreferencesRepositoryImpl
import com.example.telephoto.domain.usecase.TelegramBotUseCase

class MainActivity : ComponentActivity() {

    private val tokenSharedPreferencesRepository by lazy { TokenSharedPreferencesRepositoryImpl(this) }
    private val telegramBotRepository by lazy { TelegramBotRepositoryImpl(
        tokenSharedPreferencesRepository = tokenSharedPreferencesRepository,
        telegramBotMessageOnCommandPhotoRepository = TelegramBotMessageRepositoryAppImpl(this),
        telegramBotMessageOnCommandAddRepository = TelegramBotMessageRepositoryAppImpl(this)
    ) }
    private val telegramBotUseCase by lazy { TelegramBotUseCase(telegramBotRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MyTestAppTheme {
                MainScreen()
            }

        }
        telegramBotUseCase.start()
        orientationEventListener.enable()
    }

    override fun onDestroy() {
        super.onDestroy()
        telegramBotUseCase.stop()
        orientationEventListener.disable()
    }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                val rota = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }

                _rotation.value = rota
            }
        }
    }


    companion object {
        private val _rotation: MutableLiveData<Int> = MutableLiveData<Int>()
        val rotation : LiveData<Int> = _rotation
    }
}