package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class ClienteRequest(
    @SerializedName("nm_cliente") val nmCliente: String,
    @SerializedName("nmEmail") val nmEmail: String,
    @SerializedName("nm_senha") val nmSenha: String
)
