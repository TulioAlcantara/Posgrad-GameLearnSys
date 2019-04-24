package com.example.posgrad.fragments

import android.app.ActionBar
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.posgrad.*
import com.example.posgrad.adapters.ChartAdapter
import com.example.posgrad.data_class.Atividade
import com.example.posgrad.data_class.Missao
import com.example.posgrad.data_class.Time
import com.example.posgrad.data_class.TimePontuacao
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.android.synthetic.main.layout_chart.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DashBoardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_dashboard, container, false)

        //Definição do RecyclerView
        val chart_recyclerView = rootView.findViewById(R.id.chartView) as RecyclerView
        chart_recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        chart_recyclerView.adapter = ChartAdapter(pontuacao_collection)

        for(element in pontuacao_collection){
            Log.d("pontuacao_collection", element.nome + ": " + element.pontuacao?.values)
        }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setMainChart()
        super.onActivityCreated(savedInstanceState)

    }

    override fun onResume() {
        chartView.adapter = ChartAdapter(pontuacao_collection)
        super.onResume()
    }

    //Seto os valores do chart do time do usuário
    fun setMainChart(){
        timeNomeMain.text = timePontuacaoMain.nome

        val entries = ArrayList<BarEntry>() // Array com os pontos
        var x = 0

        //Adiciono a pontuação de cada missão no meu array
        for(missao in timePontuacaoMain.pontuacao.orEmpty()){
            val entry : BarEntry = BarEntry(x.toFloat(), missao.value!!.toFloat())
            entries.add(entry)
            x = x+1
        }

        val dataset : BarDataSet = BarDataSet(entries, "pontuacao")

        val data : BarData = BarData(dataset)
        data.setValueTextSize(12.toFloat())
        data.setValueTextColor(android.R.color.white)

        //Propriedades do chart
        chartMain.data = data
        chartMain.invalidate()
        chartMain.setFitBars(true)
        chartMain.description.isEnabled = false
        chartMain.legend.isEnabled = false
        chartMain.animateY(1000)

        //Propriedades do xAxis
        chartMain.xAxis.valueFormatter = ChartAdapter.MyxAxisFormatter()
        chartMain.xAxis.granularity = 1f
        chartMain.xAxis.position = XAxis.XAxisPosition.BOTTOM

        //Propriedades do leftAxis
        chartMain.axisRight.isEnabled = false

        //Propriedades do rightAxis
        chartMain.axisLeft.isEnabled = true
        chartMain.axisLeft.axisMinimum = 0.toFloat()
        chartMain.axisLeft.axisMaximum = 120.toFloat()
    }

    /*
    fun testData(db : FirebaseFirestore){
        val hash_test = missaoPontuacaoHashMap

        hash_test.put("missao1", 60)
        hash_test.put("missao2", 80)
        hash_test.put("missao3", 70)
        val data_test : TimePontuacao = TimePontuacao("TIME TESTE", hash_test)
        pontuacao_collection.add(data_test)
    }
    */
}