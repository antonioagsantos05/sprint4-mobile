package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class ResponseClientes(
    @SerializedName("_embedded") val embedded: EmbeddedClientes
)

data class EmbeddedClientes(
    @SerializedName("clienteModelList") val clienteList: List<ClienteResponse>
)
