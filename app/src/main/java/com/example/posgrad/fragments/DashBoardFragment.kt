package com.example.posgrad.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.posgrad.*
import com.example.posgrad.adapters.ChartAdapter
import com.example.posgrad.adapters.ChartSet
import com.example.posgrad.adapters.missao_nome
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.fragment_dashboard.*

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
        val chartSet = ChartSet()

        //Set main Chart
        timeNomeMain.text = timePontuacaoMain.nome
       chartSet.setBarChart(barChartMain, lineChartMain, timePontuacaoMain, changeChartMain)

        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        chartView.adapter = ChartAdapter(pontuacao_collection)
        super.onResume()
    }
}