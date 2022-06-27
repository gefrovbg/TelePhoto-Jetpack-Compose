package com.example.telegram.repository

import com.example.telephoto.domain.models.Client
import java.io.File

interface TelegramBotMessageRepository {

    fun onCommandPhoto(client: Client, successCallback: (File) -> Unit, errorCallback: (String) -> Unit)

    fun onCommandAdd(client: Client): String

}