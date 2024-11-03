package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.myapplication.network.RetrofitInstance
import com.example.myapplication.model.ClienteRequest
import com.example.myapplication.model.ClienteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Perfil : Fragment() {

    private lateinit var nomeTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var senhaTextView: TextView
    private val TAG = "PerfilFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        // Inicializa as TextViews
        nomeTextView = view.findViewById(R.id.tvNome)
        emailTextView = view.findViewById(R.id.tvEmail)
        senhaTextView = view.findViewById(R.id.tvSenha)

        // Recupera os dados do usuário de SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val nome = sharedPreferences.getString("nome", "Nome não disponível")
        val email = sharedPreferences.getString("email", "Email não disponível")
        val senha = sharedPreferences.getString("senha", "********")

        nomeTextView.text = nome
        emailTextView.text = email
        senhaTextView.text = senha

        // Botão de Sair
        val logoutButton: Button = view.findViewById(R.id.btn_logout)
        logoutButton.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }

        // Botão de Editar
        val editarButton: Button = view.findViewById(R.id.btn_edit)
        editarButton.setOnClickListener {
            mostrarPopupEdicao(sharedPreferences)
        }

        return view
    }

    // Função para mostrar o popup de edição de perfil
    private fun mostrarPopupEdicao(sharedPreferences: SharedPreferences) {
        val alertDialog = AlertDialog.Builder(requireContext())
        val popupView = layoutInflater.inflate(R.layout.popup_editar_perfil, null)
        alertDialog.setView(popupView)

        val nomeEditText = popupView.findViewById<EditText>(R.id.inputNome)
        val emailEditText = popupView.findViewById<EditText>(R.id.inputEmail)
        val senhaEditText = popupView.findViewById<EditText>(R.id.inputSenha)

        // Preenche os campos com os valores atuais
        nomeEditText.setText(nomeTextView.text)
        emailEditText.setText(emailTextView.text)
        senhaEditText.setText(senhaTextView.text)

        alertDialog.setPositiveButton("Confirmar") { dialog, _ ->
            val novoNome = nomeEditText.text.toString().trim()
            val novoEmail = emailEditText.text.toString().trim()
            val novaSenha = senhaEditText.text.toString().trim()

            val clienteId = sharedPreferences.getLong("clienteId", -1L)
            if (clienteId != -1L) {
                atualizarDadosNoBanco(clienteId, novoNome, novoEmail, novaSenha, sharedPreferences)
            } else {
                Log.e(TAG, "ID do cliente não encontrado no SharedPreferences")
                Toast.makeText(context, "Erro ao atualizar dados.", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        alertDialog.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialog.show()
    }

    // Função para atualizar os dados no banco de dados
    private fun atualizarDadosNoBanco(
        clienteId: Long,
        nome: String,
        email: String,
        senha: String,
        sharedPreferences: SharedPreferences
    ) {
        val clienteRequest = ClienteRequest(nome, email, senha)
        RetrofitInstance.api.updateCliente(clienteId, clienteRequest).enqueue(object : Callback<ClienteResponse> {
            override fun onResponse(call: Call<ClienteResponse>, response: Response<ClienteResponse>) {
                if (response.isSuccessful) {
                    // Atualiza as TextViews
                    nomeTextView.text = nome
                    emailTextView.text = email
                    senhaTextView.text = senha

                    // Salva os novos dados em SharedPreferences
                    sharedPreferences.edit().apply {
                        putString("nome", nome)
                        putString("email", email)
                        putString("senha", senha)
                        apply()
                    }
                    Toast.makeText(context, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "Erro ao atualizar dados: ${response.message()}")
                    Toast.makeText(context, "Erro ao atualizar dados.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ClienteResponse>, t: Throwable) {
                Log.e(TAG, "Erro na requisição: ${t.message}", t)
                Toast.makeText(context, "Erro na requisição: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
