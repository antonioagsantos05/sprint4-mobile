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

        val sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val clienteId = sharedPreferences.getLong("clienteId", -1L)

        if (clienteId == -1L) {
            Log.e(TAG, "Erro: ID do cliente não encontrado no SharedPreferences")
            Toast.makeText(context, "Erro ao carregar dados do cliente", Toast.LENGTH_SHORT).show()
        } else {
            nomeTextView.text = sharedPreferences.getString("nome", "")
            emailTextView.text = sharedPreferences.getString("email", "")
            senhaTextView.text = "********"
            Log.d(TAG, "Cliente carregado: $clienteId")
        }

        // Configuração do botão de logout
        val logoutButton: Button = view.findViewById(R.id.btn_logout)
        logoutButton.setOnClickListener {
            Log.d(TAG, "Logout iniciado")
            sharedPreferences.edit().clear().apply()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }

        // Configuração do botão de editar
        val editarButton: Button = view.findViewById(R.id.btn_edit)
        editarButton.setOnClickListener {
            mostrarPopupEdicao(sharedPreferences)
        }

        // Configuração do botão de deletar conta
        val deleteButton: Button = view.findViewById(R.id.btn_delete)
        deleteButton.setOnClickListener {
            confirmarDelecao(clienteId, sharedPreferences)
        }

        return view
    }

    private fun mostrarPopupEdicao(sharedPreferences: SharedPreferences) {
        val alertDialog = AlertDialog.Builder(requireContext())
        val popupView = layoutInflater.inflate(R.layout.popup_editar_perfil, null)
        alertDialog.setView(popupView)

        val nomeEditText = popupView.findViewById<EditText>(R.id.inputNome)
        val emailEditText = popupView.findViewById<EditText>(R.id.inputEmail)
        val senhaEditText = popupView.findViewById<EditText>(R.id.inputSenha)

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
                    nomeTextView.text = nome
                    emailTextView.text = email
                    senhaTextView.text = senha

                    sharedPreferences.edit().apply {
                        putString("nome", nome)
                        putString("email", email)
                        putString("senha", senha)
                        apply()
                    }
                    Log.d(TAG, "Dados atualizados com sucesso para o cliente ID: $clienteId")
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

    private fun confirmarDelecao(clienteId: Long, sharedPreferences: SharedPreferences) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza de que deseja deletar sua conta?")
            .setPositiveButton("Sim") { _, _ ->
                deletarConta(clienteId, sharedPreferences)
            }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun deletarConta(clienteId: Long, sharedPreferences: SharedPreferences) {
        RetrofitInstance.api.deleteCliente(clienteId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Conta deletada com sucesso para o cliente ID: $clienteId")
                    Toast.makeText(context, "Conta deletada com sucesso!", Toast.LENGTH_SHORT).show()
                    sharedPreferences.edit().clear().apply()

                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    Log.e(TAG, "Erro ao deletar conta: ${response.code()} - ${response.message()}")
                    Toast.makeText(context, "Erro ao deletar conta: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e(TAG, "Erro ao deletar conta: ${t.message}", t)
                Toast.makeText(context, "Falha na requisição: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
