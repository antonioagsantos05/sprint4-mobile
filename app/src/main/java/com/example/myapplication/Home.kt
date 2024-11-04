package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

class Home : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Referenciar o botão circular dentro do fragment
        val btnTirarFoto: Button = view.findViewById(R.id.btn_tirar_foto)

        // Adicionar o clique no botão
        btnTirarFoto.setOnClickListener {
            mostrarPopupSelecao()
        }

        return view
    }

    private fun mostrarPopupSelecao() {
        val alertDialog = AlertDialog.Builder(requireContext())
        val popupView = layoutInflater.inflate(R.layout.popup_selecao_alimento, null)
        alertDialog.setView(popupView)

        val tipoAlimentoSpinner: Spinner = popupView.findViewById(R.id.spinnerTipoAlimento)
        val qualidadeSpinner: Spinner = popupView.findViewById(R.id.spinnerQualidade)

        // Lista de tipos de alimentos e qualidades
        val tiposAlimentos = listOf("Banana", "Tomate", "Maçã", "Cenoura", "Alface")
        val qualidades = listOf("Boa", "Média", "Ruim")

        tipoAlimentoSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tiposAlimentos)
        qualidadeSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, qualidades)

        alertDialog.setPositiveButton("Confirmar") { dialog, _ ->
            val tipoAlimentoSelecionado = tipoAlimentoSpinner.selectedItem.toString()
            val qualidadeSelecionada = qualidadeSpinner.selectedItem.toString()

            // Salvar dados no SharedPreferences
            salvarDadosNoSharedPreferences(tipoAlimentoSelecionado, qualidadeSelecionada)

            dialog.dismiss()
        }

        alertDialog.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialog.show()
    }

    private fun salvarDadosNoSharedPreferences(tipoAlimento: String, qualidade: String) {
        val sharedPreferences = requireContext().getSharedPreferences("historico_alimentos", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Obter a lista atual de alimentos salvos
        val historico = sharedPreferences.getStringSet("historico_lista", mutableSetOf()) ?: mutableSetOf()

        // Adicionar novo item à lista
        historico.add("$tipoAlimento - $qualidade")

        // Salvar de volta no SharedPreferences
        editor.putStringSet("historico_lista", historico)
        editor.apply()

        Toast.makeText(context, "Dados salvos no histórico", Toast.LENGTH_SHORT).show()
    }
}
