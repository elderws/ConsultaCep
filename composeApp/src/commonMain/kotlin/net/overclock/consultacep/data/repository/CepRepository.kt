package net.overclock.consultacep.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import net.overclock.consultacep.data.model.Endereco

class CepRepository {
    private val client = HttpClient {
        expectSuccess = true
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun buscarCep(cep: String): Endereco =
        client.get("https://viacep.com.br/ws/${cep}/json/").body()
}