package com.example.telephoto.domain.usecase

import com.example.telephoto.domain.models.Client
import com.example.telephoto.domain.repository.DataBaseRepository

class UpdateClientUseCase(private val dataBaseRepository: DataBaseRepository) {

    fun execute(client: Client): Boolean{
        return dataBaseRepository.updateClient(client = client)
    }

}