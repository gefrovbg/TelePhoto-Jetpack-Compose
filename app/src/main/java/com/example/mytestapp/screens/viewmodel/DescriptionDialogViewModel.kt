package com.example.mytestapp.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.example.telephoto.domain.usecase.SaveDescriptionToSharedPreferencesUseCase

class DescriptionDialogViewModel(
    private val saveDescriptionToSharedPreferencesUseCase: SaveDescriptionToSharedPreferencesUseCase
): ViewModel() {

    fun saveDescriptionToSharedPreferences(isAccept: Boolean): Boolean{
        return saveDescriptionToSharedPreferencesUseCase.execute(isAccept)
    }

}