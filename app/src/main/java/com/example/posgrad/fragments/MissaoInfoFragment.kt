package com.example.posgrad.fragments


import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.posgrad.*
import com.example.posgrad.adapters.AtividadeInfoRecyclerAdapter

import com.example.posgrad.data_class.Atividade
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_missao_info.*

val atividades_locais = ArrayList<Atividade>()

class MissaoInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_missao_info, container, false)

        atividades_locais.clear() //Reseto a lista de atividades referentes a missão

        //Recycler Adapter set
        val atividade_recyclerview = rootView.findViewById(R.id.atividadeView) as RecyclerView
        atividade_recyclerview.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        atividade_recyclerview.adapter = AtividadeInfoRecyclerAdapter(atividades_locais, activity)
        
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        val bundle = arguments
        (activity as MainActivity).navigation.visibility = View.GONE

        Log.d("argumentos", bundle?.getString("time") + " " + bundle?.getString("missao")+ " " + bundle?.getString("descricao"))

        val missaoNomeString = bundle?.getString("missao")
        val timeNomeString = bundle?.getString("time")
        val missaoDescricaoString = bundle?.getString("descricao")
        val missaoPontos = bundle?.getInt("pontuacao")

        //Seto os elementos da UI
        missaoNome?.text = ("Missao " + missaoNomeString)
        missaoInfo?.text = missaoDescricaoString
        missaoPontuacao?.text = ("Pontuação: " + missaoPontos.toString())
        (activity as MainActivity).fragmentTitle?.text = timeNomeString
        (activity as MainActivity).backButtonVisible(1)

        //Função do backButton
        (activity as MainActivity).backButton.setOnClickListener {
            backButtonPressed()
        }

        findAtividades(missaoNomeString, timeNomeString)


        super.onActivityCreated(savedInstanceState)
    }

    fun findAtividades(missaoNomeString : String?, timeNomeString : String?){

        //Verifico todas as atividades referentes ao time e missão em questão
        for(atividade in atividade_collection){
            if(atividade.time_string == timeNomeString!!){
                if(atividade.missao_string == ("Missão " + missaoNomeString!!)){
                    atividades_locais.add(atividade)
                }
            }
        }

        Log.d("pikachu", atividades_locais.toString())
        atividadeView.adapter?.notifyDataSetChanged()
    }

    fun backButtonPressed(){
        (activity as MainActivity).backButtonVisible(0)
        (activity as MainActivity).fragmentTitle?.text = "Dashboard"
        (activity as MainActivity).navigation.visibility = View.VISIBLE
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.remove(MissaoInfoFragment())
        fragmentTransaction?.replace(R.id.fragmentContainer, DashBoardFragment())
        fragmentTransaction?.commit()
    }
}
