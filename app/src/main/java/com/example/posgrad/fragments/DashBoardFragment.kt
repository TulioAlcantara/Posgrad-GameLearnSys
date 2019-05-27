package com.example.posgrad.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.posgrad.*
import com.example.posgrad.adapters.*
import com.example.posgrad.data_class.TimePontuacao
import com.example.posgrad.recycler_adapters.ChartRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import android.os.Parcelable
import android.widget.TextView
import org.w3c.dom.Text


//val chart_recyclerView : RecyclerView

class DashBoardFragment : Fragment() {

    val spinner_items = arrayOf("1ª Temporada", "2ª Temporada", "3ª Temporada")
    val temporada_atual : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_dashboard, container, false)

        //Recycler Adapter set
        val chart_recyclerView = rootView.findViewById(R.id.chartView) as RecyclerView
        chart_recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        chart_recyclerView.adapter = ChartRecyclerAdapter(pontuacao_collection, activity)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        Log.d("agumon", timePontuacaoMain.toString())


        (activity as MainActivity).fragmentTitle.text = ("Dashboard")
        (activity as MainActivity).spinner.adapter = ArrayAdapter(
            activity, // Context
            android.R.layout.simple_spinner_dropdown_item, // Layout
            spinner_items // Array
        )

        //Spinner Adapter
        (activity as MainActivity).spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (view as TextView).setText(null)
                mudarTemporada(spinner_items.get(position))
                chartView.adapter?.notifyDataSetChanged()
            }

        }

        //Set main Chart
        val chartSet = ChartSet()
        timeNomeMain.text = timePontuacaoMain.nome
        chartSet.setBarChart(barChartMain, lineChartMain, timePontuacaoMain, changeChartMain, 1, activity)

        super.onActivityCreated(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("temporada", temporada_atual)
        super.onSaveInstanceState(outState)
    }

    fun mudarTemporada(temporada_atual : String){

        atividade_collection_temporada.clear()
        pontuacao_collection.clear()

        for(time in time_collection){

            val time_hash = HashMap(missaoPontuacaoHashMap) // Cada time recebe a copia de missaoPontuacaoHashMap
            val time_pontuacao = TimePontuacao(time.id, time_hash) // Inicializo time_pontuacao referente ao time

            for(atividade in atividade_collection){
                if (atividade.time.toString() == time.time_ref_string) {
                    for (missao in missao_collection) {
                        if (missao.missao_ref_string == atividade.missao.toString()) {

                            //Verifico a temporada
                            if (missao.temp == temporada_atual) {
                                //Atualizo a pontuação
                                val pontuacao_atual = time_pontuacao.pontuacao?.get(missao.nome)?.plus(atividade.pontuacao)
                                time_pontuacao.pontuacao?.put(missao.nome, pontuacao_atual)

                                Log.d("saber", time.id + "" + missao.nome)
                                atividade_collection_temporada.add(atividade) //Adiciono a atividade a minha lista de atividades daquela temporada
                            }
                        }
                    }

                }
            }

            //Verifico se eh o time do usuário
            if (time.id == usuario.time_string) {
                timePontuacaoMain = time_pontuacao
            } else {
                pontuacao_collection.add(time_pontuacao) //Adiciono a pontuação do time a minha lista
            }
        }

        //Set main Chart
        val chartSet = ChartSet()
        timeNomeMain.text = timePontuacaoMain.nome
        chartSet.setBarChart(barChartMain, lineChartMain, timePontuacaoMain, changeChartMain, 1, activity)

    }


}