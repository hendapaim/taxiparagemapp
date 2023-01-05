package com.hendadev.nestmapangola

import com.google.gson.annotations.SerializedName

// Tipo de dado de retorno da Api OpenService
data class RouteResponse(@SerializedName("features") val features: List<Feature>)
data class Feature(@SerializedName("geometry") val geometry: Geometry)
data class Geometry(@SerializedName("coordinates") val coordinates: List<List<Double>>)
