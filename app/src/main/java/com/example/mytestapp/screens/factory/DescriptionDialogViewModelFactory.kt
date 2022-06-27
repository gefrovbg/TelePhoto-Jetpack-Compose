package com.example.mytestapp.screens.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mytestapp.screens.viewmodel.DescriptionDialogViewModel
import com.example.telephoto.data.repository.DescriptionSharedPreferencesRepositoryImpl
import com.example.telephoto.domain.usecase.SaveDescriptionToSharedPreferencesUseCase

class DescriptionDialogViewModelFactory(context: Context): ViewModelProvider.Factory {

    private val descriptionSharedPreferencesRepository by lazy { DescriptionSharedPreferencesRepositoryImpl(context) }
    private val saveDescriptionToSharedPreferencesUseCase by lazy { SaveDescriptionToSharedPreferencesUseCase(descriptionSharedPreferencesRepository) }


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DescriptionDialogViewModel(
            saveDescriptionToSharedPreferencesUseCase = saveDescriptionToSharedPreferencesUseCase
        ) as T
    }
}