package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class ClienteResponse(
    @SerializedName("id_cliente") val idCliente: Int,
    @SerializedName("nm_cliente") val nmCliente: String? = "",
    @SerializedName("tp_cliente") val tpCliente: String? = null,
    @SerializedName("dt_cadastro") val dtCadastro: String? = null,
    @SerializedName("nmEmail") val nmEmail: String = "",
    @SerializedName("nm_senha") val nmSenha: String = ""
)
