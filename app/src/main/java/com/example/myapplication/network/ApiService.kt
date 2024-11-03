package com.example.myapplication.network


import com.example.myapplication.model.CadastroRequest
import com.example.myapplication.model.CadastroResponse
import com.example.myapplication.model.ClienteRequest
import com.example.myapplication.model.ClienteResponse
import com.example.myapplication.model.ResponseClientes
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("clientes")
    fun cadastro(@Body cadastroRequestList: List<CadastroRequest>): Call<CadastroResponse>

    @GET("clientes")
    fun getAllClientes(): Call<ResponseClientes>

    @PUT("clientes/{id}")
    fun updateCliente(@Path("id") id: Long, @Body cliente: ClienteRequest): Call<ClienteResponse>

    @DELETE("clientes/{id}")
    fun deleteCliente(@Path("id") id: Long): Call<Void>
}
