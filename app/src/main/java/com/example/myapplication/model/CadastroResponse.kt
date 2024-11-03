// CadastroResponse.kt
package com.example.myapplication.model

data class CadastroResponse(
    val id_cliente: Long,
    val nm_cliente: String,
    val nmEmail: String?,
    val nm_senha: String
)