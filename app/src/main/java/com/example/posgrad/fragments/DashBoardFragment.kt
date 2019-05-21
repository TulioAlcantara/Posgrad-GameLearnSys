package com.example.posgrad.fragments

import android.content.Context
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
import com.example.posgrad.adapters.ChartAdapter
import com.example.posgrad.adapters.ChartSet
import com.example.posgrad.adapters.FireStoreQuery
import com.example.posgrad.adapters.missao_nome
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


//val chart_recyclerView : RecyclerView

class DashBoardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_dashboard, container, false)



        //Recycler Adapter set
        val chart_recyclerView = rootView.findViewById(R.id.chartView) as RecyclerView
        chart_recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        chart_recyclerView.adapter = ChartAdapter(pontuacao_collection, activity)

        return rootView

        /*
        for(element in pontuacao_collection){
            Log.d("pontuacao_collection", element.nome + ": " + element.pontuacao?.values)
        }
        */
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val context = activity
        val chartSet = ChartSet()

        //Set main Chart
        timeNomeMain.text = timePontuacaoMain.nome
        chartSet.setBarChart(barChartMain, lineChartMain, timePontuacaoMain, changeChartMain, 1, context)

        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        //chartView.adapter = ChartAdapter(pontuacao_collection, activity!!.applicationContext)
        super.onResume()
    }
}