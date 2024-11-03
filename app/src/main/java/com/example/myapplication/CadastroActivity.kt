// CadastroActivity.kt
package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.model.CadastroRequest
import com.example.myapplication.model.CadastroResponse
import com.example.myapplication.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroActivity : AppCompatActivity() {

    private val TAG = "CadastroActivity"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val loginLink: TextView = findViewById(R.id.tv_login)
        val cadastroButton: Button = findViewById(R.id.btn_cadastrar)
        val nomeEditText: EditText = findViewById(R.id.inputNome)
        val emailEditText: EditText = findViewById(R.id.inputEmail)
        val senhaEditText: EditText = findViewById(R.id.inputSenha)

        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        cadastroButton.setOnClickListener {
            val nome = nomeEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val senha = senhaEditText.text.toString().trim()

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            fazerCadastro(nome, email, senha)
        }
    }

    private fun fazerCadastro(nome: String, email: String, senha: String) {
        val cadastroRequest = CadastroRequest(nome, email, senha)
        val cadastroRequestList = listOf(cadastroRequest)  // Colocando o objeto em uma lista

        Log.d(TAG, "Enviando JSON para cadastro: $cadastroRequestList")

        RetrofitInstance.api.cadastro(cadastroRequestList).enqueue(object : Callback<CadastroResponse> {
            override fun onResponse(call: Call<CadastroResponse>, response: Response<CadastroResponse>) {
                Log.d(TAG, "Requisição enviada - Código de resposta: ${response.code()}")

                if (response.isSuccessful && response.body() != null) {
                    Log.d(TAG, "Cadastro realizado com sucesso")
                    Toast.makeText(this@CadastroActivity, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@CadastroActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Erro desconhecido"
                    Log.e(TAG, "Erro no cadastro - Código de resposta: ${response.code()}, Mensagem: $errorMessage")
                    Toast.makeText(this@CadastroActivity, "Erro no cadastro: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CadastroResponse>, t: Throwable) {
                Log.e(TAG, "Falha na requisição: ${t.message}", t)
                Toast.makeText(this@CadastroActivity, "Falha na requisição: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
