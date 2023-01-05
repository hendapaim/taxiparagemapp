package com.hendadev.nestmapangola

import androidx.annotation.DrawableRes
import java.util.*

data class Paragem(
    var _ID: UUID? = null,
    val nomeParagem: String,
    val localidade: String,
    @DrawableRes val imagemParagem: Int,
    val numTaxiAgora: Int,
    val estado: String,
    val coordenadas: List<Double>,
    val rotas: List<String>,
) {
    init {
        this._ID = UUID.randomUUID()
    }

    val longitude = coordenadas.first()
    val latitude = coordenadas.last()
}

const val ic_paragem = R.drawable.ic_paragem

var paragens = listOf(
    Paragem(nomeParagem = "Jardim do Éden",
        localidade = "Luanda",
        imagemParagem = ic_paragem,
        numTaxiAgora = 5,
        estado = "Facil",
        coordenadas = listOf(13.25216442346573, -8.941038514377965),
        rotas = listOf("Golfe 2", "Camama 1")),

    Paragem(nomeParagem = "Golfe 2",
        localidade = "Luanda",
        imagemParagem = ic_paragem,
        numTaxiAgora = 5,
        estado = "Dificil",
        coordenadas = listOf(13.254227042198181, -8.881043606549179),
        rotas = listOf("Benfica", "Jardin do Eden", "Camama 1", "Cuca")),

    Paragem(nomeParagem = "Benfica",
        localidade = "Luanda",
        imagemParagem = ic_paragem,
        numTaxiAgora = 5,
        estado = "Media",
        coordenadas = listOf(13.147550225257875, -8.95832685438416),
        rotas = listOf("Golfe 2", "Aeroporto", "Cacuaco", "Kilamba")),

    Paragem(nomeParagem = "Cuca",
        localidade = "Luanda",
        imagemParagem = ic_paragem,
        numTaxiAgora = 5,
        estado = "Dificil",
        coordenadas = listOf(13.278318643569948, -8.816593992086288),
        rotas = listOf("Golfe 2", "Sao Paulo", "Cacuaco", "Mercado do Kwanzas", "Kikolo")),

    )
