package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class Home : Fragment() {

    // Declaração das constantes para as chaves dos parâmetros
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
            // Navegar para ErroActivity
            val intent = Intent(activity, ErroActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
        // Definir as constantes para as chaves dos parâmetros
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
