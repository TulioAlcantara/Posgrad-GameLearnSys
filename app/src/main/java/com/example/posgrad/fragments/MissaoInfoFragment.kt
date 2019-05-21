package com.example.posgrad.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.posgrad.MainActivity

import com.example.posgrad.R
import com.example.posgrad.atividade_collection
import com.example.posgrad.data_class.Atividade
import com.example.posgrad.missao_collection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_missao_info.*

val atividades_local = ArrayList<Atividade>()

class MissaoInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_missao_info, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        val bundle = arguments

        Log.d("argumentos", bundle?.getString("time") + " " + bundle?.getString("missao"))

        val missao_nome = bundle?.getString("missao")
        val time_nome = bundle?.getString("time")
        val missao_descricao = bundle?.getString("descricao")

        missaoNome?.text = missao_nome
        fragmentTitle?.text = time_nome
        missaoInfo?.text = missao_descricao

        findAtividades(missao_nome, time_nome)

        /*
        backButton.isVisible = true
        spinner.isVisible = false
        backButton.setOnClickListener {
            backButtonPressed()
        }
        */
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun findAtividades(missao_nome : String?, time_nome : String?){

        //Verifico todas as atividades referentes ao time e missão em questão
        for(atividade in atividade_collection){
            if(atividade.time_string == time_nome){
                if(atividade.missao_string == missao_nome){
                    atividades_local.add(atividade)
                }
            }
        }
    }

    fun backButtonPressed(){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragmentContainer, DashBoardFragment())
        fragmentTransaction?.commit()
    }
}
