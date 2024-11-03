package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.network.RetrofitInstance
import com.example.myapplication.model.ResponseClientes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.d(TAG, "onCreate: Iniciando LoginActivity")

        val cadastroLink: TextView = findViewById(R.id.tv_cadastro)
        val loginButton: Button = findViewById(R.id.btn_login)
        val emailEditText: EditText = findViewById(R.id.inputEmail)
        val senhaEditText: EditText = findViewById(R.id.inputSenha)

        cadastroLink.setOnClickListener {
            Log.d(TAG, "onCreate: Clicou em 'Cadastrar' - indo para CadastroActivity")
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val senha = senhaEditText.text.toString().trim()

            Log.d(TAG, "onCreate: Clicou em 'Login' - email: $email, senha: $senha")

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                fazerLogin(email, senha)
            } else {
                Log.w(TAG, "onCreate: Campos de login vazios")
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fazerLogin(email: String, senha: String) {
        RetrofitInstance.api.getAllClientes().enqueue(object : Callback<ResponseClientes> {
            override fun onResponse(call: Call<ResponseClientes>, response: Response<ResponseClientes>) {
                if (response.isSuccessful) {
                    val clientes = response.body()?.embedded?.clienteList
                    val cliente = clientes?.find { it.nmEmail == email && it.nmSenha == senha }

                    if (cliente != null) {
                        // Salva o ID do cliente em SharedPreferences
                        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                        sharedPreferences.edit().apply {
                            putLong("clienteId", cliente.idCliente.toLong())  // Verifique se "idCliente" é o nome correto do campo
                            putString("nome", cliente.nmCliente)   // Verifique se "nmCliente" é o nome correto do campo
                            putString("email", cliente.nmEmail) // Verifique se "nmEmail" é o nome correto do campo
                            apply()
                        }
                        Log.d(TAG, "Cliente ID salvo no SharedPreferences: ${cliente.idCliente}")

                        // Redireciona para a MainActivity
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Log.w(TAG, "Email ou senha incorretos")
                        Toast.makeText(this@LoginActivity, "Email ou senha incorretos", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e(TAG, "Erro ao buscar clientes - Código: ${response.code()}")
                    Toast.makeText(this@LoginActivity, "Erro ao buscar clientes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseClientes>, t: Throwable) {
                Log.e(TAG, "Falha na requisição: ${t.message}")
                Toast.makeText(this@LoginActivity, "Falha na requisição: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
