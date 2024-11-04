package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class Historico : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_historico, container, false)
        carregarHistorico(view)
        return view
    }

    private fun carregarHistorico(view: View) {
        val sharedPreferences = requireContext().getSharedPreferences("historico_alimentos", Context.MODE_PRIVATE)
        val historico = sharedPreferences.getStringSet("historico_lista", mutableSetOf()) ?: mutableSetOf()

        val historicoList = historico.toList()
        val historicoLayout = view.findViewById<LinearLayout>(R.id.historicoLayout)
        historicoLayout.removeAllViews()

        for (item in historicoList) {
            val itemView = LayoutInflater.from(context).inflate(R.layout.historico_item, historicoLayout, false)
            val itemText = itemView.findViewById<TextView>(R.id.itemText)

            // Define o texto do item
            itemText.text = item

            // Define a cor de fundo com base na qualidade
            val cardView = itemView as CardView
            when {
                item.contains("Boa") -> cardView.setCardBackgroundColor(Color.parseColor("#4CAF50")) // Verde para "Boa"
                item.contains("Média") -> cardView.setCardBackgroundColor(Color.parseColor("#FFC107")) // Amarelo para "Média"
                item.contains("Ruim") -> cardView.setCardBackgroundColor(Color.parseColor("#F44336")) // Vermelho para "Ruim"
            }

            historicoLayout.addView(itemView)
        }
    }
}
